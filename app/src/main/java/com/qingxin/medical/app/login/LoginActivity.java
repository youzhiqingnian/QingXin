package com.qingxin.medical.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.mine.login.LoginFragment;
import com.vlee78.android.vl.VLActivity;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class LoginActivity extends QingXinActivity {

    public static void startSelf(@NonNull VLActivity activity, VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static final int REQUEST_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.container, LoginFragment.newInstance(true)).commitAllowingStateLoss();
    }
}
