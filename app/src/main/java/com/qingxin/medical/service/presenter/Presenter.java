package com.qingxin.medical.service.presenter;

import android.content.Intent;

import com.qingxin.medical.service.view.View;

/**
 * Created by user on 2018-01-22.
 */

public interface Presenter {

    void onCreate();

    void onStart();//暂时没用到

    void onStop();

    void pause();//暂时没用到

    void attachView(View view);

    void attachIncomingIntent(Intent intent);//暂时没用到


}
