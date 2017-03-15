package com.example.serenade.newsound.find.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.List;

import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by 刘瑞忻 on 2017/3/11.
 */

public class MyLocation extends Application implements LocationListener{
    private String locationProvider;       //位置提供器
    private LocationManager location;

    private void getLocation(Context context) {
//        //1.获取位置管理器
//        location = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        //2.获取位置提供器，GPS或是NetWork
//        List<String> providers = location.getProviders(true);
//        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//            //如果是网络定位
//            locationProvider = LocationManager.NETWORK_PROVIDER;
//        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            //如果是GPS定位
//            locationProvider = LocationManager.GPS_PROVIDER;
//        } else {
//            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        //3.获取上次的位置，一般第一次运行，此值为null
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location locationm = location.getLastKnownLocation(locationProvider);
//        if (location!=null){
//            updateWithNewLocation(locationm);
//        }else{
//            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
//            location.requestLocationUpdates(locationProvider, 0, 0,this);
//        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
