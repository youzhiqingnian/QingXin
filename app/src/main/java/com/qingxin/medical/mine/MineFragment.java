package com.qingxin.medical.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.mine.login.LoginFragment;

/**
 * 首页我的界面
 *
 * @author zhikuo
 */
public class MineFragment extends QingXinFragment {

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;
        if (null == QingXinApplication.getInstance().getLoginUser()) {
            LoginFragment mLoginFragment = LoginFragment.newInstance(false);
            getFragmentManager().beginTransaction().add(R.id.container, mLoginFragment).commit();
        } else {
            getFragmentManager().beginTransaction().add(R.id.container, MineDataFragment.newInstance()).commit();
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, new IntentFilter(LoginFragment.LOGIN_ACTION));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MineDataFragment mineDataFragment = MineDataFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, mineDataFragment).commit();
        }
    };
}
