package com.qingxin.medical.mine;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingxin.medical.QingXinAdapter;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLFragment;

/**
 * Date 2018-03-03
 *
 * @author zhikuo1
 */

public class MyCollectListFragment extends VLFragment implements View.OnClickListener {

    private View mRootView;

    private TextView mProductTv, mDiaryTv;

    private ViewPager mViewPager;

    private boolean isDiary = false;

    public MyCollectListFragment() {
    }

    public static MyCollectListFragment newInstance() {
        return new MyCollectListFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_collect, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;
        mRootView = getView();

        initView();

        initListener();

    }

    private void initView() {

        mProductTv = mRootView.findViewById(R.id.productTv);
        mDiaryTv = mRootView.findViewById(R.id.diaryTv);

        final VLFragment[] fragments = new VLFragment[]{MyCollectionProductListFragment.newInstance(), MyCollectionDiaryListFragment.newInstance()};
        QingXinAdapter adapter = new QingXinAdapter(getActivity().getSupportFragmentManager(), fragments, null);
        mViewPager = mRootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

    }

    private void initListener() {
        mProductTv.setOnClickListener(this);
        mDiaryTv.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.productTv:
                // 我收藏的产品
                if (isDiary) {
                    isDiary = false;
                    mProductTv.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_button));
                    mProductTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                    mDiaryTv.setBackground(getActivity().getResources().getDrawable(R.drawable.mine_gray_button));
                    mDiaryTv.setTextColor(getActivity().getResources().getColor(R.color.text_color_origin_price));
                    mViewPager.setCurrentItem(0);
                }

                break;

            case R.id.diaryTv:
                // 我收藏的日记
                if (!isDiary) {
                    isDiary = true;
                    mDiaryTv.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_button));
                    mDiaryTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                    mProductTv.setBackground(getActivity().getResources().getDrawable(R.drawable.mine_gray_button));
                    mProductTv.setTextColor(getActivity().getResources().getColor(R.color.text_color_origin_price));
                    mViewPager.setCurrentItem(1);
                }
                break;
        }
    }
}
