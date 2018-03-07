package com.qingxin.medical.app.vip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.login.LoginActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.utils.HandErrorUtils;
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
            mCollectionIv,
            mScrollTopIv;
    private VLPagerView mPagerView;
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

//    private WebView mWebview;
//    private SimpleDraweeView mVipDetailImgZdv;

    private List<Integer> bannerList = new ArrayList<>();

    private ShareDialog mShareDialog;

    private VipDetailContract.Presenter mPresenter;

    public static final String BOOK_NUM = "BOOK_NUM";

    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";

    public static final String VIP_ID = "VIP_ID";
    public static final String VIP_TITLE = "VIP_TITLE";

    private String id = "";

    private String title = "";

    public static final int VIP_DETAIL_REQUEST_CODE = 6; // 跳到歆人专享详情里的请求码

    public static void startSelf(VLActivity activity, String vipId, String vipTitle, VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, VipDetailActivity.class);
        intent.putExtra(VIP_ID, vipId);
        intent.putExtra(VIP_TITLE, vipTitle);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, VIP_DETAIL_REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_detail);
        mPresenter = new VipDetailPresenter(this);
        dealIntent();
        initView();
        initListener();

        if (!TextUtils.isEmpty(id)) {
            mPresenter.getVipDetail(id);
        }
    }

    private void dealIntent() {
        if (getIntent() != null) {
            id = getIntent().getStringExtra(VIP_ID);
            title = getIntent().getStringExtra(VIP_TITLE);
        }
    }

    private void initViewPager(List<String> bannerList) {
        BannerPagerAdapter adapter = new BannerPagerAdapter(this, bannerList);
        mPagerView.getViewPager().setAdapter(adapter);
        mPagerView.setPageChangeListener(position -> mStatedBtnBar.setChecked(position));
        mStatedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(this, bannerList.size()));
        mPagerView.setCurrentItem(bannerList.size() * 1000);
        mStatedBtnBar.setChecked(mPagerView.getCurrentItem());
        mPagerView.setAutoScroll(3000);
    }

    private void initListener() {
        mTopReturnIv.setOnClickListener(this);
        mTopShareIv.setOnClickListener(this);
        mScrollTopIv.setOnClickListener(this);
        mCollectRl.setOnClickListener(this);
        mOrderNowTv.setOnClickListener(this);
        mShareDialog.setOnShareDialogListener(this);
        RelativeLayout titleBarRl = findViewById(R.id.titleBarRl);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int toolbarHeight = appBarLayout.getTotalScrollRange();
            int dy = Math.abs(verticalOffset);
            if (dy <= toolbarHeight) {
                float scale = (float) dy / toolbarHeight;
                float alpha = scale * 255;
                titleBarRl.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                mTopTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                if (dy >= toolbarHeight / 2) {
                    mTopReturnIv.setImageResource(R.mipmap.ic_title_left_return);
                    mTopShareIv.setImageResource(R.mipmap.ic_top_right_share);
                } else {
                    mTopReturnIv.setImageResource(R.mipmap.vip_detail_top_return_logo);
                    mTopShareIv.setImageResource(R.mipmap.vip_top_share_logo);
                }
            }
        });
    }

    private void initView() {
        mWholeLayoutCdl = findViewById(R.id.wholeLayoutCdl);
        mAppBarLayout = findViewById(R.id.appbar);
        mScrollSv = findViewById(R.id.scrollSv);
        mTopReturnIv = findViewById(R.id.topReturnIv);
        mCollectionIv = findViewById(R.id.collectionIv);
        mTopShareIv = findViewById(R.id.topShareIv);
        mScrollTopIv = findViewById(R.id.scrollTopIv);
        mPagerView = findViewById(R.id.viewpagerVp);
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
//        mWebview = findViewById(R.id.webview);
//        mVipDetailImgZdv = findViewById(R.id.vipDetailImgZdv);
//      VLUtils.setControllerListener(mVipDetailImgZdv,"http://p36zly2vu.bkt.clouddn.com/product/46acb4c0-0cce-11e8-9a80-a72b786a38c9.jpg");
        mShareDialog = new ShareDialog(this);

        mTopTitleNameTv.setText(title);
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
                        android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
                        behavior.onNestedPreScroll(mWholeLayoutCdl, mAppBarLayout, null, 0, 10000, new int[]{0, 0});
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
            mCollectionIv.setImageResource(R.mipmap.vip_unique_collect_logo);
            showToast(getString(R.string.cancel_collect_ok));
            Intent intent = new Intent();
            intent.putExtra(VIP_ID, id);
            setResult(Activity.RESULT_OK, intent);
            sendBroadCast(2);
        } else {
            mCollectTabTv.setText(R.string.cancel_collection);
            mCollectionIv.setImageResource(R.mipmap.already_collected_logo);
            showToast(getString(R.string.collect_ok));
            sendBroadCast(1);
        }
    }

    @Override
    public void onSuccess(AmountBean amountBean) {
        Log.i("立即预定==", amountBean.toString());
        mOrderNowTv.setEnabled(false);
        mOrderNowTv.setText(R.string.already_booked);
        mOrderCountTv.setText(String.valueOf(amountBean.getAmount()));
        showToast(R.string.already_booked);
        Intent intent = new Intent();
        intent.putExtra(VIP_ID, id);
        intent.putExtra(BOOK_NUM, String.valueOf(amountBean.getAmount()));
        setResult(Activity.RESULT_OK, intent);
        if (amountBean.getAmount() > 0) {
            // 通知我预约的产品列表刷新数据
            sendBroadCast(3);
        }
    }

    @Override
    public void onError(QingXinError error) {
        HandErrorUtils.handleError(error);
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
        mPagerView.stopAutoScroll();
    }

    private void setData(VipDetailBean vipDetailBean) {

        VipDetailItemBean itemBean = vipDetailBean.getItems();
        initViewPager(itemBean.getCover());
        mProductNameTv.setText(itemBean.getName());
        mPriceTv.setText(String.valueOf(itemBean.getPrice()));
        mGrayRmbIconTv.setText(String.format("￥%s", itemBean.getOld_price()));
        mCityNameTv.setText(String.format("%s%s", itemBean.getProvince_name(), itemBean.getCity_name()));
        mOrderCountTv.setText(String.format("%s次预约", itemBean.getOrder()));
        mHospitalNameTv.setText(itemBean.getHospital());

        if (vipDetailBean.getItems().getIs_collect().equals("n")) {
            // 如果没有收藏
            mCollectTabTv.setText(R.string.collect);
            mCollectionIv.setImageResource(R.mipmap.vip_unique_collect_logo);
        } else {
            // 如果收藏过
            mCollectTabTv.setText(R.string.cancel_collection);
            mCollectionIv.setImageResource(R.mipmap.already_collected_logo);
        }

        if (vipDetailBean.getItems().getIs_book().equals("y")) {
            // 如果该用户预定过该产品
            mOrderNowTv.setEnabled(false);
            mOrderNowTv.setText(R.string.already_booked);
        }

        if(!VLUtils.stringIsEmpty(itemBean.getAbout())){
            mProductDetailTv.setText(Html.fromHtml(itemBean.getAbout()));
        }
//        VLUtils.setControllerListener(mVipDetailImgZdv, "http://p36zly2vu.bkt.clouddn.com/" + itemBean.getCover());
    }

    private class BannerPagerAdapter extends PagerAdapter {

        private List<String> mBannerList;
        private Context mContext;

        BannerPagerAdapter(Context context, List<String> bannerList) {
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
            mSlectionMoreRl.setImageURI(mBannerList.get(position % mBannerList.size()));
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

    private void sendBroadCast(int flag) {
        Intent intent = new Intent(REFRESH_ACTION);
        intent.putExtra("refresh", true);
        if (flag == 1) {
            // 收藏产品
            int collect_amount = QingXinApplication.getInstance().getLoginSession().getMem().getCollect_amount() + 1;
            QingXinApplication.getInstance().getLoginSession().getMem().setCollect_amount(collect_amount);
        } else if (flag == 2) {
            // 取消收藏产品
            int collect_amount = QingXinApplication.getInstance().getLoginSession().getMem().getCollect_amount() - 1;
            QingXinApplication.getInstance().getLoginSession().getMem().setCollect_amount(collect_amount);
        } else if (flag == 3) {
            // 预定产品
            int book_amount = QingXinApplication.getInstance().getLoginSession().getMem().getBook_amount() + 1;
            QingXinApplication.getInstance().getLoginSession().getMem().setBook_amount(book_amount);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(
                intent
        );
    }

}
