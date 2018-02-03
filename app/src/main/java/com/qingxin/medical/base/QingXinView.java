package com.qingxin.medical.base;

import com.qingxin.medical.app.BaseView;

/**
 * 基类View
 *
 * @author zhikuo
 */
public abstract class QingXinView<T> implements BaseView {

    public abstract void onSuccess(T t);

    public abstract void onError(String errorCode, String message);

    public void showLoading(){

    }

    public void hideLoading(){
        
    }
}
