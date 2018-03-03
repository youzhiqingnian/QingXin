package com.qingxin.medical.mine;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.vip.VipListBean;
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
public class MyCollectListPresenter implements MyCollectListContract.Presenter {

    @NonNull
    private final MyCollectListContract.View mCollectListryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public MyCollectListPresenter(MyCollectListContract.View vipListView) {
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
                .subscribe(new Observer<ContentBean<VipListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<VipListBean> vipList) {
                        if(!HandErrorUtils.isError(vipList.getCode())){
                            mCollectListryView.onSuccess(vipList.getContent());
                        }else{
                            ToastUtils.showToast(vipList.getMsg());
                        }
                    }
                })
        );
    }

    @Override
    public void getMyCollectDiaryList(int limit, int skip, String type, String actyp) {

    }
}
