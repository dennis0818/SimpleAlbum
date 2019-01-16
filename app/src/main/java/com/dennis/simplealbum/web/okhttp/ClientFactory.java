package com.dennis.simplealbum.web.okhttp;

import android.content.Context;

import com.dennis.simplealbum.web.cookie.CookieManager;

import java.sql.ClientInfoStatus;

import okhttp3.OkHttpClient;

public class ClientFactory {
    private static OkHttpClient client;



    public static OkHttpClient getInstance(Context context) {

        if (client == null) {
            synchronized (ClientFactory.class) {
                if (client == null)
                    client = new OkHttpClient.Builder().cookieJar(new CookieManager(context)).build();
            }
        }
        return client;
    }

}
