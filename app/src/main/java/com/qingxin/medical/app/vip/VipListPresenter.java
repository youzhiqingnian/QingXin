package com.qingxin.medical.app.vip;

import android.support.annotation.NonNull;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.qingxin.medical.app.vip.VipListContract.View;
/**
 *
 * Date 2018-02-05
 * @author zhikuo1
 */
public class VipListPresenter implements VipListContract.Presenter {

    @NonNull
    private final View mVipListryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    VipListPresenter(@NonNull View vipListView) {
        mVipListryView = vipListView;
        mCompositeSubscription = new CompositeSubscription();
        mVipListryView.setPresenter(this);
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
    public void getVipList(int limit, int skip, String search) {
        mCompositeSubscription.add(NetRequestListManager.getVipList(limit, skip, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<VipListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mVipListryView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<VipListBean> vipList) {
                        if(!HandErrorUtils.isError(vipList.getCode())){
                            mVipListryView.onSuccess(vipList.getContent());
                        }else{
                            mVipListryView.onError(new QingXinError(vipList.getMsg()));
                        }
                    }
                })
        );
    }
}
