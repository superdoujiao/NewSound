package com.example.serenade.newsound.find.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 刘瑞忻 on 2017/3/6.
 */

public class NetWorkState {

    public static boolean isNetWorkConnect(Context context) {
        ConnectivityManager manger= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manger.getActiveNetworkInfo();
        if (activeNetworkInfo!=null){
            return  activeNetworkInfo.isConnected();
        }
        return false;
    }
}
