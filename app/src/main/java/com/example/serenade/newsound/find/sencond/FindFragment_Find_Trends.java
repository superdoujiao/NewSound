package com.example.serenade.newsound.find.sencond;

import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import static com.example.serenade.newsound.R.id.find_trends_checkboc;

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
    private View et;
    private LinearLayout linearlayout;
    private LinearLayout layoutsend;
    private CheckBox dianzan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_fragment__find__trends);
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

        head_img = (ImageView) findViewById(R.id.findfragment_find_trends_head_img);
        username = (TextView) findViewById(R.id.findfragment_find_trends_username);
        date = (TextView) findViewById(R.id.findfragment_find_trends_date);
        sex = (ImageView) findViewById(R.id.findfragment_find_trends_sex);
        count = (TextView) findViewById(R.id.find_trends_discuss_count);
        linearlayout = (LinearLayout) findViewById(R.id.findfragment_find_trends_linearlayout);
        layoutsend = (LinearLayout) findViewById(R.id.findfragment_find_trends_linearlayoutsend);
        recycleview = (RecyclerView) findViewById(R.id.findfragment_find_trends_recycleview);
        dianzan = (CheckBox) findViewById(find_trends_checkboc);


        Find.DataBean.ListBean.UserinfoBean userinfo = (Find.DataBean.ListBean.UserinfoBean) getIntent().getExtras().getSerializable("userinfo");
        String inputtime=getIntent().getExtras().getString("update");
        fid=getIntent().getExtras().getString("fid");
        final String hit=getIntent().getExtras().getString("hit");
        String is_good=getIntent().getExtras().getString("is_good");

        dianzan.setText(hit);
        username.setText(userinfo.getUsername());
        Long time=Long.parseLong(inputtime)*1000;
        String format = new SimpleDateFormat("MM-dd",Locale.CHINA).format(new Date(time));
        String[] split1 = format.split("-");
        date.setText(split1[0]+"月"+split1[1]+"日");
        Glide.with(this).load(userinfo.getHead_img()).asBitmap().into(new BitmapImageViewTarget(head_img){
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circular= RoundedBitmapDrawableFactory.create(getResources(),resource);
                circular.setCircular(true);
                head_img.setImageDrawable(circular);
            }
        });

        if (userinfo.getSex().equals("0")){
            sex.setImageBitmap(new BitmapFactory().decodeResource(getResources(),R.drawable.found_boy));
        }
        final TextView hhh= (TextView) findViewById(R.id.find_trends_check);
        if (Integer.valueOf(is_good)==1){
            dianzan.setChecked(true);
            hhh.setText("您已经点过赞了呦");
        }
        dianzan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    hhh.setText("您已经点过赞了呦");
                }else {
                    hhh.setText("喜欢，就赞一个吧");
                }

            }
        });
        final int[] count = {0};
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] ==0){
                    count[0]++;
                }else if (count[0]==1){
                    count[0]--;
                }
                dianzan.setText(count[0]+Integer.valueOf(hit)+"");
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

                //加载底部视图
                adapter.addFooterView();

                adapter.onItemClick(new FindTrendsRecycleViewAdapter.setOnItemClickListener() {
                    @Override
                    public void Onlick(View itemView, String tag) {
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindFragment_Find_Trends.this);
                                builder.setItems(new String[]{"回复评论", "复制评论", "删除评论"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),"hhh",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                builder.show();
                            }
                        });
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
                map.put("fid",fid);
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

            case R.id.findfragment_find_trends_pinglun:
                layoutsend.setVisibility(View.VISIBLE);
                linearlayout.setVisibility(View.GONE);
                break;

            case  R.id.findfragment_find_trends_send:
                linearlayout.setVisibility(View.VISIBLE);
                layoutsend.setVisibility(View.GONE);
                break;

            case R.id.findfragment_find_trends_zhuanfa:
                Intent intent=new Intent(this,Transpond.class);
                startActivity(intent);
                break;
        }
    }
}
