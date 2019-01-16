package com.dennis.simplealbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dennis.simplealbum.activity.LoginActivity;
import com.dennis.simplealbum.activity.AlbumActivity;
import com.dennis.simplealbum.web.okhttp.ClientFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private ImageView im;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(1000);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    im.startAnimation(animation);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        im = findViewById(R.id.alpha_cover);

        okCheck();

    }

    private void okCheck() {
        OkHttpClient client = ClientFactory.getInstance(getApplicationContext());

        Request okRequest = new Request.Builder()
                .url("http://192.168.1.4:8080/SimpleAlbum/Kikyo/check")
                .build();

        client.newCall(okRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("login_okhttp", response.code() + result);
                JSONObject result_obj = JSON.parseObject(result);
                boolean isLogin = result_obj.getBooleanValue("login");
                if (!isLogin) {
                    handler.sendEmptyMessage(0);
                } else {
                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }



}
