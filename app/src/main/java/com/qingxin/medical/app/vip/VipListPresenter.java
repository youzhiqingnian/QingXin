package com.qingxin.medical.app.vip;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static com.qingxin.medical.app.vip.VipListContract.*;
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

    public VipListPresenter(View vipListView) {
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
    public void getVipList(int limit, int skip, String isvip, String order) {
        mCompositeSubscription.add(NetRequestListManager.getVipList(limit, skip, isvip, order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<VipListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mVipListryView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<VipListBean> vipList) {
                        if(!VLUtils.isError(vipList.getCode())){
                            mVipListryView.onSuccess(vipList.getContent());
                        }else{
                            ToastUtils.showToast(vipList.getMsg());
                        }
                    }
                })
        );
    }

}
