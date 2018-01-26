package com.qingxin.medical.widget.indicator;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;

import com.vlee78.android.vl.VLUtils;


/**
 * 带文本的指示器标题
 */
public class SimplePagerTitleView extends android.support.v7.widget.AppCompatTextView implements IMeasurablePagerTitleView {
    protected int mTextSelectedColor;
    protected int mTextNormalColor;
    protected int mNormalBg;
    protected int mSelectedBg;
    protected int mMarginLeft;
    protected int mMarginRight;

    public SimplePagerTitleView(Context context) {
        super(context, null);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        int padding = VLUtils.dip2px(13);
        setPadding(padding, 0, padding, 0);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTextColor(mTextSelectedColor);
        setBackgroundResource(mSelectedBg);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        setTextColor(mTextNormalColor);
        setBackgroundResource(mNormalBg);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    public void setNormalBackground(int normalBg) {
        mNormalBg = normalBg;
    }

    public void setSelectedBackground(int selectedBg) {
        mSelectedBg = selectedBg;
    }

    public void setMaginLeft(int marginLeft) {
        this.mMarginLeft = marginLeft;
    }

    public void setMarginRight(int marginRight) {
        this.mMarginRight = marginRight;
    }

    public int getMarginLeft() {
        return mMarginLeft;
    }

    public int getMarginRight() {
        return mMarginRight;
    }

    public void setTextSelectedColor(int mTextSelectedColor) {
        this.mTextSelectedColor = mTextSelectedColor;
    }

    public void setTextNormalColor(int mTextNormalColor) {
        this.mTextNormalColor = mTextNormalColor;
    }
}
