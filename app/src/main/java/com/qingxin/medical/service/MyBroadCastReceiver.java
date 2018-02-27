package com.qingxin.medical.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Date 2018-02-27
 *
 * @author zhikuo1
 */


public class MyBroadCastReceiver extends BroadcastReceiver {

    private OnReceiverCallbackListener mReceiverListener;

    /**
     * 设置广播回调监听
     */
    public void setReceiverListener(OnReceiverCallbackListener receiverListener) {
        this.mReceiverListener=receiverListener;
    }

    /**
     * 实现一个广播回调监听，只有只类才能实现，用于activity之间刷新界面数据的时候用
     *
     * <p></p>
     * 上午11:46:10
     *
     * @author ZH-SW-Mengyf
     * @version 1.0.0
     */
    public interface OnReceiverCallbackListener{

        /**
         * 广播刷新数据的时候使用
         * @param intent
         */
        void receiverUpdata(Intent intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mReceiverListener!=null) {
            mReceiverListener.receiverUpdata(intent);
        }
    }
}
