package com.qingxin.medical.mine.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.user.UserService;
import com.qingxin.medical.user.UserTokenBean;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
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
    public void login(@NonNull String mobile, @NonNull String vcode) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(UserService.class).login(mobile, vcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<UserTokenBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<UserTokenBean> userTokenBeanContentBean) {
                        Log.i("登录",userTokenBeanContentBean.toString());
                        if (null != mLoginView) {
                            if(!HandErrorUtils.isError(userTokenBeanContentBean.getCode())){
                                mLoginView.onSuccess(userTokenBeanContentBean.getContent());
                            }else{
                                ToastUtils.showToast(userTokenBeanContentBean.getMsg());
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
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<MemBean> memBean) {
                        if(!HandErrorUtils.isError(memBean.getCode())){
                            mLoginView.onSuccess(memBean.getContent());
                        }else{
                            ToastUtils.showToast(memBean.getMsg());
                        }

                    }
                })
        );
    }

    @Override
    public void getMoblieCode(@NonNull String mobile) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(UserService.class).getMobileCode(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean contentBean) {
                        if(HandErrorUtils.isError(contentBean.getCode())){
                            ToastUtils.showToast(contentBean.getMsg());
                        }else {
                            ToastUtils.showToast("验证码发送成功，请查收");
                        }
                    }
                })
        );
    }
}
