package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class VLViewPager extends ViewPager {
    private ViewGroup parent;

    public VLViewPager(Context context) {
        super(context);
    }

    public VLViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNestedpParent(ViewGroup parent) {
        this.parent = parent;
        if (null == parent) this.parent = (ViewGroup) getParent();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(arg0);
    }

    /**
     * 设置viewPager滚动速度
     */
    public void setScrollDuration(int duration) {
        initViewPagerScroll(duration);
    }

    private void initViewPagerScroll(int duration) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(getContext(), duration);
            mScroller.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 2000;// 滑动速度

        public ViewPagerScroller(Context context, int duration) {
            super(context);
            this.mScrollDuration = duration;
        }

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        public int getScrollDuration() {
            return mScrollDuration;
        }

        public void setScrollDuration(int scrollDuration) {
            this.mScrollDuration = scrollDuration;
        }
    }
}
