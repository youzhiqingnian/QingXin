package com.qingxin.medical.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.qingxin.medical.update.QingXinAppUpdateMode;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.user.SessionModel;
import com.qingxin.medical.user.UserModel;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLApplication;
import com.vlee78.android.vl.VLTitleBar;

/**
 * Date 2018-04-02
 *
 * @author zhikuo1
 */
public class SettingActivity extends QingXinActivity implements View.OnClickListener {

    public static final String LOGOUT_ACTION = "com.archie.action.LOGOUT_ACTION";

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.setting));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        FrameLayout aboutQingxinFl = findViewById(R.id.aboutQingxinFl);
        FrameLayout checkVersionUpdateFl = findViewById(R.id.checkVersionUpdateFl);
        TextView logoutTv = findViewById(R.id.logoutTv);
        aboutQingxinFl.setOnClickListener(this);
        logoutTv.setOnClickListener(this);
        checkVersionUpdateFl.setOnClickListener(this);
        TextView versionNameTv = findViewById(R.id.versionNameTv);
        versionNameTv.setText(String.format("当前版本 v%s", getVLApplication().appVersionName()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkVersionUpdateFl:
                getModel(QingXinAppUpdateMode.class).manualCheck();
                break;
            case R.id.aboutQingxinFl:
                AboutQingXinActivity.startSelf(this);
                break;
            case R.id.logoutTv:
                VLApplication.instance().getModel(UserModel.class).onLogout();
                VLApplication.instance().getModel(SessionModel.class).onLogout();
                Intent intent = new Intent(LOGOUT_ACTION);
                LocalBroadcastManager.getInstance(VLApplication.instance()).sendBroadcast(intent);
                ToastUtils.showToast(getResources().getString(R.string.logout_success));
                finish();
                break;
            default:
                break;
        }
    }
}
