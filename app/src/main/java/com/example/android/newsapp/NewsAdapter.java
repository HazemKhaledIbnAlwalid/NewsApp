package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hazem_Khaled on 2017-10-24.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Activity context, ArrayList<News> NewsInfo){
        super(context,0,NewsInfo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null)
            listView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);

        News news=getItem(position);

        TextView sectionName=(TextView)listView.findViewById(R.id.NewsSectionName);
        TextView date=(TextView)listView.findViewById(R.id.NewsDate);
        TextView title=(TextView)listView.findViewById(R.id.NewsTitle);
        TextView author=(TextView)listView.findViewById(R.id.NewsAuthorName);

        if(news!=null) {
            sectionName.setText(news.getNewsSection());
            date.setText(news.getNewsDate());
            title.setText(news.getNewsTitle());
            author.setText(news.getNewsAuthor());
        }
        else{
            sectionName.setText("there is no News for this category you have entered,please try another category");
            date.setText("");
            title.setText("");
        }
        return listView;
    }

}

