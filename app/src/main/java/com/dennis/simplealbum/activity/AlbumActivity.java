package com.dennis.simplealbum.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dennis.simplealbum.R;
import com.dennis.simplealbum.adapter.MyListAdapter;
import com.dennis.simplealbum.po.entryVo;
import com.dennis.simplealbum.po.transferData;
import com.dennis.simplealbum.web.okhttp.ClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlbumActivity extends Activity {
    private static final int LIMIT_OFFSET = 10;

    private ListView listView;
    private View footerView;

    private List<entryVo> dataList;
    private List<entryVo> dataListAppend;

    private int entry_MAXCount;
    private int lastVisibleItem;

    private LayoutInflater inflater;

    private MyListAdapter mListAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mListAdapter = new MyListAdapter(AlbumActivity.this, dataList);
                    listView.setAdapter(mListAdapter);
                    //添加footerView竟然会下标越界！！
                    listView.addFooterView(footerView);
                    listView.setOnScrollListener(new MyScrollListener());
                    break;
                case 1:
                    mListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        listView = findViewById(R.id.main_listview);
        inflater = LayoutInflater.from(this);
        initView();
    }

    private void initView() {
        footerView = inflater.inflate(R.layout.album_bottom_loading_footer, null);
    }

    private class MyScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.i("outofindex", "onscrolling: " + lastVisibleItem + "---" + mListAdapter.getCount());
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastVisibleItem == mListAdapter.getCount()) {
                Log.i("outofindex", "lastVisibleItem : " + lastVisibleItem);
                loadMoreDatas();

                if (lastVisibleItem == entry_MAXCount)
                    listView.removeFooterView(footerView);
                Log.i("outofindex", "removefooter");
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
            Log.i("outofindex", "firstItem = " + firstVisibleItem);
            Log.i("outofindex", "visibleItemCount = " + visibleItemCount);

        }
    }

    private void loadMoreDatas() {
        Log.i("outofindex", "loadmore");
        int limitStart = dataList.size();
        read(limitStart, LIMIT_OFFSET);
    }

    private void initData() {
        Log.i("outofindex", "on initData....");
        new Thread() {
            @Override
            public void run() {
                String url = "http://192.168.1.4:8080/SimpleAlbum/Inuyasha/ShowMessages.action?limitStart=0&limitOffset=" + LIMIT_OFFSET;
                OkHttpClient client = ClientFactory.getInstance(getApplicationContext());
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    transferData data = JSON.parseObject(result, transferData.class);
                    dataList = data.getDataList();
                    entry_MAXCount = data.getCount();
                    Log.i("outofindex", "entry_MAXCount : " + entry_MAXCount);
                    Log.i("outofindex", "entryList_size : " + dataList.size());
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void read(final int limitStart, final int limitOffset) {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = ClientFactory.getInstance(getApplicationContext());
                String url = "http://192.168.1.4:8080/SimpleAlbum/Inuyasha/ShowMessages.action?limitStart=" + limitStart + "&limitOffset=" + limitOffset;
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    transferData data = JSON.parseObject(result, transferData.class);
                    if (data != null) {
                        dataListAppend = data.getDataList();
                        dataList.addAll(dataListAppend);
                        entry_MAXCount = data.getCount();
                        handler.sendEmptyMessage(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void toPreview(View v) {
        Intent intent = new Intent();
        intent.setClassName(this, "com.dennis.simplealbum.activity.EntryPreviewer");
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("scope", "onStart...");
        initData();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Process.killProcess(Process.myPid());
            //System.exit(0);
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(getPackageName());

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    */

}
