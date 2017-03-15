package com.example.serenade.newsound.find.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.bean.FindTrendsDiscuss;
import com.example.serenade.newsound.find.sencond.FindFragment_Find_Trends;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.resource;
import static android.R.attr.tag;


/**
 * Created by 刘瑞忻 on 2017/3/7.
 */

public class FindTrendsRecycleViewAdapter extends RecyclerView.Adapter<FindTrendsRecycleViewAdapter.FindTrendsViewHolder> implements View.OnClickListener{

    private Context context;
    private List<FindTrendsDiscuss.DataBean.ListBean> data;
    private setOnItemClickListener onItemClick=null;

    private static final int TYPE_NORMAL=0,TYPE_FOOTER=1;
    private int footerview=0;

    public FindTrendsRecycleViewAdapter(Context context, List<FindTrendsDiscuss.DataBean.ListBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {

        if (footerview==1 && position==getItemCount()-1){
            return  TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public FindTrendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case TYPE_FOOTER:
                view= LayoutInflater.from(context).inflate(R.layout.find_trends_footer,parent,false);
                break;

            case  TYPE_NORMAL:
                view= LayoutInflater.from(context).inflate(R.layout.findfragment_find_trends_discuss_item,parent,false);
                break;
        }
        FindTrendsViewHolder holder=new FindTrendsViewHolder(view);

        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FindTrendsViewHolder holder, int position) {

        if (position!=getItemCount()-footerview){
            holder.itemView.setTag(position);

            FindTrendsDiscuss.DataBean.ListBean listBean = data.get(position);

            String uptime=listBean.getUpdatetime();

            FindTrendsDiscuss.DataBean.ListBean.UserinfoBean userinfo = listBean.getUserinfo();

            holder.username.setText(userinfo.getUsername());
            holder.body.setText(listBean.getDiscuss());
            holder.rb.setText(listBean.getHit());

            Long time=Long.parseLong(uptime)*1000;
            String format = new SimpleDateFormat("MM-dd", Locale.CHINA).format(new Date(time));
            String[] split1 = format.split("-");
            holder.date.setText(split1[0]+"月"+split1[1]+"日");
            Glide.with(context).load(userinfo.getHead_img()).asBitmap().into(new BitmapImageViewTarget(holder.head_img){
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circular= RoundedBitmapDrawableFactory.create(context.getResources(),resource);
                    circular.setCircular(true);
                    holder.head_img.setImageDrawable(circular);
                }
            });

        }

    }

    public void addFooterView(){
        footerview=1;
    }

    @Override
    public int getItemCount() {

        return data.size()+footerview;
    }

    @Override
    public void onClick(View v) {
        onItemClick.Onlick(v,v.getTag().toString());
    }

    public class FindTrendsViewHolder extends RecyclerView.ViewHolder {

        ImageView head_img;
        TextView username,date,body;
        RadioButton rb;

        public FindTrendsViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.find_trends_discuss_username);
            head_img=(ImageView) itemView.findViewById(R.id.find_trends_discuss_headimg);
            date= (TextView) itemView.findViewById(R.id.find_trends_discuss_date);
            rb= (RadioButton) itemView.findViewById(R.id.find_trends_discuss_dianzan);
            body= (TextView) itemView.findViewById(R.id.find_trends_discuss_body);

        }
    }

    public void onItemClick(setOnItemClickListener listenner){
        this.onItemClick=listenner;
    }
    public interface setOnItemClickListener{
        void Onlick(View itemView,String tag);
    }
}
