package com.example.guardiannews;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

public final class QueryUtils {

    //tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //
    private QueryUtils() {
    }

    //query the theguardian.com dataset and return a list of {@link Article} objects
    public static List<Article> fetchArticleData(String requestUrl) {
        //create url object
        URL url = createUrl(requestUrl);

        //perform Http rewuest to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //extract relevant fields from the JSON response and create a list of {@link Article}
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        //retrun the list od {@link Article}
        return articles;
    }

    //return new URL object from the given string URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    //make an HTTP request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if the URL is null then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if request was successful (response code 200)
            //then read the input stream and parse out response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the storm JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //closing the input stream could throw an IOExcption which is why
                //the makeHttpRequest(URL url) method sigmature specifies and IOException
                //could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //convert the [@link InputStream} into a string which contains the whole json response
    //from server
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //return list of {@link Article} objects that has been built up from parsing given jsonResponse
    private static List<Article> extractFeatureFromJson(String articlesJSON) {
        //if json string is empty or null then return ealry
        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }

        //create an empty arraylist that we can start adding news
        List<Article> articles = new ArrayList<>();

        //try to parse the json response string. if there is a problem with the way json is
        //formatted,  jsonexception exception object will be thrown.  catch the exception so the
        //app doesn't crash and print error message to the logs
        try {
            //create a jsonObject from the json response string
            JSONObject baseJsonResponse = new JSONObject("response");

            //extract the JSONArray associated with the key called features which
            //represents a list of features (or news)
            JSONArray articlesArray = baseJsonResponse.getJSONArray("results");

            //for each news in the newsArray create a {@link News} object
            for (int i = 0; i < articlesArray.length(); i++) {
                //get a single storm at i within the list of storms
                JSONObject currentArticles = articlesArray.getJSONObject(i);

                //for a given news extract the JSONObject associated with the object
                // called properties which represents all the properties for the news
                JSONObject results = currentArticles.getJSONObject("results");

                //extract the value for the key called areaDesc
                String type = results.getString("sectionName");

                //extract the value for the key called areaDesc
                String title = results.getString("webTitle");

                //extract the value for the key called areaDesc
                String date = results.getString("webPublicationDate");

                //extract the value for the key called areaDesc
                String url = results.getString("webUrl");

                //create a new {@link Article} object with section name, web title, web publication date, and web url
                //from the JSON response
                Article article = new Article(type, title, date, url);

                //add the new {@link Article} to the list article
                articles.add(article);
            }

        } catch (JSONException e) {
            //if an error is thrown when executing any of the above statements in the try block
            //catch the exception here so the app doesnt crash. print a log message
            //witht the exception message
            Log.e("QueryUtils", "Problem parsing the article JSOn results, e");
        }

        //return the list of articles
        return articles;
    }
}
