package com.firozanwar.sample.restaurantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firozanwar.sample.restaurantapp.models.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by firozanwar on 11/10/17.
 */

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    private List<DataItem> mItems;
    private Context mContext;
    //private Map<String, Bitmap> mBitmapMap = new HashMap<>();

    public DataItemAdapter(List<DataItem> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    public DataItemAdapter(List<DataItem> mItems, Map<String, Bitmap> bitmapMap, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
        //this.mBitmapMap = bitmapMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from((mContext));
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DataItem item = mItems.get(position);

        try {
            holder.tvName.setText(item.getItemName());

            /**
             *  Case - Get the bitmap from the Map and display
             */
            //Bitmap b = mBitmapMap.get(item.getItemName());
            //holder.imageView.setImageBitmap(b);


            /**
             * Case - Downloading images on demand using AsyncTask.
             */
            /*if (mBitmapMap.containsKey(item.getItemName())) {
                Bitmap b = mBitmapMap.get(item.getItemName());
                holder.imageView.setImageBitmap(b);
            } else {
                ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask();
                imageDownloaderTask.setViewHolder(holder);
                imageDownloaderTask.execute(item);
            }*/

            /**
             * Getting images from cache.
             */
            Bitmap bitmap = ImageCacheManager.getBitmap(mContext, item);
            if (bitmap == null) {
                ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask();
                imageDownloaderTask.setViewHolder(holder);
                imageDownloaderTask.execute(item);
            } else {
                holder.imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }

    private class ImageDownloaderTask extends AsyncTask<DataItem, Void, Bitmap> {

        public static final String PHOTO_BASE_URL = "http://560057.youcanlearnit.net/services/images/";
        private DataItem mDataItem;
        private ViewHolder viewHolder;

        public void setViewHolder(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        protected Bitmap doInBackground(DataItem... dataItems) {

            mDataItem = dataItems[0];
            String imageurl = PHOTO_BASE_URL + mDataItem.getImage();
            InputStream in = null;

            try {
                in = (InputStream) new URL(imageurl).getContent();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            viewHolder.imageView.setImageBitmap(bitmap);
            //mBitmapMap.put(mDataItem.getItemName(), bitmap); // For future use
            ImageCacheManager.putBitmap(mContext, mDataItem, bitmap);
        }
    }
}
