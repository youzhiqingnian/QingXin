package com.qingxin.medical.mine;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingxin.medical.R;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLPagerView;
import com.vlee78.android.vl.VLStatedButtonBar;
import com.vlee78.android.vl.VLUtils;

/**
 * Date 2018-03-03
 *
 * @author zhikuo1
 */

public class MyCollectedTabListFragment extends VLFragment {

    private View mRootView;

    private VLPagerView mFragmentPager;
    private VLStatedButtonBar mButtonBar;


    private boolean isDiary = false;

    public MyCollectedTabListFragment() {
    }

    public static MyCollectedTabListFragment newInstance() {
        return new MyCollectedTabListFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_collect_tab, container, false);
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
        mFragmentPager = mRootView.findViewById(R.id.mainPager);
        mButtonBar = mRootView.findViewById(R.id.mainBottomBar);
        mFragmentPager.setOffscreenPageLimit(2);
        //将2个fragment添加至activity中
        VLFragment[] fragments = new VLFragment[]{MyCollectedProductListFragment.newInstance(), MyCollectedDiaryListFragment.newInstance()};
        mFragmentPager.setFragmentPages(getVLActivity().getSupportFragmentManager(), fragments);
        //滑动切换界面 false不可滑动
        mFragmentPager.setScrollable(false);
        mButtonBar.setStatedButtonBarDelegate(new MainBottomBarDelegate(getVLActivity()));
        mFragmentPager.setPageChangeListener(position -> mButtonBar.setChecked(position));
        mButtonBar.setChecked(0);
    }

    private class MainBottomBarDelegate implements VLStatedButtonBar.VLStatedButtonBarDelegate {
        private Context mContext;

        MainBottomBarDelegate(Context context) {
            mContext = context;
        }

        @Override
        public void onStatedButtonBarCreated(VLStatedButtonBar buttonBar) {
            VLStatedButtonBar.VLStatedButton button = new VLStatedButtonBar.VLStatedButton(mContext);
            button.setStatedButtonDelegate(new MainBottomBarButtonDelegate(mContext.getResources().getString(R.string.mine_product)));
            LinearLayout.LayoutParams params = VLUtils.paramsLinear(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT);
            buttonBar.addStatedButton(button, params);

            button = new VLStatedButtonBar.VLStatedButton(mContext);
            button.setStatedButtonDelegate(new MainBottomBarButtonDelegate(mContext.getResources().getString(R.string.mine_diary)));
            LinearLayout.LayoutParams params1 = VLUtils.paramsLinear(VLUtils.WRAP_CONTENT, VLUtils.WRAP_CONTENT);
            buttonBar.addStatedButton(button, params1);
        }

        @Override
        public void onStatedButtonBarChanged(VLStatedButtonBar buttonBar, int position) {
            mFragmentPager.gotoPage(position, false);
        }
    }

    private class MainBottomBarButtonDelegate implements VLStatedButtonBar.VLStatedButton.VLStatedButtonDelegate {
        private TextView mCollectTypeTv;
        private String mText;

        MainBottomBarButtonDelegate(String text) {
            mText = text;
        }

        @Override
        public void onStatedButtonCreated(VLStatedButtonBar.VLStatedButton button, LayoutInflater inflater) {
            View view = inflater.inflate(R.layout.layout_mine_collect_text, button);
            mCollectTypeTv = view.findViewById(R.id.collectTypeTv);
            mCollectTypeTv.setText(mText);
        }

        @Override
        public void onStatedButtonChanged(VLStatedButtonBar.VLStatedButton button, VLStatedButtonBar.VLStatedButton.VLButtonState buttonState, int userState) {
            //设置底部按钮选中及未选择状态
            if (buttonState == VLStatedButtonBar.VLStatedButton.VLButtonState.StateChecked) {
                mCollectTypeTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_button));
                mCollectTypeTv.setTextColor(getResources().getColor(R.color.white));
            } else {
                mCollectTypeTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.mine_gray_button));
                mCollectTypeTv.setTextColor(getResources().getColor(R.color.text_color_origin_price));
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
