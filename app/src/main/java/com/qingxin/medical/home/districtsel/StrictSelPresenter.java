package com.qingxin.medical.home.districtsel;

import android.support.annotation.NonNull;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLApplication;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class StrictSelPresenter implements StrictSelContract.Presenter {

    @NonNull
    private final StrictSelContract.View mStrictSelView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    StrictSelPresenter(@NonNull StrictSelContract.View goddessDiaryView) {
        mStrictSelView = goddessDiaryView;
        mCompositeSubscription = new CompositeSubscription();
        mStrictSelView.setPresenter(this);
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
    public void getStrictSelList(String type, int limit, int skip) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(StrictSelService.class).getStrictSelList(type, limit, skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<StrictSelBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mStrictSelView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<StrictSelBean>> contentBean) {
                        if (!HandErrorUtils.isError(contentBean.getCode())) {
                            mStrictSelView.onSuccess(contentBean.getContent());
                        } else {
                            mStrictSelView.onError(new QingXinError(contentBean.getMsg()));
                        }
                    }
                })
        );
    }
}
