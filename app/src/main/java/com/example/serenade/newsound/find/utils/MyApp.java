package com.example.serenade.newsound.find.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 刘瑞忻 on 2017/3/6.
 */

public class MyApp extends Application {

    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }

}
