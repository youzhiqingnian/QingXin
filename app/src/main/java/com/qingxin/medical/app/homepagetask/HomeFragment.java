package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 首界面
 */
public class HomeFragment extends VLFragment {

    private View rootVeiw;

    private TextView tv_city;
    private ViewPager vp_viewpager;
    private VLStatedButtonBar buttonBar;
    private RecyclerView rv_strict_famous_doctor_institute,rv_goddess_diary;

    private RecyclerGridViewAdapter mAdapter;

    private HomeGoddessDiaryAdapter goddessDiaryAdapter;

    private RecyclerView.LayoutManager mLayoutManager;


    private List<Integer> viewpagerImageList = new ArrayList<>();

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) {
            return;
        }

        rootVeiw = getView();

        initView();

        initViewPager();

    }

    private void initView() {
        tv_city = (TextView) rootVeiw.findViewById(R.id.tv_city);
        rv_strict_famous_doctor_institute = rootVeiw.findViewById(R.id.rv_strict_famous_doctor_institute);
        rv_goddess_diary = rootVeiw.findViewById(R.id.rv_goddess_diary);

        initRecyclerView();

    }

    private void initRecyclerView() {
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_strict_famous_doctor_institute.setLayoutManager(mLayoutManager);
        rv_strict_famous_doctor_institute.addItemDecoration(new GridSpacingItemDecoration(2, VLUtils.dip2px(10), false));
        mAdapter = new RecyclerGridViewAdapter(getActivity());
        rv_strict_famous_doctor_institute.setAdapter(mAdapter);


        rv_goddess_diary.setLayoutManager(new LinearLayoutManager(getActivity()));
        goddessDiaryAdapter = new HomeGoddessDiaryAdapter(getActivity());
        rv_goddess_diary.setAdapter(goddessDiaryAdapter);
        rv_goddess_diary.setNestedScrollingEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();

    }



    private void initViewPager() {

        vp_viewpager = (ViewPager) rootVeiw.findViewById(R.id.vp_viewpager);
        buttonBar = (VLStatedButtonBar) rootVeiw.findViewById(R.id.buttonBar);

        initFakeData();
        BannerPagerAdapter mAdapter = new BannerPagerAdapter(getActivity(), viewpagerImageList);
        vp_viewpager.setAdapter(mAdapter);

        vp_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                buttonBar.setChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        buttonBar.setStatedButtonBarDelegate(new DotBarDelegate(getActivity(), viewpagerImageList.size()));
        vp_viewpager.setCurrentItem(viewpagerImageList.size() * 1000);
        buttonBar.setChecked(vp_viewpager.getCurrentItem());

    }

    private void initFakeData() {

        viewpagerImageList.add(R.mipmap.image1);
        viewpagerImageList.add(R.mipmap.image2);
        viewpagerImageList.add(R.mipmap.image3);
        viewpagerImageList.add(R.mipmap.image4);
        viewpagerImageList.add(R.mipmap.image5);

    }


    private class BannerPagerAdapter extends PagerAdapter {

        private List<Integer> mHomePageRowCells;
        private Context mContext;

        BannerPagerAdapter(Context context, List<Integer> homePageRowCells) {
            this.mContext = context;
            this.mHomePageRowCells = homePageRowCells;
        }

        @Override
        public int getCount() {
            return mHomePageRowCells.size() <= 1 ? mHomePageRowCells.size() : Short.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager, container, false);
            ImageView iv_viewpager_img = (ImageView) view.findViewById(R.id.iv_viewpager_img);
            iv_viewpager_img.setImageResource(mHomePageRowCells.get(position % mHomePageRowCells.size()));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class DotBarDelegate implements VLStatedButtonBar.VLStatedButtonBarDelegate {
        private Context mContext;
        private int mCount;

        DotBarDelegate(Context context, int count) {
            this.mContext = context;
            this.mCount = count;
        }

        @Override
        public void onStatedButtonBarCreated(VLStatedButtonBar buttonBar) {
            for (int i = 0; i < mCount; i++) {
                VLStatedButtonBar.VLStatedButton button = new VLStatedButtonBar.VLStatedButton(mContext);
                button.setStatedButtonDelegate(new DotButtonDelegate(mContext, R.mipmap.vp_unselected, R.mipmap.vp_selected));
                buttonBar.addStatedButton(button);
            }
        }

        @Override
        public void onStatedButtonBarChanged(VLStatedButtonBar buttonBar, int position) {
        }
    }

    private class DotButtonDelegate implements VLStatedButtonBar.VLStatedButton.VLStatedButtonDelegate {
        private Context mContext;
        private ImageView mDotImageView;
        private int mResIdNormal;
        private int mResIdCheck;

        DotButtonDelegate(Context context, int resIdNormal, int resIdCheck) {
            this.mResIdNormal = resIdNormal;
            this.mResIdCheck = resIdCheck;
            this.mContext = context;
        }

        @Override
        public void onStatedButtonCreated(VLStatedButtonBar.VLStatedButton button, LayoutInflater inflater) {
            mDotImageView = new ImageView(mContext);
            mDotImageView.setLayoutParams(new ViewGroup.LayoutParams(VLUtils.dip2px(12), VLUtils.dip2px(12)));
            mDotImageView.setPadding(7, 5, 7, 5);
            button.addView(mDotImageView);
        }

        @Override
        public void onStatedButtonChanged(VLStatedButtonBar.VLStatedButton button, VLStatedButtonBar.VLStatedButton.VLButtonState buttonState, int userState) {
            if (mDotImageView != null) {
                mDotImageView.setImageResource(buttonState == VLStatedButtonBar.VLStatedButton.VLButtonState.StateNormal ? mResIdNormal : mResIdCheck);
            }
        }
    }

    /**
     * @author
     * @Date 2016年8月29日
     * @describe RecyclerView Item间距
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
