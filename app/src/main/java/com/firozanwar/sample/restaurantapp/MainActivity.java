package com.firozanwar.sample.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firozanwar.sample.restaurantapp.models.DataItem;
import com.firozanwar.sample.restaurantapp.services.MyService;
import com.firozanwar.sample.restaurantapp.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Map<String, Bitmap>> {

    private static final String JSON_URL = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    private static final String SECURE_JSON_URL = "http://560057.youcanlearnit.net/secured/json/itemsfeed.php";

    List<DataItem> mListItem;
    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;

    Map<String, Bitmap> mBitmapMap;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                // String message=intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
                DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
                mListItem = Arrays.asList(dataItems);

                /**
                 * Case - when you want entire images to be downloaded and inserted into HashMap to display
                 */
                //getSupportLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();

                displayDataItems();

            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)) {
                String exceptionMessage = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, exceptionMessage, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);


        if (Utils.hasNetworkStatus(this)) {

            RequestPackage requestPackage=new RequestPackage();
            requestPackage.setEndPoint(SECURE_JSON_URL);
             requestPackage.setParam("category","Desserts");

            // For POST add below code else comment it for GET
            requestPackage.setMethod("POST");

            /**
             * Start the intent service and
             * pass the URL to service to hit
             */
            Intent startServiceIntent = new Intent(this, MyService.class);
            //startServiceIntent.setData(Uri.parse(SECURE_JSON_URL));

            // Use this when you want to send request package detais like method, params etc
            startServiceIntent.putExtra(MyService.REQUEST_PACKAGE,requestPackage);
            startService(startServiceIntent);
        } else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }

        // Register the broadcast to get the response from intent service
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(MyService.MY_SERVICE_MESSAGE));
    }

    private void displayDataItems() {
        if (mListItem != null) {
            //mItemAdapter = new DataItemAdapter(mListItem, mBitmapMap, this);
            mItemAdapter = new DataItemAdapter(mListItem, this);
            mRecyclerView.setAdapter(mItemAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the broadcast.
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public Loader<Map<String, Bitmap>> onCreateLoader(int id, Bundle args) {
        return new ImageDownloader(this, mListItem);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Bitmap>> loader, Map<String, Bitmap> data) {
        mBitmapMap = new HashMap<>();
        mBitmapMap = data;
        displayDataItems();
    }

    @Override
    public void onLoaderReset(Loader<Map<String, Bitmap>> loader) {
    }


    /**
     * Lazy loading
     *
     * Download the images from different API based on some name.
     * Save these images in HasMap and then pass this to an adapter.
     * This is not recommaned because use has to wait untill all images downloaded and saved
     * to Bitmap.
     */
    private static class ImageDownloader extends AsyncTaskLoader<Map<String, Bitmap>> {

        public static final String PHOTO_BASE_URL = "http://560057.youcanlearnit.net/services/images/";
        private List<DataItem> mListItem;

        public ImageDownloader(Context context, List<DataItem> listItem) {
            super(context);
            mListItem = listItem;
        }

        @Override
        public Map<String, Bitmap> loadInBackground() {

            Map<String, Bitmap> btm = new HashMap<>();
            for (DataItem item : mListItem) {
                String imageurl = PHOTO_BASE_URL + item.getImage();
                InputStream in = null;
                try {
                    in = (InputStream) new URL(imageurl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    btm.put(item.getItemName(), bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return btm;
        }
    }
}
