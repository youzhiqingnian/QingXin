package com.vlee78.android.vl;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VLToast {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private View toastView;
    private Context mContext;
    private Handler mHandler;
    private String mToastContent = "";
    private int duration = 10;
    private int animStyleId = android.R.style.Animation_Toast;

    private final Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            removeView();
        }
    };

    private VLToast(Context context) {
        this.mContext = VLApplication.instance();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        init();
    }

    private void init() {
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.alpha = 1.0f;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowParams.setTitle("VLToast");
        mWindowParams.packageName = mContext.getPackageName();
        mWindowParams.windowAnimations = animStyleId;
        mWindowParams.y = mContext.getResources().getDisplayMetrics().widthPixels / 5;
    }

    @SuppressWarnings("deprecation")
    private View getDefaultToastView() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout content = new LinearLayout(mContext);
        float[] outerR = new float[]{VLUtils.dip2px(16), VLUtils.dip2px(16), VLUtils.dip2px(16), VLUtils.dip2px(16), VLUtils.dip2px(16), VLUtils.dip2px(16), VLUtils.dip2px(16), VLUtils.dip2px(16)};
        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        //指定填充颜色
        drawable.getPaint().setColor(0x88000000);
        // 指定填充模式
        drawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        if (Build.VERSION.SDK_INT > 16) {
            content.setBackground(drawable);
        } else {
            content.setBackgroundDrawable(drawable);
        }
        TextView view = new TextView(mContext);
        view.setMinWidth(VLUtils.dip2px(20));
        view.setText(mToastContent);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(14);
        view.setTextColor(0xffffffff);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(VLUtils.dip2px(8), VLUtils.dip2px(4), VLUtils.dip2px(8), VLUtils.dip2px(4));
        content.addView(view, layoutParams);
        linearLayout.addView(content, LinearLayout.LayoutParams.WRAP_CONTENT, VLUtils.dip2px(28));
        return linearLayout;
    }

    private static VLToast prevToast;

    public void show() {
        if (prevToast != null) {
            prevToast.destoryView();
        }
        removeView();
        if (toastView == null) {
            toastView = getDefaultToastView();
        }
        mWindowParams.gravity = GravityCompat.getAbsoluteGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, ViewCompat.getLayoutDirection(toastView));
        mWindowManager.addView(toastView, mWindowParams);
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(timerRunnable, duration);
        prevToast = this;
    }

    public void show(int gravity) {
        if (prevToast != null) {
            prevToast.destoryView();
        }
        if (toastView == null) {
            toastView = getDefaultToastView();
        }
        mWindowParams.gravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(toastView));
        removeView();
        mWindowManager.addView(toastView, mWindowParams);
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(timerRunnable, duration);
        prevToast = this;
    }

    public synchronized void removeView() {
        if (toastView != null && toastView.getParent() != null) {
            mWindowManager.removeView(toastView);
            toastView = null;
        }
        if (null != mHandler && null != timerRunnable) {
            mHandler.removeCallbacks(timerRunnable);
        }
    }

    private synchronized void destoryView() {
        try {
            VLDebug.logD("destoryView 取消动画");
            mWindowParams.windowAnimations = -1;
            toastView.setLayoutParams(mWindowParams);
            if (null != toastView) {
                mWindowManager.removeView(toastView);
                toastView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VLToast makeText(Context context, String content, int duration) {
        VLToast helper = new VLToast(context);
        helper.setDuration(duration);
        helper.setContent(content);
        return helper;
    }

    public static VLToast makeText(Context context, String content) {
        VLToast helper = new VLToast(context);
        helper.setDuration(1000);
        helper.setContent(content);
        return helper;
    }

    public static VLToast makeText(Context context, View view, int duration) {
        VLToast helper = new VLToast(context);
        helper.setDuration(duration);
        helper.setView(view);
        return helper;
    }

    public static VLToast makeText(Context context, int strId, int duration) {
        VLToast helper = new VLToast(context);
        helper.setDuration(duration);
        helper.setContent(context.getString(strId));
        return helper;
    }

    public VLToast setContent(String content) {
        this.mToastContent = content;
        return this;
    }

    public VLToast setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public VLToast setAnimation(int animStyleId) {
        this.animStyleId = animStyleId;
        mWindowParams.windowAnimations = this.animStyleId;
        return this;
    }

    public VLToast setView(View view) {
        this.toastView = view;
        return this;
    }
}
