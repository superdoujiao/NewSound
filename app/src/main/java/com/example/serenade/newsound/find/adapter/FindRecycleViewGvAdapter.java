package com.example.serenade.newsound.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.serenade.newsound.R;

/**
 * Created by 刘瑞忻 on 2017/3/14.
 */

public class FindRecycleViewGvAdapter extends BaseAdapter {
    String imgUrls[];
    Context context;

    public FindRecycleViewGvAdapter(String[] imgUrls, Context context) {
        this.imgUrls = imgUrls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgUrls.length;
    }

    @Override
    public Object getItem(int i) {
        return imgUrls[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.find_recycleview_gv_item,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(context).load(imgUrls[i]).into(holder.iv);
        return view;
    }
    class ViewHolder{
        ImageView iv;
        public ViewHolder(View view){
            iv = (ImageView) view.findViewById(R.id.find_recycle_gv_item_iv);
        }
    }

}
