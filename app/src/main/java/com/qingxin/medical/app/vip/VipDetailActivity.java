package com.qingxin.medical.app.vip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.login.LoginActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.fresco.zoomable.ZoomableDraweeView;
import com.qingxin.medical.widget.indicator.view.ShareDialog;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 歆人专享详情
 * Date 2018-02-06
 *
 * @author zhikuo1
 */
public class VipDetailActivity extends QingXinActivity implements VipDetailContract.View, View.OnClickListener, ShareDialog.OnShareDialogListener {

    private CoordinatorLayout mWholeLayoutCdl;
    private AppBarLayout mAppBarLayout;
    private NestedScrollView mScrollSv;
    private ImageView mTopReturnIv,
            mTopShareIv,
            mScrollTopIv;
    private VLPagerView mViewpagerVp;
    private VLStatedButtonBar mStatedBtnBar;
    private TextView mTopTitleNameTv,
            mProductNameTv,
            mPriceTv,
            mGrayRmbIconTv,
            mCityNameTv,
            mOrderCountTv,
            mHospitalNameTv,
            mHospitalCityNameTv,
            mProductDetailTv,
            mCollectTabTv,
            mOrderNowTv;
    private RelativeLayout mCollectRl;
    private SimpleDraweeView mHospitalCoverSdv;
    private ZoomableDraweeView mVipDetailImgZdv;

    private List<Integer> bannerList = new ArrayList<>();

    private ShareDialog mShareDialog;

    private VipDetailContract.Presenter mPresenter;

    public static final String BOOK_NUM = "BOOK_NUM";

    public static void startSelf(VLActivity activity, String vipId, VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, VipDetailActivity.class);
        intent.putExtra(VIP_ID, vipId);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, VIP_DETAIL_REQUEST_CODE);
    }

    public static final String VIP_ID = "VIP_ID";

    private String id = "";

    public static final int VIP_DETAIL_REQUEST_CODE = 6; // 跳到歆人专享详情里的请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_detail);
        mPresenter = new VipDetailPresenter(this);
        dealIntent();
        initView();
        initListener();
        initViewPager();

        if (!TextUtils.isEmpty(id)) {
            mPresenter.getVipDetail(id);
        }
    }

    private void dealIntent() {
        if (getIntent() != null) {
            id = getIntent().getStringExtra(VIP_ID);
        }
    }

    private void initViewPager() {
        bannerList.add(R.mipmap.fake1);
        bannerList.add(R.mipmap.fake2);
        bannerList.add(R.mipmap.fake3);
        BannerPagerAdapter adapter = new BannerPagerAdapter(this, bannerList);
        mViewpagerVp.getViewPager().setAdapter(adapter);
        mViewpagerVp.setPageChangeListener(position -> mStatedBtnBar.setChecked(position));
        mStatedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(this, bannerList.size()));
        mViewpagerVp.setCurrentItem(bannerList.size() * 1000);
        mStatedBtnBar.setChecked(mViewpagerVp.getCurrentItem());
        mViewpagerVp.setAutoScroll(3000);
    }

    private void initListener() {
        mTopReturnIv.setOnClickListener(this);
        mTopShareIv.setOnClickListener(this);
        mScrollTopIv.setOnClickListener(this);
        mCollectRl.setOnClickListener(this);
        mOrderNowTv.setOnClickListener(this);
        mShareDialog.setOnShareDialogListener(this);
        RelativeLayout mTitleBarRl = findViewById(R.id.titleBarRl);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());

                //第一种
                int toolbarHeight = appBarLayout.getTotalScrollRange();

                int dy = Math.abs(verticalOffset);


                if (dy <= toolbarHeight) {
                    float scale = (float) dy / toolbarHeight;
                    float alpha = scale * 255;
                    mTitleBarRl.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    mTopTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));

//                    mTextView.setText("setBackgroundColor(Color.argb((int) "+(int) alpha+", 255, 255, 255))\n"+"mFLayout.setAlpha("+percent+")");
                }

                //第二种

                // mFLayout.setAlpha(percent);


            }
        });
    }

    private void initView() {
        mWholeLayoutCdl = findViewById(R.id.wholeLayoutCdl);
        mAppBarLayout = findViewById(R.id.appbar);
        mScrollSv = findViewById(R.id.scrollSv);
        mTopReturnIv = findViewById(R.id.topReturnIv);
        mTopShareIv = findViewById(R.id.topShareIv);
        mScrollTopIv = findViewById(R.id.scrollTopIv);
        mViewpagerVp = findViewById(R.id.viewpagerVp);
        mStatedBtnBar = findViewById(R.id.statedBtnBar);
        mTopTitleNameTv = findViewById(R.id.topTitleNameTv);
        mProductNameTv = findViewById(R.id.productNameTv);
        mPriceTv = findViewById(R.id.priceTv);
        mGrayRmbIconTv = findViewById(R.id.grayRmbIconTv);
        mCityNameTv = findViewById(R.id.cityNameTv);
        mOrderCountTv = findViewById(R.id.orderCountTv);
        mHospitalNameTv = findViewById(R.id.hospitalNameTv);
        mHospitalCityNameTv = findViewById(R.id.hospitalCityNameTv);
        mProductDetailTv = findViewById(R.id.productDetailTv);
        mOrderNowTv = findViewById(R.id.orderNowTv);
        mCollectTabTv = findViewById(R.id.collectTabTv);
        mCollectRl = findViewById(R.id.collectRl);
        mHospitalCoverSdv = findViewById(R.id.hospitalCoverSdv);
        mVipDetailImgZdv = findViewById(R.id.vipDetailImgZdv);
//      VLUtils.setControllerListener(mVipDetailImgZdv,"http://p36zly2vu.bkt.clouddn.com/product/46acb4c0-0cce-11e8-9a80-a72b786a38c9.jpg");
        mShareDialog = new ShareDialog(this);

        mTopTitleNameTv.setText(getString(R.string.product_detail));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topReturnIv:
                finish();
                break;
            case R.id.topShareIv:
                mShareDialog.show();
                break;
            case R.id.scrollTopIv:
                //设置默认滚动到顶部
                VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
//                        android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
//
//
//                        behavior.onNestedPreScroll(mWholeLayoutCdl, mAppBarLayout, mScrollSv, 0, 10000, new int[]{0, 0});


                    }
                });
                break;
            case R.id.collectRl:
                // 切换收藏
                if (QingXinApplication.getInstance().getLoginUser() != null) {
                    // 如果登录过了
                    mPresenter.collect(id);
                } else {
                    // 如果没有登录就跳到登录页面
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.orderNowTv:
                // 立即预定
                if (QingXinApplication.getInstance().getLoginUser() != null) {
                    // 如果登录过了
                    mPresenter.book(id);
                } else {
                    // 如果没有登录就跳到登录页面
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void wxFriendShare() {

    }

    @Override
    public void wxCircleShare() {

    }

    @Override
    public void qqFriendsShare() {

    }

    @Override
    public void qqZoneShare() {

    }

    @Override
    public void weiboShare() {

    }

    @Override
    public void copyUrl() {

    }

    @Override
    public void onSuccess(VipDetailBean vipDetailBean) {
        Log.i("专享详情==", vipDetailBean.toString());
        setData(vipDetailBean);
    }

    @Override
    public void onSuccess(CollectBean collectBean) {
        if (collectBean.getIs_collect().equals("n")) {
            mCollectTabTv.setText(R.string.collect);
            showToast(getString(R.string.cancel_collect_ok));
        } else {
            mCollectTabTv.setText(R.string.cancel_collection);
            showToast(getString(R.string.collect_ok));
        }
    }

    @Override
    public void onSuccess(AmountBean amountBean) {
        Log.i("立即预定==", amountBean.toString());
        mOrderNowTv.setEnabled(false);
        mOrderNowTv.setBackgroundColor(getResources().getColor(R.color.line_color));
        mOrderCountTv.setText(getString(amountBean.getAmount()));
        Intent intent = new Intent();
        intent.putExtra(VIP_ID, id);
        intent.putExtra(BOOK_NUM, amountBean.getAmount());
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void setPresenter(VipDetailContract.Presenter presenter) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private void setData(VipDetailBean vipDetailBean) {

        VipDetailItemBean itemBean = vipDetailBean.getItems();

        mProductNameTv.setText(itemBean.getName());
        mPriceTv.setText(String.valueOf(itemBean.getPrice()));
        mGrayRmbIconTv.setText(getString(R.string.rmb_icon) + itemBean.getOld_price());
//        mCityNameTv.setText();
        mOrderCountTv.setText(String.valueOf(itemBean.getOrder()) + getString(R.string.order_count));
        mHospitalNameTv.setText(itemBean.getHospital());
//        mProductDetailTv.setText(itemBean.get);
        VLUtils.setControllerListener(mVipDetailImgZdv, "http://p36zly2vu.bkt.clouddn.com/" + itemBean.getCover());

    }

    private class BannerPagerAdapter extends PagerAdapter {

        private List<Integer> mBannerList;
        private Context mContext;

        BannerPagerAdapter(Context context, List<Integer> bannerList) {
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
            View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_vip_detail_viewpager, container, false);
            SimpleDraweeView mSlectionMoreRl = mView.findViewById(R.id.slectionMoreRl);
            mSlectionMoreRl.setImageDrawable(getResources().getDrawable(mBannerList.get(position % mBannerList.size())));
            container.addView(mView);
            return mView;
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

}
