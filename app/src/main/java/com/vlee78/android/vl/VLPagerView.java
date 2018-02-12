package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class VLPagerView extends RelativeLayout {
    public static final int MINE_REWARD = 0x0010;
    private VLScrollableViewPager mViewPager;
    private VLPageChangeListener mListener;
    private int mAutoScrollMs;
    private boolean mAutoScrolling;

    public VLPagerView(Context context) {
        this(context, null);
    }

    public VLPagerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLPagerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public int getPagesCount() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter == null)
            return 0;
        return adapter.getCount();
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public int getPagePosition() {
        return mViewPager.getCurrentItem();
    }

    public void gotoPage(int index, boolean smoothScroll) {
        VLDebug.Assert(index >= 0 && index < mViewPager.getAdapter().getCount());
        mViewPager.setCurrentItem(index, smoothScroll);
    }

    public void setAutoScroll(int autoScrollMs) {
        mAutoScrollMs = autoScrollMs;
        if (!mAutoScrolling) {
            mAutoScrolling = true;
            startAutoScroll();
        }
    }

    private void startAutoScroll() {
        VLScheduler.instance.schedule(mAutoScrollMs, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                int count = mViewPager.getAdapter().getCount();
                if (canceled || mAutoScrollMs == 0 || count == 0) {
                    mAutoScrolling = false;
                    return;
                }
                int position = mViewPager.getCurrentItem();
                if ((++position) >= count)
                    position = 0;
                mViewPager.setCurrentItem(position, true);
                startAutoScroll();
            }
        });
    }

    public void stopAutoScroll() {
        this.mAutoScrollMs = 0;
    }

    private void init(Context context) {
        mAutoScrollMs = 0;
        mAutoScrolling = false;
        mViewPager = new VLScrollableViewPager(context);
        mViewPager.setManualResId();
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mListener != null)
                    mListener.onPageChanged(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (null != mVLPageScrolled) {
                    mVLPageScrolled.onPageScrolled(arg0, arg1, arg2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }


    public interface VLPageChangeListener {
        void onPageChanged(int position);
    }

    public interface VLPageScrolled {
        void onPageScrolled(int arg0, float arg1, int arg2);
    }

    VLPageScrolled mVLPageScrolled;

    public void setOnPageScrolled(VLPageScrolled vlPageScrolled) {
        mVLPageScrolled = vlPageScrolled;
    }

    public void setPageChangeListener(VLPageChangeListener listener) {
        mListener = listener;
    }

    public void setPages(View[] pages) {
        mViewPager.setAdapter(new VLPagerAdapter(pages));
    }

    public void notifyDataSetChanged() {
        if (null != mViewPager.getAdapter()) {
            mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private static class VLPagerAdapter extends PagerAdapter {
        private View[] mViews;

        VLPagerAdapter(View[] views) {
            mViews = views;
        }

        @Override
        public int getCount() {
            return mViews == null ? 0 : mViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews[position];
            container.addView(view, VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = mViews[position];
            container.removeView(view);
        }
    }

    public void setPages(VLPagerDelegate delegate) {
        mViewPager.setAdapter(new VLPagerDelegateAdapter(delegate));
    }

    private static class VLPagerDelegateAdapter extends PagerAdapter {
        private VLPagerDelegate mDelegate;

        VLPagerDelegateAdapter(VLPagerDelegate delegate) {
            mDelegate = delegate;
        }

        @Override
        public int getCount() {
            return mDelegate.onGetPageCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mDelegate.onCreatePage(container, position);
            view.setLayoutParams(VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            mDelegate.onDestroyPage(container, position, view);
            container.removeView(view);
        }
    }

    public void setFragmentPages(FragmentManager fm, VLFragment[] pages) {
        mViewPager.setAdapter(new VLFragmentPagerAdapter(fm, pages));
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private static class VLFragmentPagerAdapter extends FragmentPagerAdapter {
        private VLFragment[] mPages;

        VLFragmentPagerAdapter(FragmentManager fm, VLFragment[] pages) {
            super(fm);
            mPages = pages;
        }

        @Override
        public Fragment getItem(int position) {
            return mPages[position];
        }

        @Override
        public int getCount() {
            return mPages == null ? 0 : mPages.length;
        }
    }

    public void setFragmentPages(FragmentManager fm, VLPagerDelegate delegate) {
        mViewPager.setAdapter(new VLFragmentPagerDelegateAdapter(fm, delegate));
    }

    @SuppressLint("ValidFragment")
    public static class VLFragmentPage extends VLFragment {
        private int mPosition;
        private ViewGroup mContainer;
        private View mView;
        private VLPagerDelegate mDelegate;

        public VLFragmentPage() {
        }

        public VLFragmentPage(int position, VLPagerDelegate delegate) {
            mPosition = position;
            mDelegate = delegate;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContainer = container;
            mView = mDelegate.onCreatePage(mContainer, mPosition);
            return mView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            mDelegate.onDestroyPage(mContainer, mPosition, mView);
        }
    }

    private static class VLFragmentPagerDelegateAdapter extends FragmentPagerAdapter {
        private VLPagerDelegate mDelegate;

        VLFragmentPagerDelegateAdapter(FragmentManager fm, VLPagerDelegate delegate) {
            super(fm);
            mDelegate = delegate;
        }

        @Override
        public Fragment getItem(int position) {
            return new VLFragmentPage(position, mDelegate);
        }

        @Override
        public int getCount() {
            return mDelegate.onGetPageCount();
        }
    }

    public void setScrollable(boolean scrollable) {
        mViewPager.setScrollable(scrollable);
    }

    public void setType(int type) {
        mViewPager.setType(type);
    }

    public boolean getScrollable() {
        return mViewPager.getScrollable();
    }

    public void setOffscreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    protected static class VLScrollableViewPager extends VLViewPager {
        private static int gViewPagerResId = 0x100;
        private boolean mScrollable = true;
        private int mLastMotionY, mLastMotionX;
        private int mType;

        public void setType(int type) {
            mType = type;
        }

        public void setManualResId() {
            setId(gViewPagerResId++);
        }

        public VLScrollableViewPager(Context context) {
            super(context);
        }

        public VLScrollableViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (mScrollable) {
                if (mType != MINE_REWARD) return super.onInterceptTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastMotionX = (int) event.getX();
                    mLastMotionY = (int) event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (Math.abs(event.getX() - mLastMotionX) > Math.abs(event.getY() - mLastMotionY) && Math.abs(event.getX() - mLastMotionX) > VLUtils.dip2px(8))
                        return true;
                }

                return super.onInterceptTouchEvent(event);
            } else
                return false;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return mScrollable && super.onTouchEvent(event);
        }

        public boolean getScrollable() {
            return mScrollable;
        }

        public void setScrollable(boolean scrollable) {
            mScrollable = scrollable;
        }
    }

    public interface VLPagerDelegate {
        int onGetPageCount();

        View onCreatePage(ViewGroup container, int position);

        void onDestroyPage(ViewGroup container, int position, View view);
    }
}
