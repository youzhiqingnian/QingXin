package com.qingxin.medical.mine.login;

import android.support.annotation.NonNull;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.user.UserService;
import com.qingxin.medical.user.UserTokenBean;
import com.vlee78.android.vl.VLApplication;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 登陆Presenter
 *
 * @author zhikuo
 */
@SuppressWarnings("unchecked")
public class LoginPresenter implements LoginContract.LoginPresenter {

    private LoginContract.LoginView mLoginView;
    private CompositeSubscription mCompositeSubscription;

    public LoginPresenter(@NonNull LoginContract.LoginView view) {
        this.mLoginView = view;
        mLoginView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
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
    public void login(String mobile, String vcode) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(UserService.class).login(mobile, vcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<UserTokenBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO
                    }

                    @Override
                    public void onNext(ContentBean<UserTokenBean> userTokenBeanContentBean) {
                        if (null != mLoginView) {
                            mLoginView.onSuccess(userTokenBeanContentBean.getContent());
                        }
                    }
                }));
    }
}
