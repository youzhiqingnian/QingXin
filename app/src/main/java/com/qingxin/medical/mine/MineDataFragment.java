package com.qingxin.medical.mine;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinAdapter;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.widget.indicator.CommonNavigator;
import com.qingxin.medical.widget.indicator.CommonNavigatorAdapter;
import com.qingxin.medical.widget.indicator.IPagerIndicator;
import com.qingxin.medical.widget.indicator.IPagerTitleView;
import com.qingxin.medical.widget.indicator.LinePagerIndicator;
import com.qingxin.medical.widget.indicator.MagicIndicator;
import com.qingxin.medical.widget.indicator.SimplePagerTitleView;
import com.qingxin.medical.widget.indicator.ViewPagerHelper;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLUtils;
import java.util.ArrayList;
import java.util.List;
/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */
public class MineDataFragment extends QingXinFragment implements QingXinBroadCastReceiver.OnReceiverCallbackListener, MineDataContract.View {

    private SimpleDraweeView mUserHeadSdv;
    private List<TextView> mCountTextViewList = new ArrayList();
    private QingXinBroadCastReceiver mReceiver;
    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";
    private MineDataPresenter mPresenter;
    private boolean isUpload;

    public MineDataFragment() {
    }

    public static MineDataFragment newInstance() {
        return new MineDataFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        initView();
        initBroadcastReceiver();
    }

    private void initView() {
        if (null == getView()) return;
        mUserHeadSdv = getView().findViewById(R.id.userHeadSdv);
        TextView userNicknameTv = getView().findViewById(R.id.userNicknameTv);

        userNicknameTv.setOnClickListener(view -> {
            PersonalInformationActivity.startSelf(getActivity());
        });

        if (QingXinApplication.getInstance().getLoginUser() != null) {
            userNicknameTv.setText(QingXinApplication.getInstance().getLoginUser().getName());
        }
        mPresenter = new MineDataPresenter(this);
        MagicIndicator indicator = getView().findViewById(R.id.magicIndicator);
        final VLFragment[] fragments = new VLFragment[]{MyBookedProductListFragment.newInstance(), MyPublishedDiaryListFragment.newInstance(), MyCollectedTabListFragment.newInstance()};
        final String[] titles = new String[]{getResources().getString(R.string.appointment_count), getResources().getString(R.string.diary_count), getResources().getString(R.string.collection_count)};
        QingXinAdapter adapter = new QingXinAdapter(getChildFragmentManager(), fragments, titles);
        final ViewPager viewPager = getView().findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        CommonNavigator navigator = new CommonNavigator(getActivity());
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setTextNormalColor(0xffb2b3b4);
                titleView.setTextSelectedColor(0xff3bc5e8);
                titleView.setText(titles[index]);
                titleView.setTextSize(16);
                mCountTextViewList.add(titleView);
                if (QingXinApplication.getInstance().getLoginSession() != null && mCountTextViewList.size() == 3) {
                    mPresenter.getSession();
                }
                titleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator lineIndicator = new LinePagerIndicator(context);
                lineIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                lineIndicator.setLineHeight(VLUtils.dip2px(2));
                lineIndicator.setRoundRadius(VLUtils.dip2px(2));
                lineIndicator.setColors(0xff3bc5e8);
                lineIndicator.setYOffset(VLUtils.dip2px(5));
                return lineIndicator;
            }
        });
        indicator.setNavigator(navigator);
        ViewPagerHelper.bind(indicator, viewPager);

        mUserHeadSdv.setOnClickListener(view -> {
            PersonalInformationActivity.startSelf(getActivity());
        });

        AppBarLayout appBarLayout1 = getView().findViewById(R.id.appbar);
        RelativeLayout titleBarRl = getView().findViewById(R.id.titleBarRl);
        TextView topTitleNameTv = getView().findViewById(R.id.topTitleNameTv);
        ImageView settingIv = getView().findViewById(R.id.settingIv);

        appBarLayout1.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int toolbarHeight = appBarLayout.getTotalScrollRange();
            int dy = Math.abs(verticalOffset);
            if (dy <= toolbarHeight) {
                float scale = (float) dy / toolbarHeight;
                float alpha = scale * 255;
                titleBarRl.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));

                if (dy >= toolbarHeight / 2) {
                    settingIv.setImageResource(R.mipmap.gray_setting_logo);
                    topTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                } else {
                    settingIv.setImageResource(R.mipmap.white_setting_logo);
                    topTitleNameTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                    if (dy >= toolbarHeight / 4) {
                        topTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                    }
                }
            }
        });
        settingIv.setOnClickListener(view -> {
            SettingActivity.startSelf(getActivity());
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
    }

    /**
     * 初始化广播接收者
     */
    private void initBroadcastReceiver() {
        mReceiver = new QingXinBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(REFRESH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        mReceiver.setReceiverListener(this);
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if (intent.getBooleanExtra("refresh", false)) {
            mPresenter.getSession();
        }
    }

    @Override
    protected void onVisible(boolean first) {
        super.onVisible(first);
        if (!first && !isUpload) {
            mPresenter.getSession();
        }
    }

    private void setCountRefresh(com.qingxin.medical.base.MemBean memBean) {
        if (!VLUtils.stringIsEmpty(memBean.getMem().getCover())) {
            mUserHeadSdv.setImageURI(Uri.parse(memBean.getMem().getCover()));
        }
        mCountTextViewList.get(0).setText(String.format("预约 · %s", memBean.getMem().getBook_amount()));
        mCountTextViewList.get(1).setText(String.format("日记 · %s", memBean.getMem().getDiary_amount()));
        mCountTextViewList.get(2).setText(String.format("收藏 · %s", memBean.getMem().getCollect_amount()));
    }

    @Override
    public void setPresenter(MineDataContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(com.qingxin.medical.base.MemBean memBean) {
        setCountRefresh(memBean);
    }

    @Override
    public void onError(QingXinError error) {
        hideView(R.layout.layout_loading);
        HandErrorUtils.handleError(error);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }
}
