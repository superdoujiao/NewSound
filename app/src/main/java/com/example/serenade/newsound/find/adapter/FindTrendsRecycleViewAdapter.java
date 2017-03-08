package com.example.serenade.newsound.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.bean.FindTrendsDiscuss;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.tag;


/**
 * Created by 刘瑞忻 on 2017/3/7.
 */

public class FindTrendsRecycleViewAdapter extends RecyclerView.Adapter<FindTrendsRecycleViewAdapter.FindTrendsViewHolder> implements View.OnClickListener{

    private Context context;
    private List<FindTrendsDiscuss.DataBean.ListBean> data;
    private setOnItemClickListener onItemClick=null;
    private String tag;

    public FindTrendsRecycleViewAdapter(Context context, List<FindTrendsDiscuss.DataBean.ListBean> data) {
        this.context = context;
        this.data = data;
    }



    @Override
    public FindTrendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.findfragment_find_trends_discuss_item,parent,false);
        FindTrendsViewHolder holder=new FindTrendsViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(FindTrendsViewHolder holder, int position) {
        holder.itemView.setTag(position);

        FindTrendsDiscuss.DataBean.ListBean listBean = data.get(position);

        String time=listBean.getUpdatetime();

        FindTrendsDiscuss.DataBean.ListBean.UserinfoBean userinfo = listBean.getUserinfo();

        holder.username.setText(userinfo.getUsername());
        holder.rb.setText(listBean.getHit());
        holder.date.setText(new SimpleDateFormat("yyyy-MM--dd").format(new Date(Long.parseLong(time)*1000)));
        Glide.with(context).load(userinfo.getHead_img()).into(holder.head_img);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        onItemClick.Onlick(v,(String)v.getTag());
    }

    public class FindTrendsViewHolder extends RecyclerView.ViewHolder {

        ImageView head_img;
        TextView username,date;
        RadioButton rb;

        public FindTrendsViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.find_trends_discuss_username);
            head_img=(ImageView) itemView.findViewById(R.id.find_trends_discuss_headimg);
            date= (TextView) itemView.findViewById(R.id.find_trends_discuss_date);
            rb= (RadioButton) itemView.findViewById(R.id.find_trends_discuss_dianzan);

        }
    }

    public void onItemClick(setOnItemClickListener listenner){
        this.onItemClick=listenner;
    }
    public interface setOnItemClickListener{
        void Onlick(View itemView,String tag);
    }
}
