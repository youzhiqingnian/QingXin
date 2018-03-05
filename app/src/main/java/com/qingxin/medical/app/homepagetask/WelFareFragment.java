package com.qingxin.medical.app.homepagetask;

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
import com.qingxin.medical.mine.login.LoginFragment;
import com.vlee78.android.vl.VLFragment;

/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */

public class WelFareFragment extends VLFragment {

    public static WelFareFragment newInstance() {
        return new WelFareFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welfare, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;
        if (null == QingXinApplication.getInstance().getLoginUser()) {
            getFragmentManager().beginTransaction().add(R.id.containerFl, LoginFragment.newInstance(false)).commit();
        } else {
            getFragmentManager().beginTransaction().add(R.id.containerFl, WelFareServiceFragment.newInstance()).commit();
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, new IntentFilter(LoginFragment.LOGIN_ACTION));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WelFareServiceFragment welFareServiceFragment = WelFareServiceFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFl, welFareServiceFragment).commit();
        }
    };

}
