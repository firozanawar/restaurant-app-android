package com.firozanwar.sample.restaurantapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.firozanwar.sample.restaurantapp.OkHttpHelper;
import com.firozanwar.sample.restaurantapp.RequestPackage;
import com.firozanwar.sample.restaurantapp.models.DataItem;
import com.firozanwar.sample.restaurantapp.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyNewService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicepayload";
    public static final String MY_SERVICE_EXCEPTION = "myServiceexception";
    public static final String REQUEST_PACKAGE = "requestpackage";

    public MyNewService() {
        super("MyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Utils.DEBUG_TAG, "onCreate called");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        Call<DataItem[]> call=myWebService.dataitems();

        DataItem[] dataItems;

        // Synchronous
        try {
            dataItems=call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(Utils.DEBUG_TAG,"onHandleIntent :: "+e.getMessage());
            return;
        }

        /**
         *  Send the broadcast message with the data.
         *  This message is not coupled with any activity/fragment. Anyone from the
         *  app can listen to it and respond.
         */
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(messageIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Utils.DEBUG_TAG, "onDestroy called");
    }
}
