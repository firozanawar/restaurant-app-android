package com.firozanwar.sample.restaurantapp;

import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Helper class for working with a remote server
 */
public class OkHttpHelper {

    /**
     * Returns text from a URL on a web server
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address, String username, String password, RequestPackage requestPackage) throws IOException {

        String endpoint = requestPackage.getEndpoint();
        String encodeParam = requestPackage.getEncodedParams();
        if (requestPackage.getMethod().equals("GET") && encodeParam.length() > 0) {
            endpoint = String.format("%s?%s", endpoint, encodeParam);
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(endpoint);


        if(requestPackage.getMethod().equals("POST")){
            MultipartBody.Builder multiBuilder=new MultipartBody.Builder().setType(MultipartBody.FORM);
            Map<String, String> params=requestPackage.getParams();
            for (String key:params.keySet()) {
                multiBuilder.addFormDataPart(key,params.get(key));
            }

            RequestBody requestBody=multiBuilder.build();
            builder.method("POST",requestBody);

        }

        Request request = builder.build();
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Exception response code :: " + response.code());
        }
    }
}
