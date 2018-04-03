package com.qingxin.medical.app.homepagetask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinWebViewActivity;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailActivity;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListActivity;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListAdapter;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.homepagetask.model.VipProductBean;
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.app.vip.VipListActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.districtsel.StrictSelBean;
import com.qingxin.medical.home.districtsel.StrictSelDetailActivity1;
import com.qingxin.medical.home.districtsel.StrictSelListActivity;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyActivity;
import com.qingxin.medical.map.GaoDeMapModel;
import com.qingxin.medical.search.SearchActivity;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.widget.decoration.GridSpacingItemDecoration;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页面
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class HomeFragment extends VLFragment implements HomePageTaskContract.View, View.OnClickListener {

    private View mRootView, productTopLineTv;
    private TextView mCityTv;
    private VLPagerView mPagerView;
    private VLStatedButtonBar mStatedBtnBar;
    private TextView mFirstPrNameTv, mFirstPrPriceTv, mFirstPrOldPriceTv, mSecondPrNameTv, mSecondPrPriceTv, mSecondPrOldPriceTv, mThirdPrNameTv, mThirdPrPriceTv, mThirdPrOldPriceTv, mSelectionGapTv;
    private FrameLayout mFirstFl, mSecondFl, mThirdFl, mSlectionMoreRl;
    private SimpleDraweeView mFirstPrCoverSdv, mSecondPrCoverSdv, mThirdPrCoverSdv;
    private LinearLayout mProductListLl;
    private RecyclerView mSlectionRv;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HomePageTaskContract.Presenter mPresenter;
    private GoddessDiaryListAdapter mDiaryListAdapter;
    private RecyclerGridViewAdapter mStrictSelAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        mPresenter = new HomePageTaskPresenter(this);
        if (getView() == null) {
            return;
        }
        mRootView = getView();
        initView();
    }

    private void initView() {
        mCityTv = mRootView.findViewById(R.id.cityTv);
        LinearLayout shareRl = mRootView.findViewById(R.id.shareRl);
        LinearLayout diaryRl = mRootView.findViewById(R.id.diaryRl);
        LinearLayout selectionRl = mRootView.findViewById(R.id.selectionRl);
        LinearLayout encyclopediasRl = mRootView.findViewById(R.id.encyclopediasRl);
        FrameLayout diaryMoreRl = mRootView.findViewById(R.id.diaryMoreRl);
        LinearLayout searchLl = mRootView.findViewById(R.id.searchLl);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        mStatedBtnBar = mRootView.findViewById(R.id.statedButtonBar);

        AMapLocation aMapLocation = QingXinApplication.getInstance().getLocationService().getAMLocation();
        if (null != aMapLocation) {
            mCityTv.setText(aMapLocation.getCity());
        }
        mCityTv.setOnClickListener(this);
        shareRl.setOnClickListener(this);
        diaryRl.setOnClickListener(this);
        selectionRl.setOnClickListener(this);
        encyclopediasRl.setOnClickListener(this);
        diaryMoreRl.setOnClickListener(this);
        searchLl.setOnClickListener(this);

        mFirstPrNameTv = mRootView.findViewById(R.id.firstPrNameTv);
        mFirstPrPriceTv = mRootView.findViewById(R.id.firstPrPriceTv);
        mFirstPrOldPriceTv = mRootView.findViewById(R.id.firstPrOldPriceTv);
        mSecondPrNameTv = mRootView.findViewById(R.id.secondPrNameTv);
        mSecondPrPriceTv = mRootView.findViewById(R.id.secondPrPriceTv);
        mSecondPrOldPriceTv = mRootView.findViewById(R.id.secondPrOldPriceTv);
        mThirdPrNameTv = mRootView.findViewById(R.id.thirdPrNameTv);
        mThirdPrPriceTv = mRootView.findViewById(R.id.thirdPrPriceTv);
        mThirdPrOldPriceTv = mRootView.findViewById(R.id.thirdPrOldPriceTv);
        mSecondFl = mRootView.findViewById(R.id.secondFl);
        mFirstFl = mRootView.findViewById(R.id.firstFl);
        mThirdFl = mRootView.findViewById(R.id.thirdFl);
        mProductListLl = mRootView.findViewById(R.id.productListLl);
        mFirstPrCoverSdv = mRootView.findViewById(R.id.firstPrCoverSdv);
        mSecondPrCoverSdv = mRootView.findViewById(R.id.secondPrCoverSdv);
        mThirdPrCoverSdv = mRootView.findViewById(R.id.thirdPrCoverSdv);
        mPagerView = mRootView.findViewById(R.id.pagerView);
        productTopLineTv = mRootView.findViewById(R.id.productTopLineTv);
        mSlectionRv = mRootView.findViewById(R.id.slectionRv);
        mSelectionGapTv = mRootView.findViewById(R.id.selectionGapTv);
        mSlectionMoreRl = mRootView.findViewById(R.id.slectionMoreRl);
        RecyclerView diaryRv = mRootView.findViewById(R.id.diaryRv);

        mSecondFl.setOnClickListener(this);
        mFirstFl.setOnClickListener(this);
        mThirdFl.setOnClickListener(this);
        mSlectionMoreRl.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this::getHomeData);
        showViewBelowActionBar(R.layout.layout_loading, VLUtils.dip2px(48));
        VLScheduler.instance.schedule(200, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                getHomeData();
            }
        });

        mDiaryListAdapter = new GoddessDiaryListAdapter(null);
        diaryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        diaryRv.addItemDecoration(new SpaceItemDecoration(VLUtils.dip2px(18)));
        mDiaryListAdapter.setOnItemClickListener((adapter1, view, position) -> GoddessDiaryDetailActivity.startSelf((VLActivity) getActivity(), mDiaryListAdapter.getData().get(position).getId(), mResultListener));
        diaryRv.setAdapter(mDiaryListAdapter);
        diaryRv.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mSlectionRv.setLayoutManager(gridLayoutManager);
        mSlectionRv.addItemDecoration(new GridSpacingItemDecoration(2, VLUtils.dip2px(10), false));
        mStrictSelAdapter = new RecyclerGridViewAdapter(null);
        mSlectionRv.setAdapter(mStrictSelAdapter);
        mSlectionRv.setNestedScrollingEnabled(false);
        mStrictSelAdapter.setOnItemClickListener((adapter12, view, position) -> StrictSelDetailActivity1.startSelf(getActivity(), ((StrictSelBean) adapter12.getData().get(position)).getId()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        mPagerView.stopAutoScroll();
    }

    private void getHomeData() {
        int screenWidth = VLUtils.getScreenWidth(getActivity());
        String banner_size = screenWidth + "-" + VLUtils.dip2px(180);
        String product_size = screenWidth * 325 / 750 + "-" + VLUtils.dip2px(180) + "," + screenWidth * 425 / 750 + "-" + VLUtils.dip2px(180);
        String diary_size = (screenWidth - VLUtils.dip2px(40)) / 2 + "-" + VLUtils.dip2px(168);
        mPresenter.getHomeData(banner_size, product_size, diary_size);
    }

    /**
     * 填充数据
     */
    private void setData(@NonNull HomeBean homeBean) {
        mCityTv.setTag(homeBean);
        List<HomeBean.BannersBean> bannerList = homeBean.getBanners();
        BannerPagerAdapter adapter = new BannerPagerAdapter(getActivity(), bannerList);
        mPagerView.getViewPager().setAdapter(adapter);
        mPagerView.setPageChangeListener(position -> mStatedBtnBar.setChecked(position));
        mStatedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(getActivity(), bannerList.size()));
        mPagerView.setCurrentItem(bannerList.size() * 1000);
        mStatedBtnBar.setChecked(mPagerView.getCurrentItem());
        mPagerView.setAutoScroll(3000);
        List<String> productImgs = homeBean.getProductimgs();
        if (null != productImgs && productImgs.size() >= 3) {
            mFirstPrCoverSdv.setImageURI(Uri.parse(productImgs.get(0)));
            mSecondPrCoverSdv.setImageURI(Uri.parse(productImgs.get(1)));
            mThirdPrCoverSdv.setImageURI(Uri.parse(productImgs.get(2)));
        }

        List<VipProductBean> productList = homeBean.getProducts();
        if (productList != null && productList.size() > 0) {
            if (productList.size() >= 1) {
                mFirstPrNameTv.setText(productList.get(0).getName());
                mFirstPrPriceTv.setText(String.format("%s元", productList.get(0).getPrice()));
                mFirstPrOldPriceTv.setText(String.format("原价%s元", productList.get(0).getOld_price()));
                mFirstPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                List<String> idAndName = new ArrayList<>();
                idAndName.add(productList.get(0).getId());
                idAndName.add(productList.get(0).getName());
                mFirstFl.setTag(idAndName);
            }

            if (productList.size() >= 2) {
                mSecondPrNameTv.setText(productList.get(1).getName());
                mSecondPrPriceTv.setText(String.format("%s元", productList.get(1).getPrice()));
                mSecondPrOldPriceTv.setText(String.format("原价%s元", productList.get(1).getOld_price()));
                mSecondPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                List<String> idAndName = new ArrayList<>();
                idAndName.add(productList.get(1).getId());
                idAndName.add(productList.get(1).getName());
                mSecondFl.setTag(idAndName);
            }

            if (productList.size() >= 3) {
                mThirdPrNameTv.setText(productList.get(2).getName());
                mThirdPrPriceTv.setText(String.format("%s元", productList.get(2).getPrice()));
                mThirdPrOldPriceTv.setText(String.format("原价%s元", productList.get(2).getOld_price()));
                mThirdPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                List<String> idAndName = new ArrayList<>();
                idAndName.add(productList.get(2).getId());
                idAndName.add(productList.get(2).getName());
                mThirdFl.setTag(idAndName);
            }
        } else {
            productTopLineTv.setVisibility(View.GONE);
            mProductListLl.setVisibility(View.GONE);
        }

        List<StrictSelBean> preferrsList = homeBean.getPreferrs();
        if (preferrsList == null || preferrsList.size() == 0) {
            mSlectionMoreRl.setVisibility(View.GONE);
            mSelectionGapTv.setVisibility(View.GONE);
            mSlectionRv.setVisibility(View.GONE);
        } else {
            mStrictSelAdapter.setNewData(preferrsList);
        }
        mDiaryListAdapter.setNewData(homeBean.getDiarys());
    }


    @Override
    public void setPresenter(HomePageTaskContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(HomeBean homeBean) {
        hideView(R.layout.layout_loading);
        mSwipeRefreshLayout.setRefreshing(false);
        setData(homeBean);
    }

    @Override
    public void onError(QingXinError error) {
        mSwipeRefreshLayout.setRefreshing(false);
        hideView(R.layout.layout_loading);
        HandErrorUtils.handleError(error);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cityTv: //城市列表
                HomeBean homeBean = (HomeBean) mCityTv.getTag();
                if (null == homeBean) {
                    showToast("没有获取到数据,请退出重试");
                    return;
                }
                CityListActivity.startSelf(getVLActivity(), homeBean.getOpencitys(), mResultListener);
                break;
            case R.id.shareRl: //歆人专享
                VipListActivity.startSelf(getActivity());
                break;
            case R.id.selectionRl: //本地严选
            case R.id.slectionMoreRl:
                StrictSelListActivity.startSelf(getActivity());
                break;
            case R.id.encyclopediasRl: //医美百科
                MedicalBeautyActivity.startSelf(getVLActivity());
                break;
            case R.id.diaryRl: //女神日记
            case R.id.diaryMoreRl:
                GoddessDiaryListActivity.startSelf(getActivity());
                break;
            case R.id.secondFl:
            case R.id.firstFl:
            case R.id.thirdFl:
                String vid = ((List<String>) view.getTag()).get(0);
                String name = ((List<String>) view.getTag()).get(1);
                if (null == vid) return;
                VipDetailActivity.startSelf(getVLActivity(), vid, name, null);
                break;
            case R.id.searchLl:
                SearchActivity.startSelf(getActivity());
                break;
            default:
                break;
        }
    }


    private class BannerPagerAdapter extends PagerAdapter {

        private List<HomeBean.BannersBean> mBannerList;
        private Context mContext;

        BannerPagerAdapter(Context context, List<HomeBean.BannersBean> bannerList) {
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
            SimpleDraweeView simpleDraweeView = view.findViewById(R.id.simpleDraweeView);
            simpleDraweeView.setImageURI(Uri.parse(mBannerList.get(position % mBannerList.size()).getCover()));
            container.addView(view);
            simpleDraweeView.setOnClickListener(v -> QingXinWebViewActivity.startSelf(getActivity(), mBannerList.get(position % mBannerList.size()).getLink()));
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

    private VLActivity.VLActivityResultListener mResultListener = new VLActivity.VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == GoddessDiaryDetailActivity.DIARY_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                String diaryId = intent.getStringExtra(GoddessDiaryDetailActivity.DIARY_ID);
                int collectNum = intent.getIntExtra(GoddessDiaryDetailActivity.COLLECT_NUM, 0);
                List<DiaryItemBean> diaryItemBeans = mDiaryListAdapter.getData();
                int index = 0;
                for (DiaryItemBean diaryItemBean : diaryItemBeans) {
                    if (diaryItemBean.getId().equals(diaryId)) {
                        diaryItemBean.setCollect_num(collectNum);
                        mDiaryListAdapter.notifyItemChanged(index);
                        break;
                    }
                    index++;
                }
            } else if (requestCode == CityListActivity.CITY_LIST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                HomeBean.OpencitysBean opencitysBean = (HomeBean.OpencitysBean) intent.getSerializableExtra(CityListActivity.CURRENT_SELECT_CITY);
                GaoDeMapModel gaoDeMapModel = getModel(GaoDeMapModel.class);
                if (gaoDeMapModel.getOpencitysBean() == null) {
                    gaoDeMapModel.setOpencitysBean(opencitysBean);
                    mCityTv.setText(opencitysBean.getName());
                    getHomeData();
                } else {
                    if (!opencitysBean.getCitycode().equals(gaoDeMapModel.getOpencitysBean().getCitycode())) {
                        gaoDeMapModel.setOpencitysBean(opencitysBean);
                        mCityTv.setText(opencitysBean.getName());
                        getHomeData();
                    }
                }
            }
        }
    };
}
