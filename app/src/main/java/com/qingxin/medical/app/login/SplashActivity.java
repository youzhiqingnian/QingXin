package com.qingxin.medical.app.login;

import android.os.Bundle;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.HomePageTaskActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;

/**
 * 启动页
 * Date 2018-02-05
 * @author zhikuo1
 */
public class SplashActivity extends QingXinActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        VLScheduler.instance.schedule(3000, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                // 进入主页
                HomePageTaskActivity.startSelf(SplashActivity.this, 0);
                finish();
            }
        });
    }
}
