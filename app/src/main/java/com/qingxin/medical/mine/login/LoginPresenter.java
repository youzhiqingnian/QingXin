package com.qingxin.medical.mine.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.user.UserService;
import com.qingxin.medical.user.UserTokenBean;
import com.qingxin.medical.utils.HandErrorUtils;

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

    LoginPresenter(@NonNull LoginContract.LoginView view) {
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
    public void login(@NonNull String mobile, @NonNull String vcode) {
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(UserService.class).login(mobile, vcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<UserTokenBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<UserTokenBean> userTokenBeanContentBean) {
                        Log.i("登录",userTokenBeanContentBean.toString());
                        if (null != mLoginView) {
                            if(!HandErrorUtils.isError(userTokenBeanContentBean.getCode())){
                                mLoginView.onSuccess(userTokenBeanContentBean.getContent());
                            }else{
                                mLoginView.onError(new QingXinError(userTokenBeanContentBean.getMsg()));
                            }
                        }
                    }
                }));
    }

    @Override
    public void getSession() {
        mCompositeSubscription.add(NetRequestListManager.isChcekIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<MemBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<MemBean> memBean) {
                        if(!HandErrorUtils.isError(memBean.getCode())){
                            mLoginView.onSuccess(memBean.getContent());
                        }else{
                            mLoginView.onError(new QingXinError(memBean.getMsg()));
                        }
                    }
                })
        );
    }

    @Override
    public void getMoblieCode(@NonNull String mobile) {
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(UserService.class).getMobileCode(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean contentBean) {
                        if(HandErrorUtils.isError(contentBean.getCode())){
                            mLoginView.onError(new QingXinError(contentBean.getMsg()));
                        }else {
                            if (null != mLoginView){
                                mLoginView.onGetMobileCodeSuccess();
                            }
                        }
                    }
                })
        );
    }
}
