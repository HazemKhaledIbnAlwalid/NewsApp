package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    String url;
    EditText searchFeild;
    ListView newsListView;
    NewsAdapter apdapter;
    ArrayList<News> content = new ArrayList<News>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsListView = (ListView) findViewById(R.id.MyList);
        searchFeild = (EditText) findViewById(R.id.Search_EditText);

        View empty = getLayoutInflater().inflate(R.layout.empty_list_view, null, false);
        addContentView(empty, new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        newsListView.setEmptyView(empty);

        if (savedInstanceState != null) {
            content = savedInstanceState.getParcelableArrayList("content");
            apdapter = new NewsAdapter(this, content);
        } else {
            apdapter = new NewsAdapter(this, new ArrayList<News>());
        }

        newsListView.setAdapter(apdapter);

        Button searchButton = (Button) findViewById(R.id.Search_Button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchFeild.getWindowToken(), 0);

                String userInputSearchFeild = searchFeild.getText().toString().toLowerCase();
                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
                try {
                    userInputSearchFeild = URLEncoder.encode(userInputSearchFeild, "UTF-8");
                    ConnectivityManager cm =
                            (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        url = "http://content.guardianapis.com/search?q=" + userInputSearchFeild + "&api-key=b298334e-5b6f-4296-89c1-d154e3f11cee&show-tags=contributor";
                        LoaderManager loaderManager = getSupportLoaderManager();
                        if (loaderManager.getLoader(0) == null) {
                            loaderManager.initLoader(0, null, NewsActivity.this).forceLoad();
                        } else {
                            loaderManager.restartLoader(0, null, NewsActivity.this).forceLoad();
                        }
                        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(News.urlList.get(position)));
                                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(browserIntent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "You have a problem with your browser", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("content", content);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        apdapter.clear();
        if (data != null && !data.isEmpty())
            apdapter.addAll(data);
        apdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        apdapter.clear();
    }

}
