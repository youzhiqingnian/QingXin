package com.qingxin.medical.app.vip;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-02-09
 *
 * @author zhikuo1
 */
public class VipDetailPresenter implements VipDetailContract.Presenter {

    @NonNull
    private final VipDetailContract.View mVipDetailView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public VipDetailPresenter(VipDetailContract.View vipDetailView) {
        mVipDetailView = vipDetailView;
        mCompositeSubscription = new CompositeSubscription();
        mVipDetailView.setPresenter(this);
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
    public void getVipDetail(String id) {
        mCompositeSubscription.add(NetRequestListManager.getVipDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<VipDetailBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mVipDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<VipDetailBean> vipDetail) {
                        mVipDetailView.onSuccess(vipDetail.getContent());
                    }
                })
        );
    }

    @Override
    public void collect(String id) {

        mCompositeSubscription.add(NetRequestListManager.collectVip(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<CollectBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mVipDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<CollectBean> collectBean) {
                        mVipDetailView.onSuccess(collectBean.getContent());
                    }
                })
        );

    }

    @Override
    public void book(String id) {
        mCompositeSubscription.add(NetRequestListManager.bookVip(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<AmountBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mVipDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<AmountBean> amountBean) {
                        Log.i("预定",amountBean.toString());
                        mVipDetailView.onSuccess(amountBean.getContent());
                    }
                })
        );
    }

}