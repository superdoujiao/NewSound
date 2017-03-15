package com.example.serenade.newsound.find.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.bean.GetUserLenth;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static com.example.serenade.newsound.R.id.findfragment_nearby_item_sex;
import static com.example.serenade.newsound.R.id.findfragment_nearby_item_username;

/**
 * Created by 刘瑞忻 on 2017/3/6.
 */

public class NearbyRecycleViewAdapter extends RecyclerView.Adapter<NearbyRecycleViewAdapter.NearbyItem>{
    private List<GetUserLenth.DataBean.ListBean> data;
    private Context context;

    public NearbyRecycleViewAdapter(List<GetUserLenth.DataBean.ListBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public NearbyItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.findfragment_nearby_item,parent,false);
        NearbyItem item=new NearbyItem(view);
        return item;
    }

    @Override
    public void onBindViewHolder(final NearbyItem holder, int position) {
        GetUserLenth.DataBean.ListBean bean = data.get(position);
        //holder.distance.setText(new BigDecimal(MapUtils.GetDistance(lat,lng))+"");
        double getDistance=bean.getDistance()/(double)1000;
        DecimalFormat df=new DecimalFormat("0.00");
        double parseDouble = Double.parseDouble(df.format(getDistance));
        if (parseDouble!=(double)0.0){
            holder.distance.setText(parseDouble+"km");
        }


        holder.username.setText(bean.getUsername());
        if (bean.getHead_img()!=""){
            Glide.with(context).load(bean.getHead_img()).asBitmap().into(new BitmapImageViewTarget(holder.head_img){
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable corclur= RoundedBitmapDrawableFactory.create(context.getResources(),resource);
                    corclur.setCircular(true);
                    holder.head_img.setImageDrawable(corclur);
                }
            });
        }


        if (bean.getSex().equals("0")){
            Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.found_boy);
            holder.sex.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NearbyItem extends RecyclerView.ViewHolder{
        ImageView head_img,sex;
        TextView distance,username;
        public NearbyItem(View itemView) {
            super(itemView);
            distance = (TextView) itemView.findViewById(R.id.findfragment_nearby_item_distance);
            username = (TextView) itemView.findViewById(R.id.findfragment_nearby_item_username);
            head_img = (ImageView) itemView.findViewById(R.id.findfragment_nearby_item_head_img);
            sex = (ImageView) itemView.findViewById(R.id.findfragment_nearby_item_sex);

        }
    }
}
