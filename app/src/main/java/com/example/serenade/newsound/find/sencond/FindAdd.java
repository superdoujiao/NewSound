package com.example.serenade.newsound.find.sencond;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.bean.PathCogfig;
import com.example.serenade.newsound.find.utils.MyApp;
import com.example.serenade.newsound.find.view.MyTranspond;

import java.util.HashMap;
import java.util.Map;

public class FindAdd extends AppCompatActivity {

    private MyTranspond mt;
    private String body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_add);
        mt = (MyTranspond) findViewById(R.id.findadd_myTranspond);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.findfragment_find_add_back:
                finish();
            break;
            case R.id.findfragment_find_add_send:
                body = mt.getText().toString();
                StringRequest request=new StringRequest(Request.Method.POST, PathCogfig.ADD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("hhh","hhh");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    //fid=&relay_uid=&uid=1481&body=fffgt1&imglist=&sond_name=
                    // &sond_id=&sond_user=&sond_type=&sond_img=&address=&lat=&lng=
                    map.put("fid","");
                    map.put("relay_uid","");
                    map.put("uid","1481");
                    Log.e("body",body);
                    map.put("body",body);
                    map.put("imglist","");
                    map.put("sond_name","");
                    map.put("sond_id","");
                    map.put("sond_user","");
                    map.put("sond_type","");
                    map.put("sond_img","");
                    map.put("address","");
                    map.put("lat","");
                    map.put("lng","");
                    return map;
                }
            };
                MyApp.getRequestQueue().add(request);
                FindAdd.this.setResult(RESULT_OK);
                finish();
                break;

            case R.id.findadd_imageView:
                Intent intent=new Intent(FindAdd.this,PhotoAlbum.class);
                startActivity(intent);
                break;
        }

    }

}
