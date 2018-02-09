package com.qingxin.medical.home.districtsel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.qingxin.medical.QingXinAdapter;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.widget.indicator.CommonNavigator;
import com.qingxin.medical.widget.indicator.CommonNavigatorAdapter;
import com.qingxin.medical.widget.indicator.IPagerIndicator;
import com.qingxin.medical.widget.indicator.IPagerTitleView;
import com.qingxin.medical.widget.indicator.LinePagerIndicator;
import com.qingxin.medical.widget.indicator.MagicIndicator;
import com.qingxin.medical.widget.indicator.SimplePagerTitleView;
import com.qingxin.medical.widget.indicator.ViewPagerHelper;
import com.vlee78.android.vl.VLUtils;

/**
 * Date 2018/2/6
 *
 * @author zhikuo
 */
public class StrictSelListActivity extends QingXinActivity implements View.OnClickListener {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, StrictSelListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strictsel_list);
        findViewById(R.id.backIv).setOnClickListener(this);

        MagicIndicator indicator = findViewById(R.id.magicIndicator);
        final QingXinFragment[] fragments = new QingXinFragment[]{StrictSelListFragment.newInstance(StrictSelListFragment.STRICTSEL_TYEP_HOSPITALS), StrictSelListFragment.newInstance(StrictSelListFragment.STRICTSEL_TYEP_DOCTORS)};
        final String[] titles = new String[]{getResources().getString(R.string.agency), getResources().getString(R.string.doctor)};
        QingXinAdapter adapter = new QingXinAdapter(getSupportFragmentManager(), fragments, titles);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setTextNormalColor(0xff464a4c);
                titleView.setTextSelectedColor(0xff3bc5e8);
                titleView.setText(titles[index]);
                titleView.setTextSize(18);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backIv:
                this.finish();
                break;
            default:
                break;
        }
    }
}
