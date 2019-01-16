package com.dennis.simplealbum.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dennis.simplealbum.R;
import com.dennis.simplealbum.web.okhttp.ClientFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
    private Button login_button;
    private EditText login_username;
    private EditText login_password;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                String errorMsg = (String) msg.obj;
                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = login_username.getText().toString();
                String password = login_password.getText().toString();
                login(username, password);
            }
        });

    }

    public void initViews() {
        login_button = findViewById(R.id.login_button);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);

    }

    public void login(final String username, String password) {
        //用OKHttp的方法
        Callback okCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                String result = response.body().string();
                Log.i("login_okhttp", result);
                JSONObject result_obj = JSON.parseObject(result);
                boolean isLogin = result_obj.getBooleanValue("login");
                if (isLogin) {
                    Intent intent = new Intent(LoginActivity.this, AlbumActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = result_obj.getString("errorMsg");
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }
        };

        OkHttpClient mOkClient = ClientFactory.getInstance(getApplicationContext());

        FormBody okFormBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request okRequest = new Request.Builder()
                .url("http://192.168.1.4:8080/SimpleAlbum/Kikyo/login")
                .post(okFormBody)
                .build();

        mOkClient.newCall(okRequest).enqueue(okCallback);

    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

}
