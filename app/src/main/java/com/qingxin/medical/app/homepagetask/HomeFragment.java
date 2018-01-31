package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListActivity;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.app.homepagetask.model.HomeBanner;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.homepagetask.model.HomeProduct;
import com.qingxin.medical.service.entity.Book;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 首界面
 */
public class HomeFragment extends VLFragment implements HomePageTaskContract.View, View.OnClickListener {

    private View rootVeiw;

    private TextView tv_city,
            tv_free_program,
            tv_left_product_price,
            tv_left_product_origin_price,
            tv_right_top_program_title,
            tv_right_top_product_price,
            tv_right_top_product_origin_price,
            tv_right_bottom_program_title,
            tv_right_bottom_product_price,
            tv_right_bottom_product_origin_price;

    private SimpleDraweeView iv_left_product_cover,
            iv_right_top_product_cover,
            iv_right_bottom_product_cover;

    private RelativeLayout rl_more_goddess_diary;

    private VLStatedButtonBar buttonBar;

//    private AtomicInteger mStep;

    public static final int MAX_STEP = 3;

    private HomePageTaskContract.Presenter mPresenter;

    private HomeBanner mBanner;
    private GoddessDiary mDiary;
    private HomeProduct mProduct;
    private HomeBean mHomeBean;

    private HomePageTaskPresenter mHomePageTaskPresenter;

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
        new HomePageTaskPresenter(getActivity(), this);
        if (getView() == null) {
            return;
        }
        rootVeiw = getView();
        initView();
        initListener();

        mPresenter.getHomeData();

    }

    private void initView() {
        tv_city = rootVeiw.findViewById(R.id.tv_city);
        tv_free_program = rootVeiw.findViewById(R.id.tv_free_program);
        tv_left_product_price = rootVeiw.findViewById(R.id.tv_left_product_price);
        tv_left_product_origin_price = rootVeiw.findViewById(R.id.tv_left_product_origin_price);
        tv_right_top_program_title = rootVeiw.findViewById(R.id.tv_right_top_program_title);
        tv_right_top_product_price = rootVeiw.findViewById(R.id.tv_right_top_product_price);
        tv_right_top_product_origin_price = rootVeiw.findViewById(R.id.tv_right_top_product_origin_price);
        tv_right_bottom_program_title = rootVeiw.findViewById(R.id.tv_right_bottom_program_title);
        tv_right_bottom_product_price = rootVeiw.findViewById(R.id.tv_right_bottom_product_price);
        tv_right_bottom_product_origin_price = rootVeiw.findViewById(R.id.tv_right_bottom_product_origin_price);

        iv_left_product_cover = rootVeiw.findViewById(R.id.iv_left_product_cover);
        iv_right_top_product_cover = rootVeiw.findViewById(R.id.iv_right_top_product_cover);
        iv_right_bottom_product_cover = rootVeiw.findViewById(R.id.iv_right_bottom_product_cover);

        rl_more_goddess_diary = rootVeiw.findViewById(R.id.rl_more_goddess_diary);

        buttonBar = rootVeiw.findViewById(R.id.buttonBar);
    }


    private void initListener() {
        rl_more_goddess_diary.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    /**
     * 填充数据
     */
    private void setData() {

        List<HomeBean.ContentBean.BannersBean> bannerList = mHomeBean.getContent().getBanners();

        ViewPager vp_viewpager = rootVeiw.findViewById(R.id.vp_viewpager);
        BannerPagerAdapter mAdapter = new BannerPagerAdapter(getActivity(), bannerList);
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
        buttonBar.setStatedButtonBarDelegate(new DotBarDelegate(getActivity(), bannerList.size()));
        vp_viewpager.setCurrentItem(bannerList.size() * 1000);
        buttonBar.setChecked(vp_viewpager.getCurrentItem());
        
        List<HomeBean.ContentBean.ProductsBean> productList = mHomeBean.getContent().getProducts();
        
        if (productList.size() >= 1) {
            tv_free_program.setText(productList.get(0).getName());
            tv_left_product_price.setText(productList.get(0).getPrice() + getStr(R.string.yuan));
            tv_left_product_origin_price.setText(getStr(R.string.origin_price) + productList.get(0).getOld_price() + getStr(R.string.yuan));
            iv_left_product_cover.setImageURI(Uri.parse(productList.get(0).getCover()));
        }

        if (productList.size() >= 2) {
            tv_right_top_program_title.setText(productList.get(1).getName());
            tv_right_top_product_price.setText(productList.get(1).getPrice() + getStr(R.string.yuan));
            tv_right_top_product_origin_price.setText(getStr(R.string.origin_price) + productList.get(1).getOld_price() + getStr(R.string.yuan));
            iv_right_top_product_cover.setImageURI(Uri.parse(productList.get(1).getCover()));
        }

        if (productList.size() >= 3) {
            tv_right_bottom_program_title.setText(productList.get(2).getName());
            tv_right_bottom_product_price.setText(productList.get(2).getPrice() + getStr(R.string.yuan));
            tv_right_bottom_product_origin_price.setText(getStr(R.string.origin_price) + productList.get(2).getOld_price() + getStr(R.string.yuan));
            iv_right_bottom_product_cover.setImageURI(Uri.parse(productList.get(2).getCover()));
        }


        RecyclerView rv_strict_famous_doctor_institute = rootVeiw.findViewById(R.id.rv_strict_famous_doctor_institute);
        RecyclerView rv_goddess_diary = rootVeiw.findViewById(R.id.rv_goddess_diary);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_strict_famous_doctor_institute.setLayoutManager(mLayoutManager);
        rv_strict_famous_doctor_institute.addItemDecoration(new GridSpacingItemDecoration(2, VLUtils.dip2px(10), false));
        RecyclerGridViewAdapter strictSelctionAdapter = new RecyclerGridViewAdapter(getActivity());
        rv_strict_famous_doctor_institute.setAdapter(strictSelctionAdapter);
        rv_strict_famous_doctor_institute.setNestedScrollingEnabled(false);

        List<HomeBean.ContentBean.DiarysBean> diaryList = mHomeBean.getContent().getDiarys();
        if (diaryList != null && diaryList.size() > 0) {
            rv_goddess_diary.setLayoutManager(new LinearLayoutManager(getActivity()));
            HomeGoddessDiaryAdapter mGoddessDiaryAdapter = new HomeGoddessDiaryAdapter(getActivity(), diaryList);
            rv_goddess_diary.setAdapter(mGoddessDiaryAdapter);
            rv_goddess_diary.setNestedScrollingEnabled(false);
        }


    }

    @Override
    public void setPresenter(HomePageTaskContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.subscribe();
    }

    @Override
    public void onSuccess(Book mBook) {

    }


    @Override
    public void onSuccess(HomeBean homeBean) {
        mHomeBean = homeBean;
        if(mHomeBean != null && mHomeBean.getCode().equals("200")){
            setData();
        }
        
    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_more_goddess_diary:

                Intent intent = new Intent(getActivity(), GoddessDiaryListActivity.class);
                startActivity(intent);

                break;

        }
    }


    private class BannerPagerAdapter extends PagerAdapter {

        private List<HomeBean.ContentBean.BannersBean> mBannerList;
        private Context mContext;

        BannerPagerAdapter(Context context, List<HomeBean.ContentBean.BannersBean> bannerList) {
            this.mContext = context;
            this.mBannerList = bannerList;
        }

        @Override
        public int getCount() {
            return mBannerList.size() <= 1 ? mBannerList.size() : Short.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager, container, false);
            SimpleDraweeView iv_viewpager_img = view.findViewById(R.id.iv_viewpager_img);
            iv_viewpager_img.setImageURI(Uri.parse(mBannerList.get(position % mBannerList.size()).getCover()));
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
                button.setStatedButtonDelegate(new DotButtonDelegate(mContext, R.drawable.banner_unselected_circle_point, R.drawable.banner_selected_point));
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
     * gridveiw item之间的间距
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
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
