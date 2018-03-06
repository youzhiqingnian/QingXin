package com.qingxin.medical.app.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;

/**
 * 歆人专享列表
 * Date 2018-02-05
 *
 * @author zhikuo1
 */

public class VipListActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, VipListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.container,VipListFragment.newInstance(true)).commit();
    }
}
