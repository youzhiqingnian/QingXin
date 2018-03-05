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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.app.vip.VipListActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.FragmentToActivity;
import com.qingxin.medical.home.districtsel.StrictSelBean;
import com.qingxin.medical.home.districtsel.StrictSelDetailActivity;
import com.qingxin.medical.home.districtsel.StrictSelListActivity;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyActivity;
import com.qingxin.medical.search.SearchActivity;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLScheduler;
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

    private VLPagerView mPagerView;
    private LinearLayout mProductListLl;

    private SimpleDraweeView mFirstPrCoverSdv,
            mSecondPrCoverSdv,
            mThirdPrCoverSdv;

    private FrameLayout mSlectionMoreRl;
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
        TextView cityTv = mRootView.findViewById(R.id.cityTv);
        mFirstPrCoverSdv = mRootView.findViewById(R.id.firstPrCoverSdv);
        mSecondPrCoverSdv = mRootView.findViewById(R.id.secondPrCoverSdv);
        mThirdPrCoverSdv = mRootView.findViewById(R.id.thirdPrCoverSdv);
        mProductListLl = mRootView.findViewById(R.id.productListLl);

        LinearLayout shareRl = mRootView.findViewById(R.id.shareRl);
        LinearLayout diaryRl = mRootView.findViewById(R.id.diaryRl);
        LinearLayout selectionRl = mRootView.findViewById(R.id.selectionRl);
        LinearLayout encyclopediasRl = mRootView.findViewById(R.id.encyclopediasRl);
        FrameLayout diaryMoreRl = mRootView.findViewById(R.id.diaryMoreRl);
        LinearLayout searchLl = mRootView.findViewById(R.id.searchLl);
        mSlectionMoreRl = mRootView.findViewById(R.id.slectionMoreRl);
        mStatedBtnBar = mRootView.findViewById(R.id.statedBtnBar);

        AMapLocation aMapLocation = QingXinApplication.getInstance().getLocationService().getAMLocation();
        if (null != aMapLocation) {
            cityTv.setText(aMapLocation.getCity());
        }
        shareRl.setOnClickListener(this);
        diaryRl.setOnClickListener(this);
        selectionRl.setOnClickListener(this);
        encyclopediasRl.setOnClickListener(this);
        diaryMoreRl.setOnClickListener(this);
        mSlectionMoreRl.setOnClickListener(this);
        searchLl.setOnClickListener(this);
        showView(R.layout.layout_loading);
        VLScheduler.instance.schedule(200, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                getHomeData();
            }
        });


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
    private void setData() {
        TextView firstPrNameTv = mRootView.findViewById(R.id.firstPrNameTv);
        TextView firstPrPriceTv = mRootView.findViewById(R.id.firstPrPriceTv);
        TextView firstPrOldPriceTv = mRootView.findViewById(R.id.firstPrOldPriceTv);
        TextView secondPrNameTv = mRootView.findViewById(R.id.secondPrNameTv);
        TextView secondPrPriceTv = mRootView.findViewById(R.id.secondPrPriceTv);
        TextView secondPrOldPriceTv = mRootView.findViewById(R.id.secondPrOldPriceTv);
        TextView thirdPrNameTv = mRootView.findViewById(R.id.thirdPrNameTv);
        TextView thirdPrPriceTv = mRootView.findViewById(R.id.thirdPrPriceTv);
        TextView thirdPrOldPriceTv = mRootView.findViewById(R.id.thirdPrOldPriceTv);
        FrameLayout secondFl = mRootView.findViewById(R.id.secondFl);
        FrameLayout firstFl = mRootView.findViewById(R.id.firstFl);
        FrameLayout thirdFl = mRootView.findViewById(R.id.thirdFl);
        secondFl.setOnClickListener(this);
        firstFl.setOnClickListener(this);
        thirdFl.setOnClickListener(this);

        List<HomeBean.BannersBean> bannerList = mHomeBean.getBanners();
        mPagerView = mRootView.findViewById(R.id.viewpagerVp);
        BannerPagerAdapter adapter = new BannerPagerAdapter(getActivity(), bannerList);
        mPagerView.getViewPager().setAdapter(adapter);
        mPagerView.setPageChangeListener(position -> mStatedBtnBar.setChecked(position));
        mStatedBtnBar.setStatedButtonBarDelegate(new DotBarDelegate(getActivity(), bannerList.size()));
        mPagerView.setCurrentItem(bannerList.size() * 1000);
        mStatedBtnBar.setChecked(mPagerView.getCurrentItem());
        mPagerView.setAutoScroll(3000);

        List<ProductBean> productList = mHomeBean.getProducts();
        if (productList != null && productList.size() > 0) {
            if (productList.size() >= 1) {
                firstPrNameTv.setText(productList.get(0).getName());
                firstPrPriceTv.setText(String.format("%s元", productList.get(0).getPrice()));
                firstPrOldPriceTv.setText(String.format("原价%s元", productList.get(0).getOld_price()));
                firstPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mFirstPrCoverSdv.setImageURI(Uri.parse(productList.get(0).getCover()));
                firstFl.setTag(productList.get(0).getId());
            }

            if (productList.size() >= 2) {
                secondPrNameTv.setText(productList.get(1).getName());
                secondPrPriceTv.setText(String.format("%s元", productList.get(1).getPrice()));
                secondPrOldPriceTv.setText(String.format("原价%s元", productList.get(1).getOld_price()));
                secondPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mSecondPrCoverSdv.setImageURI(Uri.parse(productList.get(1).getCover()));
                secondFl.setTag(productList.get(1).getId());
            }

            if (productList.size() >= 3) {
                thirdPrNameTv.setText(productList.get(2).getName());
                thirdPrPriceTv.setText(String.format("%s元", productList.get(2).getPrice()));
                thirdPrOldPriceTv.setText(String.format("原价%s元", productList.get(2).getOld_price()));
                thirdPrOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mThirdPrCoverSdv.setImageURI(Uri.parse(productList.get(2).getCover()));
                thirdFl.setTag(productList.get(2).getId());
            }
        } else {
            View productTopLineTv = mRootView.findViewById(R.id.productTopLineTv);
            productTopLineTv.setVisibility(View.GONE);
            mProductListLl.setVisibility(View.GONE);
        }

        List<StrictSelBean> preferrsList = mHomeBean.getPreferrs();
        RecyclerView slectionRv = mRootView.findViewById(R.id.slectionRv);

        if (preferrsList == null || preferrsList.size() == 0) {
            TextView mSelectionGapTv = mRootView.findViewById(R.id.selectionGapTv);
            mSlectionMoreRl.setVisibility(View.GONE);
            mSelectionGapTv.setVisibility(View.GONE);
            slectionRv.setVisibility(View.GONE);
        } else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            slectionRv.setLayoutManager(mLayoutManager);
            slectionRv.addItemDecoration(new GridSpacingItemDecoration(2, VLUtils.dip2px(10), false));
            RecyclerGridViewAdapter strictSelctionAdapter = new RecyclerGridViewAdapter(preferrsList);
            slectionRv.setAdapter(strictSelctionAdapter);
            slectionRv.setNestedScrollingEnabled(false);
            strictSelctionAdapter.setOnItemClickListener((adapter12, view, position) -> StrictSelDetailActivity.startSelf(getActivity(), (StrictSelBean) adapter12.getData().get(position)));
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
        hideView(R.layout.layout_loading);
        mHomeBean = homeBean;
        Log.i("homeBean", homeBean.toString());
        if (mHomeBean != null) {
            setData();
        }
    }

    @Override
    public void onError(String result) {
        hideView(R.layout.layout_loading);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareRl: //歆人专享
                VipListActivity.startSelf(getActivity());
                break;
            case R.id.selectionRl: //本地严选(暂时换成专属服务)
                FragmentToActivity.startSelf(getActivity(),FragmentToActivity.EXCLUSIVE_SERVICE_FRAGMENT);
                break;
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
                String vid = (String) view.getTag();
                if (null == vid) return;
                VipDetailActivity.startSelf(getVLActivity(), vid, null);
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
            View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager, container, false);
            SimpleDraweeView simpleDraweeView = mView.findViewById(R.id.slectionMoreRl);
            simpleDraweeView.setImageURI(Uri.parse(mBannerList.get(position % mBannerList.size()).getCover()));
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
            }
        }
    };
}
