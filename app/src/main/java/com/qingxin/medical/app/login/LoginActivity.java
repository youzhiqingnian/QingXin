package com.qingxin.medical.app.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.mine.login.LoginFragment;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLUtils;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class LoginActivity extends QingXinActivity implements LoginFragment.OnLoginSuccessListener {

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

    @Override
    public void loginSuccess() {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (VLUtils.isShouldHideInput(getCurrentFocus(), ev)) {
                hideKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
