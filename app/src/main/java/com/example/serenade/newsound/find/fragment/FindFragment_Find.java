package com.example.serenade.newsound.find.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.serenade.newsound.find.bean.PathCogfig;
import com.example.serenade.newsound.find.sencond.FindAdd;
import com.example.serenade.newsound.find.sencond.Transpond;
import com.example.serenade.newsound.find.utils.MyApp;
import com.example.serenade.newsound.R;

import com.example.serenade.newsound.find.adapter.FindRecycleViewAdapter;
import com.example.serenade.newsound.find.bean.Find;
import com.example.serenade.newsound.find.sencond.FindFragment_Find_Trends;
import com.example.serenade.newsound.find.utils.NetWorkState;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.start;
import static android.app.Activity.RESULT_OK;

/**
 * Created by 刘瑞忻 on 2017/3/4.
 */

public class FindFragment_Find extends Fragment {

    private SwipeRefreshLayout swiperefresh;
    private RecyclerView recycleview;

    private List<Find.DataBean.ListBean> dataList;
    private FindRecycleViewAdapter adapter;
    private FloatingActionButton floatingbtn;

    private LinearLayoutManager manager;
    private int page = 1;
    private boolean isLoading = false;
    private int requestcode;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swiperefresh.setRefreshing(false);
                    break;
                case 2:
                    dataList.clear();
                    page=1;
                    volleyFindPost();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findframgent_find, container, false);

        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.findfragment_find_swiperefresh);
        recycleview = (RecyclerView) view.findViewById(R.id.findfragment_find_recycleview);
        floatingbtn = (FloatingActionButton) view.findViewById(R.id.findfragment_find_floatingbutton);

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        dataList = new ArrayList<>();
        adapter = new FindRecycleViewAdapter(getActivity(), dataList,handler);
        recycleview.setLayoutManager(manager);
        recycleview.setAdapter(adapter);

        judgeNetWork(view);

        loading();

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindAdd.class);
                requestcode=10;
                startActivityForResult(intent,requestcode);
            }
        });


        return view;
    }

    private void loading() {
        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (manager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    if (isLoading) {
                        return;
                    } else {
                        isLoading = true;
                        volleyFindPost();
                    }
                }

                if (manager.findFirstCompletelyVisibleItemPosition() == 0) {

                    swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            volleyFindPost();
                            handler.sendEmptyMessageDelayed(1, 3000);
                        }
                    });

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    private void judgeNetWork(View view) {
        if (NetWorkState.isNetWorkConnect(getActivity())) {
            volleyFindPost();
            swiperefresh.setVisibility(View.VISIBLE);
            TextView tv = (TextView) view.findViewById(R.id.findfragment_find_tv);
            tv.setVisibility(View.INVISIBLE);
        } else {
            TextView tv = (TextView) view.findViewById(R.id.findfragment_find_tv);
            tv.setVisibility(View.VISIBLE);
            swiperefresh.setVisibility(View.INVISIBLE);
            FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.findfragment_find_floatingbutton);
            button.setVisibility(View.INVISIBLE);
        }

    }

    private void volleyFindPost() {
        final StringRequest request = new StringRequest(Request.Method.POST, PathCogfig.FREND_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                List<Find.DataBean.ListBean> list = new Gson().fromJson(response, Find.class).getData().getList();
                if (list != null && list.size() > 0) {
                    dataList.addAll(list);
                    page++;
                    isLoading = false;
                    adapter.notifyDataSetChanged();
                }

                adapter.setOnItemClick(new FindRecycleViewAdapter.OnfindrvItemClickListner() {
                    @Override
                    public void onItemClick(final View itemView, final String data) {
//
                        //对每一项的点击事件
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), FindFragment_Find_Trends.class);
                                Bundle bundle = new Bundle();
                                String fid = dataList.get((int) v.getTag()).getId();

                                Find.DataBean.ListBean.UserinfoBean userinfo = dataList.get((int) v.getTag()).getUserinfo();
                                bundle.putSerializable("userinfo", userinfo);
                                String time = dataList.get((int) v.getTag()).getUpdatetime();
                                bundle.putString("fid", fid);
                                bundle.putString("update", time);
                                bundle.putString("is_good",dataList.get((int) v.getTag()).getIs_good()+"");
                                bundle.putString("hit",dataList.get((int) v.getTag()).getHit());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cur_page", String.valueOf(page));
                map.put("cur_size", "20");
                map.put("uid", "1481");
                map.put("parent_id", "");
                return map;
            }
        };

        MyApp.getRequestQueue().add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        page=1;
        dataList.clear();
        volleyFindPost();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 10:
                    dataList.clear();
                    page=1;
                    volleyFindPost();
                    break;
            }
        }

    }
}
