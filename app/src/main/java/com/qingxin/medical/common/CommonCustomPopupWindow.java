package com.qingxin.medical.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

/**
 * 自定义的popupWindow
 */
public class CommonCustomPopupWindow extends PopupWindow {

    public ViewGroup mMenuView;

    @SuppressLint("InflateParams")
    public CommonCustomPopupWindow(Context context, int layoutId, final int layoutboundId, int animStyleId, ColorDrawable dw, OnClickListener onItemsClickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = (ViewGroup) inflater.inflate(layoutId, null);

        //给所有的控件添加点击事件。
        setChildsEnableClick(mMenuView, onItemsClickListener);
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(animStyleId);
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0x80000000);
        // ColorDrawable dw = new ColorDrawable();
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(layoutboundId).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    // 给所有viewGrop的孩子添加点击事件
    public void setChildsEnableClick(ViewGroup v, OnClickListener onItemsClickListener) {
        int childCount = v.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (v.getChildAt(i) instanceof ViewGroup) {
                setChildsEnableClick((ViewGroup) v.getChildAt(i),
                        onItemsClickListener);
            } else {
                v.getChildAt(i).setOnClickListener(onItemsClickListener);
            }
        }
    }
}
