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
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.qingxin.medical.user.User;
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

public class MineDataFragment extends QingXinFragment implements QingXinBroadCastReceiver.OnReceiverCallbackListener {

    private View mRootView;

    private AppBarLayout mAppbar;

    private List<TextView> mCountTextViewList = new ArrayList();

    private QingXinBroadCastReceiver mReceiver;

    public static final String COUNT_ACTION = "com.archie.action.COUNT_ACTION";
    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";

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
        if (getView() == null) return;
        mRootView = getView();

        initView();
        initBroadcastReceiver();

    }

    private void initView() {

        SimpleDraweeView userHeadSdv = mRootView.findViewById(R.id.userHeadSdv);
        ImageView defaultHeadIv = mRootView.findViewById(R.id.defaultHeadIv);
        TextView userNicknameTv = mRootView.findViewById(R.id.userNicknameTv);

        if (QingXinApplication.getInstance().getLoginUser() != null) {
            if (!VLUtils.stringIsEmpty(QingXinApplication.getInstance().getLoginUser().getCover())) {
                defaultHeadIv.setVisibility(View.GONE);
                userHeadSdv.setImageURI(Uri.parse(QingXinApplication.getInstance().getLoginUser().getCover()));
            }
            userNicknameTv.setText(QingXinApplication.getInstance().getLoginUser().getName());
        }

        MagicIndicator indicator = mRootView.findViewById(R.id.magicIndicator);
        final VLFragment[] fragments = new VLFragment[]{MyBookedProductListFragment.newInstance(), MyPublishedDiaryListFragment.newInstance(), MyCollectedTabListFragment.newInstance()};
        final String[] titles = new String[]{getResources().getString(R.string.appointment_count), getResources().getString(R.string.diary_count), getResources().getString(R.string.collection_count)};
        QingXinAdapter adapter = new QingXinAdapter(getChildFragmentManager(), fragments, titles);
        final ViewPager viewPager = mRootView.findViewById(R.id.viewPager);
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
                    setCountRefresh();
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

        mAppbar = mRootView.findViewById(R.id.appbar);
        RelativeLayout titleBarRl = mRootView.findViewById(R.id.titleBarRl);
        TextView topTitleNameTv = mRootView.findViewById(R.id.topTitleNameTv);
        ImageView settingIv = mRootView.findViewById(R.id.settingIv);


        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
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
    }

    /**
     * 初始化广播接收者
     */
    private void initBroadcastReceiver() {
        mReceiver = new QingXinBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(COUNT_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        mReceiver.setReceiverListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if (intent.getBooleanExtra("refresh", false)) {
            setCountRefresh();
        }
    }


    private void setCountRefresh() {
        User sessionBean = QingXinApplication.getInstance().getLoginSession().getMem();
        String bookCount = sessionBean.getBook_amount() + "";
        String diaryCount = sessionBean.getDiary_amount() + "";
        String collectCount = sessionBean.getCollect_amount() + "";

        if (!VLUtils.stringIsEmpty(bookCount)) {
            mCountTextViewList.get(0).setText(getActivity().getResources().getString(R.string.appointment_count_prefix) + bookCount);
        }
        if (!VLUtils.stringIsEmpty(diaryCount)) {
            mCountTextViewList.get(1).setText(getActivity().getResources().getString(R.string.diary_count_prefix) + diaryCount);

        }
        if (!VLUtils.stringIsEmpty(collectCount)) {
            mCountTextViewList.get(2).setText(getActivity().getResources().getString(R.string.collection_count_prefix) + collectCount);
        }
    }

}
