package com.example.serenade.newsound.find.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.serenade.newsound.find.utils.MyApp;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.adapter.NearbyRecycleViewAdapter;
import com.example.serenade.newsound.find.bean.GetUserLenth;
import com.example.serenade.newsound.find.utils.NetWorkState;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘瑞忻 on 2017/3/5.
 */

public class FindFragment_Nearby extends Fragment {

    private RecyclerView recycleview;

    private NearbyRecycleViewAdapter adapter;

    private List<String>  data=new ArrayList<>();
    private String url="http://api.konglongtech.com/api/webapi/ap_common/get_user_lenth";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private SwipeRefreshLayout swiperefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.findfragment_nearby,container,false);

        recycleview = (RecyclerView) view.findViewById(R.id.findfragment_nearby_recycleview);
        swiperefresh = (SwipeRefreshLayout)view.findViewById(R.id.findfragment_nearby_swiperefresh);

        swiperefresh.setColorSchemeResources(R.color.orange);

        judgeNetWork(view);


        return view;
    }

    private void judgeNetWork(View view) {

        if (NetWorkState.isNetWorkConnect(getActivity())){

            volleyNearbyPost(url);
            swiperefresh.setVisibility(View.VISIBLE);
            TextView tv= (TextView) view.findViewById(R.id.findfragment_nearby_tv);
            tv.setVisibility(View.INVISIBLE);
        }else {
            TextView tv= (TextView) view.findViewById(R.id.findfragment_nearby_tv);
            tv.setVisibility(View.VISIBLE);
            swiperefresh.setVisibility(View.INVISIBLE);
        }
    }


    private void volleyNearbyPost(String url) {

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<GetUserLenth.DataBean.ListBean> data = new Gson().fromJson(response, GetUserLenth.class).getData().getList();

                adapter=new NearbyRecycleViewAdapter(data,getActivity());
                recycleview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                recycleview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"网络连接有误",Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("cur_page","1");
                map.put("cur_size","20");
                map.put("lat","40.04344");
                map.put("lng","116.376614");
                return map;
            }
        };

        MyApp.getRequestQueue().add(request);
    }



}
