package com.qingxin.medical.home.districtsel;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ItemBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.utils.HandErrorUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-04-02
 * <p>
 * 获取严选详情
 *
 * @author zhikuo
 */
public class StrictSelDetailPresenter implements StrictSelDetailContract.Presenter {

    @NonNull
    private final StrictSelDetailContract.View mStrictSelView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    StrictSelDetailPresenter(@NonNull StrictSelDetailContract.View strictSelView) {
        mStrictSelView = strictSelView;
        mCompositeSubscription = new CompositeSubscription();
        mStrictSelView.setPresenter(this);
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
    public void getStrictSelDetail(String id) {
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(StrictSelService.class).getStrictSelDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ItemBean<StrictSelBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mStrictSelView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<ItemBean<StrictSelBean>> contentBean) {
                        if (!HandErrorUtils.isError(contentBean.getCode())) {
                            mStrictSelView.onSuccess(contentBean.getContent().getItem());
                        } else {
                            mStrictSelView.onError(new QingXinError(contentBean.getMsg()));
                        }
                    }
                })
        );
    }
}
