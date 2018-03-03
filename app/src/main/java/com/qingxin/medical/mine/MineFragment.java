package com.qingxin.medical.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qingxin.medical.QingXinAdapter;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinFragment;
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

/**
 * 首页我的界面
 *
 * @author zhikuo
 */
public class MineFragment extends QingXinFragment {

    private View mRootView;

//    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AppBarLayout mAppbar;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        return new MineFragment();
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

    }

    private void initView() {

        MagicIndicator indicator = mRootView.findViewById(R.id.magicIndicator);
        final VLFragment[] fragments = new VLFragment[]{MyAppointmengListFragment.newInstance(), MyDiaryListFragment.newInstance(), MyCollectionListFragment.newInstance()};
        final String[] titles = new String[]{getResources().getString(R.string.appointment_count), getResources().getString(R.string.diary_count), getResources().getString(R.string.collection_count)};
        QingXinAdapter adapter = new QingXinAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        final ViewPager viewPager = mRootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        CommonNavigator navigator = new CommonNavigator(getActivity());
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
                titleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator lineIndicator = new LinePagerIndicator(context);
                lineIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                lineIndicator.setLineHeight(VLUtils.dip2px(2));
                lineIndicator.setRoundRadius(VLUtils.dip2px(2));
                lineIndicator.setLineWidth(VLUtils.dip2px(16));
                lineIndicator.setColors(0xff3bc5e8);
                lineIndicator.setYOffset(VLUtils.dip2px(5));
                return lineIndicator;
            }
        });
        indicator.setNavigator(navigator);
        ViewPagerHelper.bind(indicator, viewPager);

//        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        mAppbar = mRootView.findViewById(R.id.appbar);
        RelativeLayout titleBarRl = mRootView.findViewById(R.id.titleBarRl);
        TextView topTitleNameTv = mRootView.findViewById(R.id.topTitleNameTv);
        ImageView settingIv = mRootView.findViewById(R.id.settingIv);


        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


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

                        if(dy >= toolbarHeight / 4){
                            topTitleNameTv.setTextColor(Color.argb((int) alpha, 70, 74, 76));
                        }

                    }
               /*     if (verticalOffset >= 0) {
                        mSwipeRefreshLayout.setEnabled(true);
                    } else {
                        mSwipeRefreshLayout.setEnabled(false);
                    }*/

                }
            }

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
