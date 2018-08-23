package com.example.android.randomrhyme.networking;

import com.example.android.randomrhyme.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {


    private static final String BASE_URL = "https://api.poet.hu/vers.php?f=";
    private static final String PARAM_API_KEY = "&j=";
    private final static String USERNAME = BuildConfig.USERNAME;
    private final static String API_KEY = BuildConfig.API_KEY;
    private final static String CATEGORY = "&kat=Szerelem";
    private final static String ORDER = "&rendez=veletlen";

    private NetworkUtils() {
    }

    public static URL buildUrl() {
        URL url = null;
        try {
            url = new URL(BASE_URL + USERNAME + PARAM_API_KEY + API_KEY + CATEGORY + ORDER);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}


