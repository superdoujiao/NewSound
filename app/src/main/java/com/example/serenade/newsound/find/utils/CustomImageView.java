package com.example.serenade.newsound.find.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.Glide.with;
import static java.security.AccessController.getContext;

/**
 * Created by 刘瑞忻 on 2017/3/7.
 */

public class CustomImageView extends ImageView {
    private String url;
    private boolean isAttachedToWindow;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context) {
        super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable=getDrawable();
                if(drawable!=null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp=getDrawable();
                if(drawableUp!=null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        //Picasso.with(getContext()).cancelRequest(this);
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }


    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
                Glide .with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
            }
        }
    }
}
