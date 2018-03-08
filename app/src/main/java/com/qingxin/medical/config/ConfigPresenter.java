package com.qingxin.medical.config;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLApplication;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018/3/7
 *
 * @author zhikuo
 */
public class ConfigPresenter implements ConfigContract.Presenter {

    @NonNull
    private ConfigContract.View mConfigView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public ConfigPresenter(@NonNull ConfigContract.View configView) {
        mConfigView = configView;
        mCompositeSubscription = new CompositeSubscription();
        mConfigView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getConfigBean() {
        mCompositeSubscription.add(QingXinApplication.getInstance().getModel(RetrofitModel.class).getService(ConfigService.class).getConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ConfigBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ContentBean<ConfigBean> configBeanContentBean) {
                        if (!HandErrorUtils.isError(configBeanContentBean.getCode())) {
                            Log.i("全局配置的bean", configBeanContentBean.toString());
                            QingXinApplication.instance().getModel(ConfigModel.class).setConfigBean(configBeanContentBean.getContent());
                        }
                    }
                }));
    }
}
