package com.qingxin.medical.app.homepagetask;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 *
 * Date 2018-02-03
 * @author zhikuo1
 */
public class ExclusiveServicePresenter implements ServiceListContract.Presenter {

    @NonNull
    private final ServiceListContract.View mExclusiveServiceView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    ExclusiveServicePresenter(@NonNull ServiceListContract.View exclusiveServiceView) {
        mExclusiveServiceView = exclusiveServiceView;
        mCompositeSubscription = new CompositeSubscription();
        mExclusiveServiceView.setPresenter(this);
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
    public void getExclusiveService(int limit, int skip) {
        mCompositeSubscription.add(NetRequestListManager.getExclusiveService(limit, skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<ServiceBean>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mExclusiveServiceView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<ServiceBean>> diary) {
                        if(!HandErrorUtils.isError(diary.getCode())){
                            mExclusiveServiceView.onSuccess(diary.getContent());
                        }else{
                            ToastUtils.showToast(diary.getMsg());
                        }

                    }
                })
        );
    }
}
