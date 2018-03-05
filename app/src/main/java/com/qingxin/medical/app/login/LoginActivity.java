package com.qingxin.medical.app.login;

import android.os.Bundle;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.mine.login.LoginFragment;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class LoginActivity extends QingXinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.container, LoginFragment.newInstance(true)).commitAllowingStateLoss();
    }
}
