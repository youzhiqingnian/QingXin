package com.vlee78.android.vl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by liliang on 2016/11/1.
 */

public class VLProgressBar extends ProgressBar {

    private String mTitle;
    private String mText = "";
    private Paint mPaint;
    private boolean mGlobal;

    public VLProgressBar(Context context) {
        super(context);
        initText();
    }

    public VLProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initText();
    }

    public VLProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VLProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initText();
    }

    @Override
    public void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint != null) {
            String text = mTitle != null ? String.format("%s: %s", mTitle, mText) : this.mText;
            Rect rect = new Rect();
            this.mPaint.getTextBounds(text, 0, text.length(), rect);
            int x = (getWidth() / 2) - rect.centerX();// 让现实的字体处于中心位置;;
            int y = (getHeight() / 2) - rect.centerY();// 让显示的字体处于中心位置;;
            canvas.drawText(text, x, y, this.mPaint);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean isGlobal() {
        return mGlobal;
    }

    public void setGlobal(boolean global) {
        this.mGlobal = global;
    }

    // 初始化，画笔
    private void initText() {
        try {
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);// 设置抗锯齿;
            this.mPaint.setColor(Color.BLUE);
        } catch (Exception e) {
            this.mPaint = null;
            // 某些版本系统异常
            VLDebug.logEx(Thread.currentThread(), e);
        }
    }

    // 设置文字内容
    private void setText(int progress) {
        if (mPaint != null) {
            int i = (int) ((progress * 1.0f / this.getMax()) * 100);
            this.mText = i + "%";
        }
    }
}
