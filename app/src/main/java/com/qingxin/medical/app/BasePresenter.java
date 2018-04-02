package com.qingxin.medical.app;

import com.vlee78.android.vl.VLApplication;

/**
 * Date 2018-01-23
 *
 * @author zhikuo1
 */
public interface BasePresenter {

    void subscribe();

    void unsubscribe();

    default <T> T getModel(Class<T> modelClass) {
        return VLApplication.instance().getModel(modelClass);
    }
}
