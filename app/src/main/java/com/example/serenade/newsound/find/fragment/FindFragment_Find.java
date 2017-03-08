package com.example.serenade.newsound.find.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.serenade.newsound.find.utils.MyApp;
import com.example.serenade.newsound.R;

import com.example.serenade.newsound.find.adapter.FindRecycleViewAdapter;
import com.example.serenade.newsound.find.bean.Find;
import com.example.serenade.newsound.find.sencond.FindFragment_Find_Trends;
import com.example.serenade.newsound.find.utils.NetWorkState;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by 刘瑞忻 on 2017/3/4.
 */

public class FindFragment_Find extends Fragment {

    private SwipeRefreshLayout swiperefresh;
    private RecyclerView recycleview;

    private String url="http://api.konglongtech.com/api/webapi/ap_common/get_fruit_frend_list ";

    private List<Find.DataBean.ListBean> dataList;
    private FindRecycleViewAdapter adapter;
    private ImageView more;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.findframgent_find,container,false);

        swiperefresh= (SwipeRefreshLayout) view.findViewById(R.id.findfragment_find_swiperefresh);
        recycleview = (RecyclerView) view.findViewById(R.id.findfragment_find_recycleview);


        judgeNetWork(view);

        return view;
    }

    private void judgeNetWork(View view) {
        if (NetWorkState.isNetWorkConnect(getActivity())){
            volleyFindPost(url);
            swiperefresh.setVisibility(View.VISIBLE);
            TextView tv= (TextView) view.findViewById(R.id.findfragment_find_tv);
            tv.setVisibility(View.INVISIBLE);
        }else {
            TextView tv= (TextView) view.findViewById(R.id.findfragment_find_tv);
            tv.setVisibility(View.VISIBLE);
            swiperefresh.setVisibility(View.INVISIBLE);
            FloatingActionButton button= (FloatingActionButton) view.findViewById(R.id.findfragment_find_floatingbutton);
            button.setVisibility(View.INVISIBLE);
        }

    }

    private void volleyFindPost(String url) {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataList=new ArrayList<>();
                dataList = new Gson().fromJson(response, Find.class).getData().getList();

                recycleview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                adapter=new FindRecycleViewAdapter(getActivity(),dataList);
                recycleview.setAdapter(adapter);

                adapter.setOnItemClick(new FindRecycleViewAdapter.OnfindrvItemClickListner() {
                    @Override
                    public void onItemClick(View itemView, final String data) {
                        ImageView more= (ImageView) itemView.findViewById(R.id.findfragment_find_more);
                        more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setItems(
                                        new String[]{"举报"}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setItems(
                                                        new String[]{"恶意攻击谩骂","营销广告","淫秽色情","政治反动","其他"}, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Toast.makeText(getActivity(),"已举报",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                );
                                                builder.show();
                                            }
                                        }
                                );

                                builder.show();
                            }
                        });
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getActivity(),FindFragment_Find_Trends.class);
                            Bundle bundle = new Bundle();
                            //fid=dataList.get()
                            Find.DataBean.ListBean.UserinfoBean userinfo = dataList.get((int) v.getTag()).getUserinfo();
                            bundle.putSerializable("userinfo", userinfo);
                            String time=dataList.get((int)v.getTag()).getUpdatetime();
                            bundle.putString("update",time);
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
        });
        MyApp.getRequestQueue().add(request);
    }
}
