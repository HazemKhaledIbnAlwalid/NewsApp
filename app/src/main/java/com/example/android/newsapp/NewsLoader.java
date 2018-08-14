package com.example.android.newsapp;

import  android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazem_Khaled on 2017-10-24.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream IS = connection.getInputStream();
            InputStreamReader ISR = new InputStreamReader(IS);
            int data = ISR.read();
            String urlContent = "";
            while (data != -1) {

                char c = (char) data;
                urlContent += c;
                data = ISR.read();
            }

            List<News> newsList = new ArrayList<>();
            JSONObject root = new JSONObject(urlContent);
            JSONObject response;
            if(root.has("response")) {
                response = root.getJSONObject("response");
                JSONArray results;
                if(response.has("results")) {
                    results=response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject news = results.getJSONObject(i);
                        String newsCategory = "Not Mentioned";
                        if (news.has("sectionName")) {
                            newsCategory = news.getString("sectionName");
                        }

                        String newsTitle = "Not Mentioned";
                        if (news.has("webTitle")) {
                            newsTitle = news.getString("webTitle");
                        }

                        String newsLink="Not Mentioned";
                        if (news.has("webUrl")) {
                            newsLink = news.getString("webUrl");
                        }

                        String newsDate="Not Mentioned";
                        if(news.has("webPublicationDate")){
                            newsDate=news.getString("webPublicationDate");
                        }

                        String newsAuthor = "Not Mentioned";
                        JSONArray authors;
                        if(news.has("tags")) {
                            authors=news.getJSONArray("tags");
                            for(int k=0;k<authors.length();k++) {
                                JSONObject obj = authors.getJSONObject(k);

                                if (obj.has("firstName")) {
                                    newsAuthor = obj.getString("firstName") + ' ';
                                }
                                if (obj.has("lastName")) {
                                    newsAuthor += (obj.getString("lastName")+ "\n\n");
                                }
                            }
                        }
                        News newsObj = new News(newsTitle, newsCategory, newsLink,newsDate,newsAuthor);
                        newsList.add(newsObj);
                    }
                }
            }
            connection.disconnect();
            return newsList;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
