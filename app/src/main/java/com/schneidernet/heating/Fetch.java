package com.schneidernet.heating;


import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by werner on 22.06.17.
 */

public class Fetch {

    public static final String USER = "foo";
    public static final String PASSWORD = "bar";
    public static final String BASE_URL = "http://212.47.234.165:8080";
    public static final String ENDPOINT = "";

    public static JSONArray getJSON(String restAPI) {
        URL url;
        try {
            url = new URL(BASE_URL + ENDPOINT + restAPI);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            final String basicAuth = "Basic " + Base64.encodeToString((USER + ":" + PASSWORD).getBytes(), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", basicAuth);

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));

            StringBuffer json = new StringBuffer(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            return new JSONArray(String.valueOf(json));

        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
