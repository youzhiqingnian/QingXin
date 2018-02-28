package com.qingxin.medical.app.homepagetask;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * Date 2018-01-23
 * @author zhikuo1
 */

public class HomePageTaskPresenter implements HomePageTaskContract.Presenter{

    @NonNull
    private final HomePageTaskContract.View mHomePageTaskView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    HomePageTaskPresenter(HomePageTaskContract.View homePageTaskView) {
        mHomePageTaskView = checkNotNull(homePageTaskView);
        mCompositeSubscription = new CompositeSubscription();
        mHomePageTaskView.setPresenter(this);
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
    public void getHomeData(String banner_size, String product_size, String diary_size) {
        mCompositeSubscription.add(NetRequestListManager.getHomeData(banner_size,product_size,diary_size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<HomeBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mHomePageTaskView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<HomeBean> homeBean) {
                        if(!VLUtils.isError(homeBean.getCode())){
                            mHomePageTaskView.onSuccess(homeBean.getContent());
                        }else{
                            ToastUtils.showToast(homeBean.getMsg());
                        }

                    }
                })
        );
    }
}
