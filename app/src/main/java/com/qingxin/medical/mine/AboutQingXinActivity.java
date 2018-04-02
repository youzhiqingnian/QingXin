package com.qingxin.medical.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class AboutQingXinActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, AboutQingXinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_qingxin);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.about_us));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        TextView rightOwnTv = findViewById(R.id.rightOwnTv);
        rightOwnTv.setText(getResources().getString(R.string.right_owner));
    }
}
