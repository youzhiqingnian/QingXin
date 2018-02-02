package com.vlee78.android.vl;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VLTitleBar extends FrameLayout {
    private Context mContext;
    private LinearLayout mLeftContainer;
    private LinearLayout mRightContainer;
    private LinearLayout mCenterContainer;

    public interface VLTitleBarListener {
        void onTitleBarClicked(VLTitleBarPos pos, int index);
    }

    public interface VLTitleBarDelegate {
        void onTitleBarInit(VLTitleBar titleBar, Class<?> cls);
    }

    public interface VLOnDoubleClickListener {
        void onDoubleClick(View view);
    }

    VLOnDoubleClickListener mVLOnDoubleClickListener;

    public VLOnDoubleClickListener getVLOnDoubleClickListener() {
        return mVLOnDoubleClickListener;
    }

    long mLastClickTime;
    final int DOUBLE_CLICK_INTERVAL = 1000;

    /**
     * 为了能够效应双击事件，此方法会消费掉单击事件
     */
    public void setVLOnDoubleClickListener(VLOnDoubleClickListener vlOnDoubleClickListener) {
        this.mVLOnDoubleClickListener = vlOnDoubleClickListener;
        if (null != mVLOnDoubleClickListener) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (System.currentTimeMillis() - mLastClickTime < DOUBLE_CLICK_INTERVAL) {
                        mLastClickTime = 0;
                        mVLOnDoubleClickListener.onDoubleClick(v);
                    } else {
                        mLastClickTime = System.currentTimeMillis();
                    }
                }
            });
        } else {
            setOnClickListener(null);
        }
    }

    public enum VLTitleBarPos {
        TitleBarLeft,
        TitleBarCenter,
        TitleBarRight,
    }

    public VLTitleBar(Context context) {
        this(context, null, 0);
    }

    public VLTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mContext = getContext();

        mLeftContainer = new LinearLayout(mContext);
        mLeftContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLeftContainer.setLayoutParams(VLUtils.paramsFrame(VLUtils.WRAP_CONTENT, VLUtils.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mLeftContainer);

        mRightContainer = new LinearLayout(mContext);
        mRightContainer.setOrientation(LinearLayout.HORIZONTAL);
        mRightContainer.setLayoutParams(VLUtils.paramsFrame(VLUtils.WRAP_CONTENT, VLUtils.MATCH_PARENT, Gravity.END | Gravity.CENTER_VERTICAL));
        addView(mRightContainer);

        mCenterContainer = new LinearLayout(mContext);
        mCenterContainer.setOrientation(LinearLayout.HORIZONTAL);
        mCenterContainer.setLayoutParams(VLUtils.paramsFrame(VLUtils.WRAP_CONTENT, VLUtils.MATCH_PARENT, Gravity.CENTER | Gravity.CENTER_VERTICAL));
        addView(mCenterContainer);

    }

    public LinearLayout getContainer(VLTitleBarPos pos) {
        if (pos == VLTitleBarPos.TitleBarLeft) return mLeftContainer;
        else if (pos == VLTitleBarPos.TitleBarCenter) return mCenterContainer;
        else return mRightContainer;
    }

    public void clear(VLTitleBarPos pos) {
        LinearLayout container = getContainer(pos);
        container.removeAllViews();
    }

    public View addText(VLTitleBarPos pos, int leftMargin, int rightMargin, String text, int textSize, int textColor, OnClickListener listener) {
        FrameLayout clickRegion = new FrameLayout(mContext);
        clickRegion.setLayoutParams(VLUtils.paramsLinear(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT, 0, Gravity.CENTER));
        if (listener != null) clickRegion.setOnClickListener(listener);

        TextView textView = new TextView(mContext);
        LayoutParams params = VLUtils.paramsFrame(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT, Gravity.CENTER);
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        textView.setLayoutParams(params);
        textView.setMaxEms(15);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.getPaint().setFakeBoldText(true);// 设置加粗
        clickRegion.addView(textView);

        LinearLayout container = getContainer(pos);
        container.addView(clickRegion);
        return clickRegion;
    }

    public View addIcon(VLTitleBarPos pos, int leftMargin, int rightMargin, int iconResId, int iconWidth, int iconHeight, OnClickListener listener) {
        FrameLayout clickRegion = new FrameLayout(mContext);
        clickRegion.setLayoutParams(VLUtils.paramsLinear(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT, 0, Gravity.CENTER));
        if (listener != null) clickRegion.setOnClickListener(listener);

        ImageView imageView = new ImageView(mContext);
        iconWidth = VLUtils.dip2px(iconWidth);
        iconHeight = VLUtils.dip2px(iconHeight);
        LayoutParams params = VLUtils.paramsFrame(iconWidth, iconHeight, Gravity.CENTER);
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        imageView.setLayoutParams(params);
        imageView.setImageResource(iconResId);
        clickRegion.addView(imageView);

        LinearLayout container = getContainer(pos);
        container.addView(clickRegion);
        return clickRegion;
    }

    public View addIcon(VLTitleBarPos pos, int leftMargin, int rightMargin, int iconResId, int iconWidth, int iconHeight, int splitColor, OnClickListener listener) {
        FrameLayout clickRegion = new FrameLayout(mContext);
        clickRegion.setLayoutParams(VLUtils.paramsLinear(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT, 0, Gravity.CENTER));
        if (listener != null) clickRegion.setOnClickListener(listener);

        ImageView imageView = new ImageView(mContext);
        iconWidth = VLUtils.dip2px(iconWidth);
        iconHeight = VLUtils.dip2px(iconHeight);
        LayoutParams params = VLUtils.paramsFrame(iconWidth, iconHeight, Gravity.CENTER);
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        imageView.setLayoutParams(params);
        imageView.setImageResource(iconResId);
        clickRegion.addView(imageView);
        View view = new View(mContext);
        FrameLayout splitView = new FrameLayout(mContext);
        splitView.setLayoutParams(VLUtils.paramsLinear(14, VLUtils.MATCH_PARENT, 0, Gravity.CENTER));
        LayoutParams splitParams = VLUtils.paramsFrame(10, iconHeight + 10, Gravity.CENTER);
        splitParams.leftMargin = 10;
        view.setLayoutParams(splitParams);
        view.setBackgroundColor(splitColor);
        splitView.addView(view);
        LinearLayout container = getContainer(pos);
        container.addView(clickRegion);
        container.addView(splitView);
        return clickRegion;
    }

    public View addTextIcon(VLTitleBarPos pos, int leftMargin, int rightMargin, String text, int textSize, int textColor, int space, int iconResId, float iconWidthDp, float iconHeightDp, OnClickListener listener) {
        FrameLayout clickRegion = new FrameLayout(mContext);
        clickRegion.setLayoutParams(VLUtils.paramsLinear(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT, 0, Gravity.CENTER));
        if (listener != null) clickRegion.setOnClickListener(listener);

        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutParams params = VLUtils.paramsFrame(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT, Gravity.CENTER);
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        clickRegion.addView(linearLayout);

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(VLUtils.paramsLinear(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);

        ImageView imageView = new ImageView(mContext);
        int iconWidth = VLUtils.dip2px(iconWidthDp);
        int iconHeight = VLUtils.dip2px(iconHeightDp);
        imageView.setLayoutParams(VLUtils.paramsLinear(iconWidth, iconHeight));
        imageView.setImageResource(iconResId);

        linearLayout.addView(textView);
        linearLayout.addView(imageView);
        textView.setPadding(0, 0, space, 0);

        LinearLayout container = getContainer(pos);
        container.addView(clickRegion);
        return clickRegion;
    }

    public View addIconText(VLTitleBarPos pos, int leftMargin, int rightMargin, int iconResId, int iconWidth, int iconHeight, int space, String text, int textSize, int textColor, OnClickListener listener) {
        FrameLayout clickRegion = new FrameLayout(mContext);
        clickRegion.setLayoutParams(VLUtils.paramsLinear(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT, 0, Gravity.CENTER));
        if (listener != null) clickRegion.setOnClickListener(listener);

        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutParams params = VLUtils.paramsFrame(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT, Gravity.CENTER);
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        clickRegion.addView(linearLayout);

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(VLUtils.paramsLinear(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);

        ImageView imageView = new ImageView(mContext);
        iconWidth = VLUtils.dip2px(iconWidth);
        iconHeight = VLUtils.dip2px(iconHeight);
        imageView.setLayoutParams(VLUtils.paramsLinear(iconWidth, iconHeight));
        imageView.setImageResource(iconResId);


        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        textView.setPadding(space, 0, 0, 0);

        LinearLayout container = getContainer(pos);
        container.addView(clickRegion);
        return clickRegion;
    }
}
