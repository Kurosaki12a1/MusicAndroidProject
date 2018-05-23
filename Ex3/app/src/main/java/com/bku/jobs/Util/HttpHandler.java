package com.bku.jobs.Util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Huy on 05/22/18.
 */

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl){
        String respone = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // read the respone
            InputStream in = new BufferedInputStream(connection.getInputStream());
            respone = convertStreamToString(in);
        } catch (MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage() );
        } catch (ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e){
            Log.e(TAG, "IOException: " + e.getMessage() );
        } catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage() );
        }
        return respone;
    }

    private String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
