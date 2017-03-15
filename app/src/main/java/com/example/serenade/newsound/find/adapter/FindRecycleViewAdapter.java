package com.example.serenade.newsound.find.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.serenade.newsound.MainActivity;
import com.example.serenade.newsound.R;
import com.example.serenade.newsound.find.bean.Find;
import com.example.serenade.newsound.find.bean.PathCogfig;
import com.example.serenade.newsound.find.sencond.Transpond;
import com.example.serenade.newsound.find.utils.MyApp;
import com.example.serenade.newsound.find.view.MyGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.R.attr.format;
import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.serenade.newsound.R.drawable.more;
import static com.example.serenade.newsound.R.drawable.username;

/**
 * Created by 刘瑞忻 on 2017/3/5.
 */

public class FindRecycleViewAdapter extends RecyclerView.Adapter<FindRecycleViewAdapter.ViewHolderItem1> implements View.OnClickListener {

    private Context context;
    private List<Find.DataBean.ListBean> data;
    private String fid;

    private Handler handler;
    private OnfindrvItemClickListner onitemclicklistner = null;

    public FindRecycleViewAdapter(Context context, List<Find.DataBean.ListBean> data,Handler handler) {
        this.context = context;
        this.data = data;
        this.handler=handler;
    }

    @Override
    public ViewHolderItem1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.findfragment_find_item, parent, false);
        ViewHolderItem1 item1 = new ViewHolderItem1(view);

        //注册点击事件
        view.setOnClickListener(this);
        return item1;
    }

    @Override
    public void onBindViewHolder(final ViewHolderItem1 holder, final int position) {
        final Find.DataBean.ListBean listBean = data.get(position);
        String body = listBean.getBody();
        fid=listBean.getId();
        holder.dianzan.setTag(listBean.getId());
        final String hit=listBean.getHit();
        final String userid=listBean.getUid();
        final int[] is_good = {listBean.getIs_good()};
        int follow_result=listBean.getFollow_result();
        final String uid=listBean.getUid();
        holder.forward.setText(listBean.getFollow_count());

        holder.guanzhu.setTag(uid);
        if (holder.guanzhu.getTag().equals(uid)){
            if (follow_result==1){
                holder.guanzhu.setText("已关注");
            }
            if (userid.equals("1481")){
                holder.guanzhu.setVisibility(View.INVISIBLE);
            }else {
                holder.guanzhu.setVisibility(View.VISIBLE);
            }
        }

        //TODO 对转发事件的监听
        //转发的点击事件
        holder.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Transpond.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("list",listBean);
                intent.putExtras(bundle);
                context.startActivity(intent);
                //((MainActivity)context).startActivityForResult(intent,11);
            }
        });

        //TODO 对删除事件的监听
        holder.more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setItems(
                        new String[]{"删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest request1=new StringRequest(Request.Method.POST, PathCogfig.DEL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        handler.sendEmptyMessage(2);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                })
                                {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> map=new HashMap<String, String>();
                                        map.put("id",data.get(position).getId());
                                        return map;
                                    }
                                };
                                MyApp.getRequestQueue().add(request1);
                            }
                        }
                );

                builder.show();
            }
        });


        //TODO 对关注事件的监听
        final int[] cnt = {0};
        holder.guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt[0]==1){
                    cnt[0]--;
                    holder.guanzhu.setText("关注");
                }else if (cnt[0] ==0){
                    cnt[0]++;
                    holder.guanzhu.setText("已关注");
                    StringRequest request=new StringRequest(Request.Method.POST, PathCogfig.GUANZHU, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("user_id","1481");
                            map.put("parent_id",uid);
                            return map;
                        }
                    };
                    MyApp.getRequestQueue().add(request);
                }

            }
        });

        //TODO 对点赞事件的监听
        final int[] count = {0};
        if (is_good[0] ==1){
            if (holder.dianzan.getTag().equals(listBean.getId())){
                holder.dianzan.setChecked(true);
                count[0]=1;
            }
        }else {
            count[0]=0;
        }
        final int[] x = {0};
        holder.dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count[0] == 0) {
                    count[0]++;
                    x[0]++;
                    holder.dianzan.setChecked(true);

                } else if (count[0] == 1) {
                    count[0]--;
                    x[0]--;
                    holder.dianzan.setChecked(false);
                }
                holder.dianzan.setText(x[0]+Integer.valueOf(hit)+"");
                final int finalCount = count[0];
                StringRequest req = new StringRequest(Request.Method.POST, PathCogfig.DIANZAN1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", "1481") ;
                        map.put("fid", holder.dianzan.getTag().toString());
                        map.put("hit_type", String.valueOf(finalCount));
                        return map;
                    }
                };
                MyApp.getRequestQueue().add(req);
            }
        });

        String inputtime = listBean.getInputtime();
        Long time = Long.parseLong(inputtime) * 1000;
        String format = new SimpleDateFormat("MM-dd", Locale.CHINA).format(new Date(time));
        String[] split1 = format.split("-");
        holder.date.setText(split1[0] + "月" + split1[1] + "日");

        final Find.DataBean.ListBean.UserinfoBean userinfo = listBean.getUserinfo();
//TODO 绑定数据
        holder.dianzan.setText(listBean.getHit());
        holder.pinglun.setText(listBean.getDiscuss_count());
        holder.forward.setText(listBean.getFollow_count());
        holder.username.setText(userinfo.getUsername());

        SpannableStringBuilder sb = new SpannableStringBuilder();
        String[] split2 = body.split("//");
        for (int i = 0; i < split2.length; i++) {
            String q = null;
            if (i != 0) {
                q = "//" + split2[i];
            } else {
                q = split2[i];
            }
            SpannableString string = new SpannableString(q);
            if (split2[i].contains("@") && split2[i].contains(":")) {
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
                string.setSpan(fcs, split2[i].indexOf("@") + 2, split2[i].indexOf(":") + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ClickableSpan click = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "hhh", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                };
                string.setSpan(click, split2[i].indexOf("@") + 2, split2[i].indexOf(":") + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            sb.append(string);
        }
        holder.body.setMovementMethod(LinkMovementMethod.getInstance());
        holder.body.setText(sb);

        Glide.with(context).load(userinfo.getHead_img()).asBitmap().into(new BitmapImageViewTarget(holder.iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circlur = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circlur.setCircular(true);
                holder.iv.setImageDrawable(circlur);
            }
        });

        if (userinfo.getSex().equals("0")) {
            holder.sex.setImageBitmap(new BitmapFactory().decodeResource(context.getResources(), R.drawable.found_boy));
        }

        String imgUrl = listBean.getImglist();
        if (imgUrl != "") {
            holder.layout.setVisibility(View.VISIBLE);
            holder.gv.setTag(R.id.imageurl, imgUrl);
            String[] split = imgUrl.split("\\|");

            if (split.length == 1) {
                holder.one.setTag(R.id.imageurl, imgUrl);
                holder.gv.setVisibility(View.GONE);
                holder.one.setVisibility(View.VISIBLE);
//                if (holder.one.getTag(R.id.imageurl) != null && imgUrl == holder.one.getTag(R.id.imageurl)) {
                Glide.with(context).load(imgUrl).into(holder.one);
//                }
            }
            else if(split.length==2){
                holder.one.setVisibility(View.GONE);
                holder.gv.setVisibility(View.VISIBLE);
                holder.gv.setNumColumns(2);
                FindRecycleViewGvAdapter adapter = new FindRecycleViewGvAdapter(split, context);
                holder.gv.setAdapter(adapter);

            }

            //TODO 设置图片
            else if (holder.gv.getTag(R.id.imageurl) != null && imgUrl == holder.gv.getTag(R.id.imageurl)) {
                holder.one.setVisibility(View.GONE);
                holder.gv.setVisibility(View.VISIBLE);
                FindRecycleViewGvAdapter adapter = new FindRecycleViewGvAdapter(split, context);
                holder.gv.setAdapter(adapter);
            }
            // }


        } else {
            holder.layout.setVisibility(View.GONE);
        }
        if (imgUrl != "") {
            holder.layout.setVisibility(View.VISIBLE);
            holder.gv.setTag(R.id.imageurl, imgUrl);
            String[] split = imgUrl.split("\\|");

            if (split.length == 1) {
                holder.one.setTag(R.id.imageurl, imgUrl);
                holder.gv.setVisibility(View.GONE);
                holder.one.setVisibility(View.VISIBLE);
//                if (holder.one.getTag(R.id.imageurl) != null && imgUrl == holder.one.getTag(R.id.imageurl)) {
                Glide.with(context).load(imgUrl).into(holder.one);
//                }
            }
            else if(split.length==2){
                holder.one.setVisibility(View.GONE);
                holder.gv.setVisibility(View.VISIBLE);
                holder.gv.setNumColumns(2);
                FindRecycleViewGvAdapter adapter = new FindRecycleViewGvAdapter(split, context);
                holder.gv.setAdapter(adapter);

            }

            //TODO 设置图片
            else if (holder.gv.getTag(R.id.imageurl) != null && imgUrl == holder.gv.getTag(R.id.imageurl)) {
                holder.one.setVisibility(View.GONE);
                holder.gv.setVisibility(View.VISIBLE);
                FindRecycleViewGvAdapter adapter = new FindRecycleViewGvAdapter(split, context);
                holder.gv.setAdapter(adapter);
            }
            // }


        } else {
            holder.layout.setVisibility(View.GONE);
        }



//        if (imgUrl != "") {
//            holder.layout.setTag(R.id.imageurl, imgUrl);
//            holder.layout.setVisibility(View.VISIBLE);
//
//            String[] split = imgUrl.split("\\|");
//
//            //TODO 设置图片
//            holder.layout.setTag(imgUrl);
//            if (holder.layout.getTag().equals(imgUrl)){
//                for (int i = 0; i < split.length; i++) {
//                    ImageView iv = new ImageView(context);
//
//                    iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//                    if (holder.layout.getTag(R.id.imageurl) != null && imgUrl == holder.layout.getTag(R.id.imageurl)) {
//                        Glide.with(context).load(split[i]).into(iv);
//                        holder.layout.addView(iv);
//                    }
//
//                }
//            }
//        }else {
//            holder.layout.setVisibility(View.GONE);
//        }

        //将数据保存在itemview的tag中，以便点击时获取
        holder.itemView.setTag(position);
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        onitemclicklistner.onItemClick(v, v.getTag().toString());
    }

    public class ViewHolderItem1 extends RecyclerView.ViewHolder {

        ImageView iv, sex,more,one;
        RadioButton dianzan;
        TextView date, body, username, forward, pinglun,guanzhu;
        RelativeLayout layout;
        MyGridView gv;
        public ViewHolderItem1(View itemView) {
            super(itemView);
//TODO 初始化控件
            iv = (ImageView) itemView.findViewById(R.id.findfragment_find_iv);
            sex = (ImageView) itemView.findViewById(R.id.findfragment_find_sex);
            username = (TextView) itemView.findViewById(R.id.findfragment_find_username);
            date = (TextView) itemView.findViewById(R.id.findfragment_find_date);
            body = (TextView) itemView.findViewById(R.id.findfragment_find_body);
            layout = (RelativeLayout) itemView.findViewById(R.id.findfragment_find_imgs);
            dianzan = (RadioButton) itemView.findViewById(R.id.findfragment_find_radioButton);
            forward = (TextView) itemView.findViewById(R.id.findfragment_find_forward);
            pinglun = (TextView) itemView.findViewById(R.id.findfragment_find_commment);
            guanzhu= (TextView) itemView.findViewById(R.id.findfragment_find_guanzhu);
            more= (ImageView) itemView.findViewById(R.id.findfragment_find_more);
            gv = (MyGridView) itemView.findViewById(R.id.findfragment_find_gv);
            one = (ImageView) itemView.findViewById(R.id.findfragment_find_one);
        }
    }

    public void setOnItemClick(OnfindrvItemClickListner listner) {
        this.onitemclicklistner = listner;
    }

    //定义接口
    public interface OnfindrvItemClickListner {
        void onItemClick(View itemView, String data);
    }


}
