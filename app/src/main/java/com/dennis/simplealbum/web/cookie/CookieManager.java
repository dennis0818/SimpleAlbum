package com.dennis.simplealbum.web.cookie;

import android.content.Context;
import android.util.Log;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieManager implements CookieJar {

    private PersistentCookieStore cookieStore;

    public CookieManager(Context context) {
        this.cookieStore = new PersistentCookieStore(context);
    }



    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            Log.i("login_okhttp", "save + " + url.toString());
            Log.i("login_okhttp", "save + " + cookie.name());
            Log.i("login_okhttp", "save + " + cookie.value());
            cookieStore.add(url, cookie);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        for (Cookie cookie : cookies) {
            Log.i("login_okhttp", "load + " + url.toString());
            Log.i("login_okhttp", "load + " + cookie.name());
            Log.i("login_okhttp", "load + " + cookie.value());
        }
        return cookies;
    }
}
