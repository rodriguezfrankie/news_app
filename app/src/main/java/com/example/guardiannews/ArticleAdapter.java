package com.example.guardiannews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//an {@link ArticleAdapter} knows how to create a list item layout for each article
//in the data source (a list od {@link Article} objects). These list items layout will be
//provided to an adapter view like ListView to be displayed to the user
public class ArticleAdapter extends ArrayAdapter<Article> {

    //construct a new {@link ArticleAdapter} @param context of the app
    //@param articles is the list of articles the data source of the adapter
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    //returns a list item view that displays information about the article at the given position
    //in the article list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //check for existing list item view (called convertView) that can be used
        //otherwise if counterView is null then inflate a new list item layout
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        //find storm at the given position in the list of storms
        Article currentArticle = getItem(position);

        //find the textview with id news_type
        TextView typeView = (TextView) listItemView.findViewById(R.id.news_type);
        //display the type of news in the textview
        typeView.setText(currentArticle.getType());

        //find the textview with id article_title
        TextView titleView = (TextView) listItemView.findViewById(R.id.article_title);
        //display the title of the article in the textview
        titleView.setText(currentArticle.getTitle());


        //find the textview with id headline
        TextView dateView = (TextView) listItemView.findViewById(R.id.article_date);
        //display the date of article in the textview
        dateView.setText(currentArticle.getDate());

        //return list item View showing appropriate data
        return listItemView;

    }

}
