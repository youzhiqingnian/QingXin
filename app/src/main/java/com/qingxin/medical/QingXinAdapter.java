package com.qingxin.medical;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qingxin.medical.base.QingXinFragment;
import com.vlee78.android.vl.VLFragment;

/**
 * 声优玩法界面ViewPager适配器
 * Date 2017/10/23 15:48
 *
 * @author zhikuo
 */
public class QingXinAdapter extends FragmentPagerAdapter {
    private VLFragment[] mPages;
    private String[] mTitles;

    public QingXinAdapter(@NonNull FragmentManager fm, @NonNull VLFragment[] pages, @NonNull String[] titles) {
        super(fm);
        this.mPages = pages;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages[position];
    }

    @Override
    public int getCount() {
        return mPages.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
