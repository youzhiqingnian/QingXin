package com.qingxin.medical.common;

import android.support.annotation.NonNull;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.utils.HandErrorUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018/4/8
 *
 * @author zhikuo
 */
public class DistrictPresenter implements DistrictContract.Presenter {

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    @NonNull
    private DistrictContract.View mDistrictView;

    public DistrictPresenter(@NonNull DistrictContract.View view) {
        this.mDistrictView = view;
        mDistrictView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getDistrictData(String level) {
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(DistrictService.class).getDistrictData(level)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContentBean<ListBean<DistrictItemData>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mDistrictView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<DistrictItemData>> contentBean) {
                        if (!HandErrorUtils.isError(contentBean.getCode())) {
                            mDistrictView.onSuccess(contentBean.getContent().getItems());
                        } else {
                            mDistrictView.onError(new QingXinError(contentBean.getMsg()));
                        }
                    }
                }));
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
}
