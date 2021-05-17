package com.example.flickrphotodemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable{
    private static final String TAG = "MainActivity";
    private FlickrRecycleViewAdapter recycleViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recycleViewAdapter = new FlickrRecycleViewAdapter(new ArrayList<PhotoData>(),this);
        recyclerView.setAdapter(recycleViewAdapter);

        recyclerView.setOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                GetFlickrJsonData flickrJsonData = new GetFlickrJsonData("en-us",true,MainActivity.this::onDataAvailable);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String queryTags = sharedPreferences.getString(FLICKR_QUERY,"");
                if(queryTags.length()>0){
                    flickrJsonData.execute(queryTags);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: start");
        GetFlickrJsonData flickrJsonData = new GetFlickrJsonData("en-us",true,this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryTags = sharedPreferences.getString(FLICKR_QUERY,"");
        if(queryTags.length()>0){
            flickrJsonData.execute(queryTags);
        }
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(ArrayList<PhotoData> data, DownloadStatus status) {
        if(status==DownloadStatus.OK){

            recycleViewAdapter.loadNewData(data);
        }else {
            Log.e(TAG, "onDataAvailable: download failed with status: "+status);

        }




    }


}

