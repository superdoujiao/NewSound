package com.example.serenade.newsound.find.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.bean.Find;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.R.attr.format;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by 刘瑞忻 on 2017/3/5.
 */

public class FindRecycleViewAdapter extends RecyclerView.Adapter<FindRecycleViewAdapter.ViewHolderItem1> implements View.OnClickListener{

    private Context context;
    private List<Find.DataBean.ListBean> data;

    private OnfindrvItemClickListner onitemclicklistner=null;

    public FindRecycleViewAdapter(Context context, List<Find.DataBean.ListBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolderItem1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.findfragment_find_item,parent,false);
        ViewHolderItem1 item1=new ViewHolderItem1(view);

        //注册点击事件
        view.setOnClickListener(this);
        return item1;
    }

    @Override
    public void onBindViewHolder(ViewHolderItem1 holder, int position) {
        Find.DataBean.ListBean listBean = data.get(position);
        holder.body.setText(listBean.getBody());
        String inputtime = listBean.getInputtime();


        Long time=Long.parseLong(inputtime)*1000;
        String format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.CHINA).format(new Date((long) (time) * 1000));
        holder.date.setText(format+"");

        Find.DataBean.ListBean.UserinfoBean userinfo = listBean.getUserinfo();
        holder.username.setText(userinfo.getUsername());
        Glide.with(context).load(userinfo.getHead_img()).into(holder.iv);
        if (userinfo.getSex().equals("0")){
            holder.sex.setImageBitmap(new BitmapFactory().decodeResource(context.getResources(),R.drawable.found_boy));
        }

        String imgUrl=listBean.getImglist();

        if (imgUrl!=""){
            holder.layout.setTag(R.id.imageurl,imgUrl);
            holder.layout.setVisibility(View.VISIBLE);

            String[] split = imgUrl.split("\\|");

            for (int i=0;i<split.length;i++){
                ImageView iv=new ImageView(context);

                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                if (holder.layout.getTag(R.id.imageurl)!=null && imgUrl==holder.layout.getTag(R.id.imageurl)){
                    Glide.with(context).load(split[i]).into(iv);
                    holder.layout.addView(iv);
                }

            }

        }




        //将数据保存在itemview的tag中，以便点击时获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        onitemclicklistner.onItemClick(v,v.getTag().toString());

    }



    public class ViewHolderItem1 extends RecyclerView.ViewHolder {

        ImageView iv,sex;
        TextView username;
        TextView date,body;
        LinearLayout layout;

        public ViewHolderItem1(View itemView) {
            super(itemView);

            iv = (ImageView) itemView.findViewById(R.id.findfragment_find_iv);
            sex = (ImageView) itemView.findViewById(R.id.findfragment_find_sex);
            username = (TextView) itemView.findViewById(R.id.findfragment_find_username);
            date = (TextView) itemView.findViewById(R.id.findfragment_find_date);
            body = (TextView) itemView.findViewById(R.id.findfragment_find_body);
            layout= (LinearLayout) itemView.findViewById(R.id.findfragment_find_imgs);
        }
    }

    public void setOnItemClick(OnfindrvItemClickListner listner){
        this.onitemclicklistner=listner;
    }
    //定义接口
    public interface OnfindrvItemClickListner{
        void onItemClick(View itemView,String data);
    }

}
