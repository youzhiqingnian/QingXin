package com.qingxin.medical.app.homepagetask;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class RecommendUserPresenter implements RecommendUserContract.Presenter {

    @NonNull
    private final RecommendUserContract.View mRecommendUserView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    RecommendUserPresenter(@NonNull RecommendUserContract.View recommendUserView) {
        mRecommendUserView = recommendUserView;
        mCompositeSubscription = new CompositeSubscription();
        mRecommendUserView.setPresenter(this);
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
    public void submitRecommendUser(String name, String mobile, String product, String inthos, String remark) {
        mCompositeSubscription.add(NetRequestListManager.submitRecommendUser(name, mobile, product, inthos, remark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<RecommendResultBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRecommendUserView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<RecommendResultBean> recommendResultBean) {
                        if(!HandErrorUtils.isError(recommendResultBean.getCode())){
                            mRecommendUserView.onSuccess(recommendResultBean.getContent());
                        }else{
                            mRecommendUserView.onError(new QingXinError(recommendResultBean.getMsg()));
                        }
                    }
                })
        );
    }
}
