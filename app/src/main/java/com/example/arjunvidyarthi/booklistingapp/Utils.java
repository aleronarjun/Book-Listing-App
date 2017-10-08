package com.example.arjunvidyarthi.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class Utils {

    private static final String LOG_TAG ="" ;

    private Utils(){

    }

    public static URL convertToURL(String url){
        URL URL = null;
        try {
            URL = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return URL;
    }



    public static String getFromStream (InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    public static String makeHTTPRequest (URL url) throws IOException{
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = getFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static ArrayList<bookData> parseResponse(String JSONResponse) throws JSONException {


        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }
        ArrayList<bookData> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(JSONResponse);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {

                JSONObject currentbook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = currentbook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                JSONArray authorsJSON = new JSONArray();
                ArrayList<String> authors = new ArrayList<>();
                try {
                    authorsJSON = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authorsJSON.length(); j++) {
                        authors.add(authorsJSON.getString(j));
                    }
                }
                catch (JSONException e){
                    Log.e(LOG_TAG, "No authors given.", e);
                    authors.add("Unknown author");
                }

                JSONObject Img = volumeInfo.getJSONObject("imageLinks");
                String ImgLink = Img.getString("smallThumbnail");
                Bitmap bm = getBitmapFromURL(ImgLink);

                String infoL = volumeInfo.getString("infoLink");

                bookData book = new bookData(title, authors, bm, infoL);

                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }


        return books;
    }

    public static ArrayList<bookData> networkReq(String url) throws JSONException {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e(LOG_TAG, "onNetworkReq");

        URL URL = convertToURL(url);

        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(URL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<bookData> books = parseResponse(jsonResponse);

        return books;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
