package com.qingxin.medical;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Date 2018/3/30
 *
 * @author zhikuo
 */
public class NetErrorView extends FrameLayout {
    public NetErrorView(@NonNull Context context) {
        this(context, null);
    }

    public NetErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_net_error, null);
        addView(view);
    }
}
