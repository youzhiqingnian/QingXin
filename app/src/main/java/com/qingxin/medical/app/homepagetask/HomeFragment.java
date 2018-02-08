package com.qingxin.medical.app.homepagetask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailActivity;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListActivity;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListAdapter;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.homepagetask.model.ProductBean;
import com.qingxin.medical.app.vip.VipListActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.home.districtsel.StrictSelListActivity;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

import java.util.List;

/**
 * 首页面
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class HomeFragment extends VLFragment implements HomePageTaskContract.View, View.OnClickListener {

    private View mRootView;

    private TextView mFirstPrNameTv,
            mFirstPrPriceTv,
            mFirstPrOldPriceTv,
            mSecondPrNameTv,
            mSecondPrPriceTv,
            mSecondPrOldPriceTv,
            mThirdPrNameTv,
            mThirdPrPriceTv,
            mThirdPrOldPriceTv;

    private LinearLayout mProductListLl;

    private SimpleDraweeView mFirstPrCoverSdv,
            mSecondPrCoverSdv,
            mThirdPrCoverSdv;

    private RelativeLayout mSlectionMoreRl,
            mDiaryMoreRl;

    private VLStatedButtonBar mStatedBtnBar;

    private HomePageTaskContract.Presenter mPresenter;

    private HomeBean mHomeBean;
    private GoddessDiaryListAdapter mDiaryListAdapter;

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
        mPresenter = new HomePageTaskPresenter(this);
        if (getView() == null) {
            return;
        }
        mRootView = getView();
        initView();
        initListener();
    }

    private void initView() {
        TextView mCityTv = mRootView.findViewById(R.id.cityTv);
        mFirstPrNameTv = mRootView.findViewById(R.id.firstPrNameTv);
        mFirstPrPriceTv = mRootView.findViewById(R.id.firstPrPriceTv);
        mFirstPrOldPriceTv = mRootView.findViewById(R.id.firstPrOldPriceTv);
        mSecondPrNameTv = mRootView.findViewById(R.id.secondPrNameTv);
        mSecondPrPriceTv = mRootView.findViewById(R.id.secondPrPriceTv);
        mSecondPrOldPriceTv = mRootView.findViewById(R.id.secondPrOldPriceTv);
        mThirdPrNameTv = mRootView.findViewById(R.id.thirdPrNameTv);
        mThirdPrPriceTv = mRootView.findViewById(R.id.thirdPrPriceTv);
        mThirdPrOldPriceTv = mRootView.findViewById(R.id.thirdPrOldPriceTv);


        mFirstPrCoverSdv = mRootView.findViewById(R.id.firstPrCoverSdv);
        mSecondPrCoverSdv = mRootView.findViewById(R.id.secondPrCoverSdv);
        mThirdPrCoverSdv = mRootView.findViewById(R.id.thirdPrCoverSdv);
        mProductListLl = mRootView.findViewById(R.id.productListLl);

        LinearLayout shareRl = mRootView.findViewById(R.id.shareRl);
        LinearLayout diaryRl = mRootView.findViewById(R.id.diaryRl);
        LinearLayout selectionRl = mRootView.findViewById(R.id.selectionRl);
        LinearLayout encyclopediasRl = mRootView.findViewById(R.id.encyclopediasRl);
        mSlectionMoreRl = mRootView.findViewById(R.id.slectionMoreRl);
        mDiaryMoreRl = mRootView.findViewById(R.id.diaryMoreRl);

        mStatedBtnBar = mRootView.findViewById(R.id.statedBtnBar);
        AMapLocation aMapLocation = QingXinApplication.getInstance().getLocationService().getAMLocation();
        if (null != aMapLocation) {
            mCityTv.setText(aMapLocation.getCity());
        }

        shareRl.setOnClickListener(this);
        diaryRl.setOnClickListener(this);
        selectionRl.setOnClickListener(this);
        encyclopediasRl.setOnClickListener(this);


        getHomeData();

    }

    private void initListener() {
        mSlectionMoreRl.setOnClickListener(this);
        mDiaryMoreRl.setOnClickListener(this);
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
    private void setData() {
        List<HomeBean.BannersBean> bannerList = mHomeBean.getBanners();
        VLPagerView pagerView = mRootView.findViewById(R.id.viewpagerVp);
        BannerPagerAdapter adapter = new BannerPagerAdapter(getActivity(), bannerList);
        pagerView.getViewPager().setAdapter(adapter);
        pagerView.setPageChangeListener(position -> mStatedBtnBar.setChecked(position));
        mStatedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(getActivity(), bannerList.size()));
        pagerView.setCurrentItem(bannerList.size() * 1000);
        mStatedBtnBar.setChecked(pagerView.getCurrentItem());
        pagerView.setAutoScroll(3000);

        List<ProductBean> productList = mHomeBean.getProducts();

        if (productList != null && productList.size() > 0) {
            if (productList.size() >= 1) {
                mFirstPrNameTv.setText(productList.get(0).getName());
                mFirstPrPriceTv.setText(productList.get(0).getPrice() + getStr(R.string.yuan));
                mFirstPrOldPriceTv.setText(getStr(R.string.origin_price) + productList.get(0).getOld_price() + getStr(R.string.yuan));
                mFirstPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mFirstPrCoverSdv.setImageURI(Uri.parse(productList.get(0).getCover()));
            }

            if (productList.size() >= 2) {
                mSecondPrNameTv.setText(productList.get(1).getName());
                mSecondPrPriceTv.setText(productList.get(1).getPrice() + getStr(R.string.yuan));
                mSecondPrOldPriceTv.setText(getStr(R.string.origin_price) + productList.get(1).getOld_price() + getStr(R.string.yuan));
                mSecondPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mSecondPrCoverSdv.setImageURI(Uri.parse(productList.get(1).getCover()));
            }

            if (productList.size() >= 3) {
                mThirdPrNameTv.setText(productList.get(2).getName());
                mThirdPrPriceTv.setText(productList.get(2).getPrice() + getStr(R.string.yuan));
                mThirdPrOldPriceTv.setText(getStr(R.string.origin_price) + productList.get(2).getOld_price() + getStr(R.string.yuan));
                mThirdPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mThirdPrCoverSdv.setImageURI(Uri.parse(productList.get(2).getCover()));
            }
        } else {
            TextView mProductTopLineTv = mRootView.findViewById(R.id.productTopLineTv);
            mProductTopLineTv.setVisibility(View.GONE);
            mProductListLl.setVisibility(View.GONE);
        }

        List<HomeBean.PreferrsBean> preferrsList = mHomeBean.getPreferrs();


        RecyclerView mSlectionRv = mRootView.findViewById(R.id.slectionRv);

        if (preferrsList == null || preferrsList.size() == 0) {

            TextView mSelectionGapTv = mRootView.findViewById(R.id.selectionGapTv);

            mSlectionMoreRl.setVisibility(View.GONE);
            mSelectionGapTv.setVisibility(View.GONE);
            mSlectionRv.setVisibility(View.GONE);
        } else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mSlectionRv.setLayoutManager(mLayoutManager);
            mSlectionRv.addItemDecoration(new GridSpacingItemDecoration(2, VLUtils.dip2px(10), false));
            RecyclerGridViewAdapter strictSelctionAdapter = new RecyclerGridViewAdapter(getActivity(), preferrsList);
            mSlectionRv.setAdapter(strictSelctionAdapter);
            mSlectionRv.setNestedScrollingEnabled(false);
        }


        RecyclerView diaryRv = mRootView.findViewById(R.id.diaryRv);
        List<DiaryItemBean> diaryList = mHomeBean.getDiarys();
        if (diaryList != null && diaryList.size() > 0) {
            diaryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            mDiaryListAdapter = new GoddessDiaryListAdapter(diaryList);
            diaryRv.addItemDecoration(new SpaceItemDecoration(VLUtils.dip2px(18)));
            mDiaryListAdapter.setOnItemClickListener((adapter1, view, position) -> GoddessDiaryDetailActivity.startSelf((VLActivity) getActivity(), mHomeBean.getDiarys().get(position).getId(), mResultListener));
            diaryRv.setAdapter(mDiaryListAdapter);
            diaryRv.setNestedScrollingEnabled(false);
        }
    }


    @Override
    public void setPresenter(HomePageTaskContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(HomeBean homeBean) {
        mHomeBean = homeBean;
        Log.i("homeBean", homeBean.toString());
        if (mHomeBean != null) {
            setData();
        }

    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.shareRl:
                // 歆人专享
                Intent vipIntent = new Intent(getActivity(), VipListActivity.class);
                startActivity(vipIntent);
                break;

            case R.id.selectionRl: // 本地严选
            case R.id.slectionMoreRl:
                StrictSelListActivity.startSelf(getActivity());
                break;
            case R.id.encyclopediasRl:
                // 医美百科

                break;

            case R.id.diaryRl:
            case R.id.diaryMoreRl:
                // 女神日记
                Intent intent = new Intent(getActivity(), GoddessDiaryListActivity.class);
                startActivity(intent);

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
            View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager, container, false);
            SimpleDraweeView mSlectionMoreRl = mView.findViewById(R.id.slectionMoreRl);
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

    private VLActivity.VLActivityResultListener mResultListener = new VLActivity.VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == GoddessDiaryDetailActivity.DIARY_DETAIL_REQUEST_CODE &&  resultCode == Activity.RESULT_OK) {
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
            }
        }
    };
}
