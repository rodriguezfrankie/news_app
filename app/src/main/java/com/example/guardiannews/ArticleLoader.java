package com.example.guardiannews;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    //tag for log messages
    private static final String LOG_TAG = ArticleLoader.class.getName();

    //query URL
    private String mUrl;

    //constructs a new {@link StormLoader}
    //@param context of the activity, @param url to load data from
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //this is background thread
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        //perform the network request, parse the response and extract the list of storms
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
