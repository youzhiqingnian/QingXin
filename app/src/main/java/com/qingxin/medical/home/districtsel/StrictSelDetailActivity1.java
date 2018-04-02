package com.qingxin.medical.home.districtsel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.widget.indicator.view.ShareDialog;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

/**
 * 严选详情界面 区别 {@link StrictSelDetailActivity} 本界面无视频
 *
 * @author zhikuo
 */
public class StrictSelDetailActivity1 extends QingXinActivity implements OnClickListener, ShareDialog.OnShareDialogListener, StrictSelDetailContract.View {

    public static final String STRICTSEL_ID = "STRICTSEL_ID";
    private ShareDialog mShareDialog;
    private StrictSelDetailPresenter mDetailPresenter;


    public static void startSelf(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, StrictSelDetailActivity1.class);
        intent.putExtra(STRICTSEL_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strictsel_detail1);
        mDetailPresenter = new StrictSelDetailPresenter(this);
        mDetailPresenter.getStrictSelDetail(getIntent().getStringExtra(STRICTSEL_ID));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topReturnIv: //返回键
                this.finish();
                break;
            case R.id.topShareIv: //分享键
                mShareDialog.show();
                break;
            default:
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
    public void setPresenter(StrictSelDetailContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(StrictSelBean strictSelBean) {
        setData(strictSelBean);
    }

    @Override
    public void onError(QingXinError error) {
        HandErrorUtils.handleError(error);
    }

    private void setData(StrictSelBean strictSelBean) {
        if (null == strictSelBean) return;
        TextView nameTv = findViewById(R.id.nameTv);
        TextView locationTv = findViewById(R.id.locationTv);
        TextView descrTv = findViewById(R.id.descrTv);
        ImageView backIv = findViewById(R.id.topReturnIv);
        ImageView topShareIv = findViewById(R.id.topShareIv);
        VLPagerView pagerView = findViewById(R.id.pagerView);
        VLStatedButtonBar statedBtnBar = findViewById(R.id.statedButtonBar);

        nameTv.setText(strictSelBean.getCity_name());
        descrTv.setText(strictSelBean.getSummary());
        locationTv.setText(strictSelBean.getName());
        mShareDialog = new ShareDialog(this);
        backIv.setOnClickListener(this);
        topShareIv.setOnClickListener(this);
        mShareDialog.setOnShareDialogListener(this);
        RelativeLayout titleBarRl = findViewById(R.id.titleBarRl);
        TextView titleNameTv = findViewById(R.id.topTitleNameTv);
        AppBarLayout appBar = findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int toolbarHeight = appBarLayout.getTotalScrollRange();
            int dy = Math.abs(verticalOffset);
            if (dy <= toolbarHeight) {
                float scale = (float) dy / toolbarHeight;
                float alpha = scale * 255;
                titleBarRl.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                titleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                if (dy >= toolbarHeight / 2) {
                    backIv.setImageResource(R.mipmap.ic_title_left_return);
                    topShareIv.setImageResource(R.mipmap.ic_top_right_share);
                } else {
                    backIv.setImageResource(R.mipmap.vip_detail_top_return_logo);
                    topShareIv.setImageResource(R.mipmap.vip_top_share_logo);
                }
            }
        });

        String[] cover = strictSelBean.getCover();
        BannerPagerAdapter adapter = new BannerPagerAdapter(this, cover);
        pagerView.getViewPager().setAdapter(adapter);
        pagerView.setPageChangeListener(statedBtnBar::setChecked);
        statedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(this, cover.length));
        pagerView.setCurrentItem(cover.length * 1000);
        statedBtnBar.setChecked(pagerView.getCurrentItem());
        pagerView.setAutoScroll(3000);
    }

    private class BannerPagerAdapter extends PagerAdapter {

        private String[] mBanners;
        private Context mContext;

        BannerPagerAdapter(@NonNull Context context, @NonNull String[] banners) {
            this.mContext = context;
            this.mBanners = banners;
        }

        @Override
        public int getCount() {
            return mBanners.length <= 1 ? mBanners.length : Short.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager, container, false);
            SimpleDraweeView simpleDraweeView = view.findViewById(R.id.simpleDraweeView);
            simpleDraweeView.setImageURI(Uri.parse(mBanners[position % mBanners.length]));
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

    @Override
    protected void onResume() {
        super.onResume();
        mDetailPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.unsubscribe();
    }
}
