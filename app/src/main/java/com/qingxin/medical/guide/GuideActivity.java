package com.qingxin.medical.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLPagerView;

/**
 * Date 2018/3/6
 *
 * @author zhikuo
 */

public class GuideActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        VLPagerView pagerView = findViewById(R.id.pagerView);
        VLFragment[] fragments = new VLFragment[4];
        for (int i = 0; i < 4; i++) {
            fragments[i] = GuideFragment.newInstance(i + 1);
        }
        pagerView.setFragmentPages(getSupportFragmentManager(), fragments);
    }
}
