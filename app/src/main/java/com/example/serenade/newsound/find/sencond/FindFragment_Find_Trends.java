package com.example.serenade.newsound.find.sencond;

import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.adapter.FindRecycleViewAdapter;
import com.example.serenade.newsound.find.adapter.FindTrendsRecycleViewAdapter;
import com.example.serenade.newsound.find.bean.Find;
import com.example.serenade.newsound.find.bean.FindTrendsDiscuss;
import com.example.serenade.newsound.find.utils.MyApp;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.data;

public class FindFragment_Find_Trends extends AppCompatActivity {


    private ImageView head_img;
    private TextView username;
    private TextView date;
    private ImageView sex;

    private String url="http://api.konglongtech.com/api/webapi/ap_common/get_friend_discuss_list";
    private List<FindTrendsDiscuss.DataBean.ListBean> dataList;
    private String uid,fid;
    private RecyclerView recycleview;

    private FindTrendsRecycleViewAdapter adapter;
    private TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_fragment__find__trends);

        head_img = (ImageView) findViewById(R.id.findfragment_find_trends_head_img);
        username = (TextView) findViewById(R.id.findfragment_find_trends_username);
        date = (TextView) findViewById(R.id.findfragment_find_trends_date);
        sex = (ImageView) findViewById(R.id.findfragment_find_trends_sex);
        count = (TextView) findViewById(R.id.find_trends_discuss_count);
        recycleview = (RecyclerView) findViewById(R.id.findfragment_find_trends_recycleview);

        View footer= LayoutInflater.from(this).inflate(R.layout.find_trends_footer,null);

        Find.DataBean.ListBean.UserinfoBean userinfo = (Find.DataBean.ListBean.UserinfoBean) getIntent().getExtras().getSerializable("userinfo");
        String time=getIntent().getExtras().getString("update");

        Glide.with(this).load(userinfo.getHead_img()).into(head_img);
        username.setText(userinfo.getUsername());
        String format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CHINA).format(new Date(Long.parseLong(time) * 1000));
        date.setText(format);
        Glide.with(this).load(userinfo.getHead_img()).into(head_img);
        if (userinfo.getSex().equals("0")){
            sex.setImageBitmap(new BitmapFactory().decodeResource(getResources(),R.drawable.found_boy));
        }
        CheckBox cb= (CheckBox)findViewById(R.id.find_trends_checkboc);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView hhh= (TextView) findViewById(R.id.find_trends_check);
                if (isChecked){
                    hhh.setText("您已经点过赞了呦");
                }else {
                    hhh.setText("喜欢，就赞一个吧");
                }

            }
        });

        uid = userinfo.getId();
        volleyFindTrends(userinfo,uid);

    }

    private void volleyFindTrends(Find.DataBean.ListBean.UserinfoBean userinfo, final String uid){
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataList=new ArrayList<>();
                dataList = new Gson().fromJson(response, FindTrendsDiscuss.class).getData().getList();
                adapter=new FindTrendsRecycleViewAdapter(getApplicationContext(),dataList);
                count.setText("最新评论("+dataList.size()+")");
                recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                recycleview.setAdapter(adapter);

                adapter.onItemClick(new FindTrendsRecycleViewAdapter.setOnItemClickListener() {
                    @Override
                    public void Onlick(View itemView, String tag) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("cur_page","1");
                map.put("cur_size","20");
                map.put("fid","33");
                map.put("uid",uid);
                return map;
            }
        };

        MyApp.getRequestQueue().add(request);
    }

    public  void  click(View view){
        switch (view.getId()){
            case R.id.findfragment_find_trends_back:
                finish();
                break;

            case R.id.findfragment_find_trends_more:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setItems(new String[]{"举报"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                break;
        }
    }
}
