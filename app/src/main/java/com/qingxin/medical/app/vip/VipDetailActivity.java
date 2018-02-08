package com.qingxin.medical.app.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.fresco.DoubleTapGestureListener;
import com.qingxin.medical.fresco.ZoomableDraweeView;
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
public class VipDetailActivity extends QingXinActivity implements View.OnClickListener, ShareDialog.OnShareDialogListener {

    private ScrollView mScrollSv;
    private ImageView mTopReturnIv,
            mTopShareIv,
            mScrollTopIv;
    private VLPagerView mViewpagerVp;
    private VLStatedButtonBar mStatedBtnBar;
    private TextView mProductNameTv,
            mPriceTv,
            mGrayRmbIconTv,
            mCityNameTv,
            mOrderCountTv,
            mHospitalNameTv,
            mHospitalCityNameTv,
            mProductDetailTv,
            mOrderNowTv;
    private RelativeLayout mCollectRl;
    private SimpleDraweeView mHospitalCoverSdv;
    private ZoomableDraweeView mVipDetailImgZdv;

    private List<Integer> bannerList = new ArrayList<>();

    private ShareDialog mShareDialog;

    public static void startSelf(VLActivity activity, String vipId) {
        Intent intent = new Intent(activity, VipDetailActivity.class);
        intent.putExtra(VIP_ID, vipId);
        activity.startActivity(intent);
    }

    public static final String VIP_ID = "VIP_ID";

    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_detail);
        dealIntent();
        initView();
        initListener();
        initViewPager();
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
    }

    private void initView() {
        mScrollSv = findViewById(R.id.scrollSv);
        mTopReturnIv = findViewById(R.id.topReturnIv);
        mTopShareIv = findViewById(R.id.topShareIv);
        mScrollTopIv = findViewById(R.id.scrollTopIv);
        mViewpagerVp = findViewById(R.id.viewpagerVp);
        mStatedBtnBar = findViewById(R.id.statedBtnBar);
        mProductNameTv = findViewById(R.id.productNameTv);
        mPriceTv = findViewById(R.id.priceTv);
        mGrayRmbIconTv = findViewById(R.id.grayRmbIconTv);
        mCityNameTv = findViewById(R.id.cityNameTv);
        mOrderCountTv = findViewById(R.id.orderCountTv);
        mHospitalNameTv = findViewById(R.id.hospitalNameTv);
        mHospitalCityNameTv = findViewById(R.id.hospitalCityNameTv);
        mProductDetailTv = findViewById(R.id.productDetailTv);
        mOrderNowTv = findViewById(R.id.orderNowTv);
        mCollectRl = findViewById(R.id.collectRl);
        mHospitalCoverSdv = findViewById(R.id.hospitalCoverSdv);
        mVipDetailImgZdv = findViewById(R.id.vipDetailImgZdv);
        mVipDetailImgZdv.setAllowTouchInterceptionWhileZoomed(true);
        // needed for double tap to zoom
        mVipDetailImgZdv.setIsLongpressEnabled(false);
        mVipDetailImgZdv.setTapListener(new DoubleTapGestureListener(mVipDetailImgZdv));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri("http://p36zly2vu.bkt.clouddn.com/product/eff1b140-0b55-11e8-9a80-a72b786a38c9.jpeg")
                .setCallerContext(this)
                .build();
        mVipDetailImgZdv.setController(controller);
        mShareDialog = new ShareDialog(this);
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
                        mScrollSv.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                break;
            case R.id.collectRl:

                break;
            case R.id.orderNowTv:

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
