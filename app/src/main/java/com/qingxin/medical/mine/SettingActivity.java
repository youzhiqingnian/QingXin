package com.qingxin.medical.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLTitleBar;

/**
 * Date 2018-04-02
 *
 * @author zhikuo1
 */

public class SettingActivity extends QingXinActivity{


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
        aboutQingxinFl.setOnClickListener(view -> {
            AboutQingXinActivity.startSelf(this);
        });
        checkVersionUpdateFl.setOnClickListener(view -> {
            // TODO 检查版本更新
        });
        logoutTv.setOnClickListener(view -> {

        });
    }

}
