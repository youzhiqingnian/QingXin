package com.qingxin.medical.app.homepagetask;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.Constants;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListActivity;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.service.entity.Book;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 首界面
 */
public class HomeFragment extends VLFragment implements HomePageTaskContract.View, View.OnClickListener {

    private View mRootView;

    private TextView mCityTv,
            mFirstPrNameTv,
            mFirstPrPriceTv,
            mFirstPrOldPriceTv,
            mSecondPrNameTv,
            mSecondPrPriceTv,
            mSecondPrOldPriceTv,
            mThirdPrNameTv,
            mThirdPrPriceTv,
            mThirdPrOldPriceTv;

    private SimpleDraweeView mFirstPrCoverSdv,
            mSecondPrCoverSdv,
            mThirdPrCoverSdv;

    private RelativeLayout mShareRl,
            mDiaryRl,
            mSelectionRl,
            mEncyclopediasRl,
            mSlectionMoreRl,
            mDiaryMoreRl;

    private VLStatedButtonBar mStatedBtnBar;

    private HomePageTaskContract.Presenter mPresenter;

    private HomeBean mHomeBean;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


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
        new HomePageTaskPresenter(this);
        if (getView() == null) {
            return;
        }
        mRootView = getView();


        initView();
        initListener();

        int screenWidth = VLUtils.getScreenWidth(getActivity());

        String banner_size = screenWidth + "-" + VLUtils.dip2px(180);
        String product_size = screenWidth * 325 / 750 + "-" + VLUtils.dip2px(180) + "," + screenWidth * 425 / 750 + "-" + VLUtils.dip2px(180);
        String diary_size = (screenWidth - VLUtils.dip2px(40)) / 2 + "-" + VLUtils.dip2px(168);

        mPresenter.getHomeData(banner_size, product_size, diary_size);

    }

    private void initView() {
        mCityTv = mRootView.findViewById(R.id.mCityTv);
        mFirstPrNameTv = mRootView.findViewById(R.id.mFirstPrNameTv);
        mFirstPrPriceTv = mRootView.findViewById(R.id.mFirstPrPriceTv);
        mFirstPrOldPriceTv = mRootView.findViewById(R.id.mFirstPrOldPriceTv);
        mSecondPrNameTv = mRootView.findViewById(R.id.mSecondPrNameTv);
        mSecondPrPriceTv = mRootView.findViewById(R.id.mSecondPrPriceTv);
        mSecondPrOldPriceTv = mRootView.findViewById(R.id.mSecondPrOldPriceTv);
        mThirdPrNameTv = mRootView.findViewById(R.id.mThirdPrNameTv);
        mThirdPrPriceTv = mRootView.findViewById(R.id.mThirdPrPriceTv);
        mThirdPrOldPriceTv = mRootView.findViewById(R.id.mThirdPrOldPriceTv);

        mFirstPrCoverSdv = mRootView.findViewById(R.id.mFirstPrCoverSdv);
        mSecondPrCoverSdv = mRootView.findViewById(R.id.mSecondPrCoverSdv);
        mThirdPrCoverSdv = mRootView.findViewById(R.id.mThirdPrCoverSdv);

        mShareRl = mRootView.findViewById(R.id.mShareRl);
        mDiaryRl = mRootView.findViewById(R.id.mDiaryRl);
        mSelectionRl = mRootView.findViewById(R.id.mSelectionRl);
        mEncyclopediasRl = mRootView.findViewById(R.id.mEncyclopediasRl);
        mSlectionMoreRl = mRootView.findViewById(R.id.mSlectionMoreRl);
        mDiaryMoreRl = mRootView.findViewById(R.id.mDiaryMoreRl);

        mStatedBtnBar = mRootView.findViewById(R.id.mStatedBtnBar);
    }


    private void requestGaodeLoction() {

        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //获取一次定位结果：
        //该方法默认为false
        mLocationOption.setOnceLocation(true);

//        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(1000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);


        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);


        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE},
                        Constants.GAODE_MAP_GRANTED_REQUEST_CODE);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                //启动定位
                mLocationClient.startLocation();
            }

        } else {
            //启动定位
            mLocationClient.startLocation();
        }


        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        Log.i("城市码", aMapLocation.getCityCode() + aMapLocation.getCity());
                        String cityName = aMapLocation.getCity();
                        if (cityName.endsWith(getStr(R.string.city_unit))) {
                            cityName = cityName.substring(0, cityName.length() - 1);
                        }
                        mCityTv.setText(cityName);

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        // 提示定位失败
                        Toast.makeText(getActivity(), getStr(R.string.location_failuer), Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }

    private void initListener() {
        mShareRl.setOnClickListener(this);
        mDiaryRl.setOnClickListener(this);
        mSelectionRl.setOnClickListener(this);
        mEncyclopediasRl.setOnClickListener(this);
        mSlectionMoreRl.setOnClickListener(this);
        mDiaryMoreRl.setOnClickListener(this);
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

        ViewPager mViewpagerVp = mRootView.findViewById(R.id.mViewpagerVp);
        BannerPagerAdapter mAdapter = new BannerPagerAdapter(getActivity(), bannerList);
        mViewpagerVp.setAdapter(mAdapter);

        mViewpagerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mStatedBtnBar.setChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mStatedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(getActivity(), bannerList.size()));
        mViewpagerVp.setCurrentItem(bannerList.size() * 1000);
        mStatedBtnBar.setChecked(mViewpagerVp.getCurrentItem());

        List<HomeBean.ContentBean.ProductsBean> productList = mHomeBean.getContent().getProducts();

        if (productList.size() >= 1) {
            mFirstPrNameTv.setText(productList.get(0).getName());
            mFirstPrPriceTv.setText(productList.get(0).getPrice() + getStr(R.string.yuan));
            mFirstPrOldPriceTv.setText(getStr(R.string.origin_price) + productList.get(0).getOld_price() + getStr(R.string.yuan));
            mFirstPrCoverSdv.setImageURI(Uri.parse(productList.get(0).getCover()));
        }

        if (productList.size() >= 2) {
            mSecondPrNameTv.setText(productList.get(1).getName());
            mSecondPrPriceTv.setText(productList.get(1).getPrice() + getStr(R.string.yuan));
            mSecondPrOldPriceTv.setText(getStr(R.string.origin_price) + productList.get(1).getOld_price() + getStr(R.string.yuan));
            mSecondPrCoverSdv.setImageURI(Uri.parse(productList.get(1).getCover()));
        }

        if (productList.size() >= 3) {
            mThirdPrNameTv.setText(productList.get(2).getName());
            mThirdPrPriceTv.setText(productList.get(2).getPrice() + getStr(R.string.yuan));
            mThirdPrOldPriceTv.setText(getStr(R.string.origin_price) + productList.get(2).getOld_price() + getStr(R.string.yuan));
            mThirdPrCoverSdv.setImageURI(Uri.parse(productList.get(2).getCover()));
        }


        RecyclerView mSlectionRv = mRootView.findViewById(R.id.mSlectionRv);
        RecyclerView mDiaryRv = mRootView.findViewById(R.id.mDiaryRv);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mSlectionRv.setLayoutManager(mLayoutManager);
        mSlectionRv.addItemDecoration(new GridSpacingItemDecoration(2, VLUtils.dip2px(10), false));
        RecyclerGridViewAdapter strictSelctionAdapter = new RecyclerGridViewAdapter(getActivity());
        mSlectionRv.setAdapter(strictSelctionAdapter);
        mSlectionRv.setNestedScrollingEnabled(false);

        List<HomeBean.ContentBean.DiarysBean> diaryList = mHomeBean.getContent().getDiarys();
        if (diaryList != null && diaryList.size() > 0) {
            mDiaryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            HomeGoddessDiaryAdapter mGoddessDiaryAdapter = new HomeGoddessDiaryAdapter(getActivity(), diaryList);
            mDiaryRv.setAdapter(mGoddessDiaryAdapter);
            mDiaryRv.setNestedScrollingEnabled(false);
        }

        /**
         * 初始化定位
         */
        requestGaodeLoction();

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
        if (mHomeBean != null && mHomeBean.getCode().equals("200")) {
            setData();
        }

    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mShareRl:
                // 歆人专享

                break;

            case R.id.mSelectionRl:
            case R.id.mSlectionMoreRl:
                // 本地严选

                break;

            case R.id.mEncyclopediasRl:
                // 医美百科

                break;

            case R.id.mDiaryRl:
            case R.id.mDiaryMoreRl:
                // 女神日记
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
            View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager, container, false);
            SimpleDraweeView mSlectionMoreRl = mView.findViewById(R.id.mSlectionMoreRl);
            mSlectionMoreRl.setImageURI(Uri.parse(mBannerList.get(position % mBannerList.size()).getCover()));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.GAODE_MAP_GRANTED_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.startLocation();
            } else {
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
