package com.firozanwar.sample.restaurantapp;

import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {

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


        InputStream is = null;

        // Authentication
        byte[] loginbytes = (username + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginbytes, Base64.DEFAULT));
        try {

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization", loginBuilder.toString());
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //conn.setRequestMethod("GET");
            conn.setRequestMethod(requestPackage.getMethod());
            conn.setDoInput(true);

            // For post add the below code
            if (requestPackage.getMethod().equals("POST") && encodeParam.length() > 0) {
               conn.setDoOutput(true);
                OutputStreamWriter writer= new OutputStreamWriter(conn.getOutputStream());
                 writer.write(requestPackage.getEncodedParams());
                writer.flush();
                writer.close();
            }

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            return readStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
