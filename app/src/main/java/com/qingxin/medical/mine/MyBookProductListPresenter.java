package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 *
 * Date 2018-02-05
 * @author zhikuo1
 */
public class MyBookProductListPresenter implements MyBookedProductListContract.Presenter {

    @NonNull
    private final MyBookedProductListContract.View mBookedProductListryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public MyBookProductListPresenter(MyBookedProductListContract.View vipListView) {
        mBookedProductListryView = vipListView;
        mCompositeSubscription = new CompositeSubscription();
        mBookedProductListryView.setPresenter(this);
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
    public void getMyBookedProductList(int limit, int skip, String type, String actyp) {
        mCompositeSubscription.add(NetRequestListManager.getMyBookedProductList(limit, skip, type, actyp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ProductListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<ProductListBean> vipList) {
                        if(!HandErrorUtils.isError(vipList.getCode())){
                            mBookedProductListryView.onSuccess(vipList.getContent());
                        }else{
                            ToastUtils.showToast(vipList.getMsg());
                        }
                    }
                })
        );
    }
}
