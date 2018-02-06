package com.example.t00035975.bookapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by T00035975 on 2/5/2018.
 */

public class NetworkUtils {
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";

    public static String getBookInfo(String queryString) {
        Uri bookUri = Uri.parse(BOOK_BASE_URL)
                .buildUpon().appendQueryParameter(QUERY_PARAM, queryString)
                .appendQueryParameter(MAX_RESULTS, "5").build();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse = "";

        try {
            URL requestURL = new URL(bookUri.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer buffer = new StringBuffer();


            String l;
            while((l = reader.readLine()) != null)
            {
                buffer.append(l);
                buffer.append("\n");
            }

            if(buffer.length() == 0)
            {
                return null;
            }

            jsonResponse = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResponse;
    }

    public static String parseJSON(String jsonString){
        String authorName = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            for(int i=0; i<itemsArray.length(); i++)
            {
                String title = null;
                String author = null;
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                try {
                    title = volumeInfo.getString("title");
                    author = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(title != null && author != null) {
                    return "Title: " + title + "\nAuthor" + author;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return authorName;
    }
}
