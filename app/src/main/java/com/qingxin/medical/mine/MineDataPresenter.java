package com.qingxin.medical.mine;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */
public class MineDataPresenter implements MineDataContract.Presenter {

    private MineDataContract.View mUploadHeadView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    MineDataPresenter(@NonNull MineDataContract.View uploadHeadView) {
        mUploadHeadView = uploadHeadView;
        mCompositeSubscription = new CompositeSubscription();
        mUploadHeadView.setPresenter(this);
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
    public void getSession() {
        mCompositeSubscription.add(NetRequestListManager.isChcekIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<com.qingxin.medical.base.MemBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mUploadHeadView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<com.qingxin.medical.base.MemBean> memBean) {
                        if (!HandErrorUtils.isError(memBean.getCode())) {
                            mUploadHeadView.onSuccess(memBean.getContent());
                        } else {
                            mUploadHeadView.onError(new QingXinError(memBean.getMsg()));
                        }
                    }
                })
        );
    }

}
