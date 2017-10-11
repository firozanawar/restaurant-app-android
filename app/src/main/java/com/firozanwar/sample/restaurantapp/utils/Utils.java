package com.firozanwar.sample.restaurantapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by firozanwar on 10/10/17.
 */

public class Utils {

    public static final String DEBUG_TAG="restaurantapp";

    /**
     * Check for internet connectivity.
     *
     * @param context
     * @return
     */
    public static boolean hasNetworkStatus(Context context){

        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            return networkInfo!=null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
