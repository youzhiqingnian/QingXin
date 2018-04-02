package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class MyCollectedProductListPresenter implements MyCollectedProductListContract.Presenter {

    @NonNull
    private final MyCollectedProductListContract.View mCollectListryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    MyCollectedProductListPresenter(@NonNull MyCollectedProductListContract.View vipListView) {
        mCollectListryView = vipListView;
        mCompositeSubscription = new CompositeSubscription();
        mCollectListryView.setPresenter(this);
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
    public void getMyCollectProductList(int limit, int skip, String type, String actyp) {
        mCompositeSubscription.add(NetRequestListManager.getMyBookedProductList(limit, skip, type, actyp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ProductListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCollectListryView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<ProductListBean> vipList) {
                        if (!HandErrorUtils.isError(vipList.getCode())) {
                            mCollectListryView.onSuccess(vipList.getContent());
                        } else {
                            mCollectListryView.onError(new QingXinError(vipList.getMsg()));
                        }
                    }
                })
        );
    }

    @Override
    public void cancelCollect(String id) {
        mCompositeSubscription.add(NetRequestListManager.collectVip(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<CollectBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCollectListryView.onCollectError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<CollectBean> collectBean) {
                        if (!HandErrorUtils.isError(collectBean.getCode())) {
                            mCollectListryView.onCollectSuccess(collectBean.getContent());
                        } else {
                            mCollectListryView.onCollectError(new QingXinError(collectBean.getMsg()));
                        }
                    }
                })
        );
    }
}
