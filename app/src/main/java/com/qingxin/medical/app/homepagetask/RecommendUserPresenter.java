package com.qingxin.medical.app.homepagetask;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
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
    public void submitRecommendUser(String name, String mobile, String product_id, String inthos, String remark) {
        mCompositeSubscription.add(NetRequestListManager.submitRecommendUser(name, mobile, product_id, inthos, remark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<RecommendResultBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mRecommendUserView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<RecommendResultBean> recommendResultBean) {
                        mRecommendUserView.onSuccess(recommendResultBean.getContent());
                    }
                })
        );
    }
}
