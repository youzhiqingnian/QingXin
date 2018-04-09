package com.qingxin.medical.update;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLAppUpdateModel;
import com.vlee78.android.vl.VLAsyncHandler;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 版本更新
 */
public class QingXinAppUpdateMode extends VLAppUpdateModel {

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate() {
        super.onCreate();
        setAutoCheckDelay(5000);//建议更新的话,延迟5秒显示提示更新的框 根据启动页的停留时间设置相应的数值 防止在启动页弹框被销毁
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onAfterCreate() {
        super.onAfterCreate();
    }

    @Override
    protected void onProcessUpdateCheck(final VLAppUpdateRes appUpdateRes, final VLAsyncHandler<Object> asyncHandler) {
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(AppUpdateService.class).getReleaseVersion(getConcretApplication().appVersionName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ContentBean<AppUpdateBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != asyncHandler) {
                            asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(ContentBean<AppUpdateBean> appUpdateBeanContentBean) {
                        if (!HandErrorUtils.isError(appUpdateBeanContentBean.getCode())) {
                            AppUpdateBean appUpdateBean = appUpdateBeanContentBean.getContent();
                            if (null == appUpdateBean) {
                                final VLActivity progressActivity = getConcretApplication().getActivityManager().getCurrentActivity();
                                if (progressActivity != null) progressActivity.hideProgressDialog();
                                return;
                            }
                            AppUpdateBean.AppUpdateItem appUpdateItem = appUpdateBean.getItem();
                            String hasNew = appUpdateBean.getHasnew();
                            if ("Y".equals(hasNew)) {
                                if ("suggest".equals(appUpdateItem.getLevel())) {
                                    appUpdateRes.mCheckRes = VLAppUpdateRes.UpdateSugguest;
                                    appUpdateRes.mPromptTitle = "更新" + appUpdateItem.getVersion();
                                    appUpdateRes.mPromptMessageSuggest = appUpdateItem.getLog();
                                    appUpdateRes.mDownloadFilepath = getConcretApplication().appExternalHome() + "/" + getConcretApplication().appName() + ".apk";
                                    appUpdateRes.mDownloadUrl = appUpdateItem.getUrl();
                                } else if ("force".equals(appUpdateItem.getLevel())) {
                                    appUpdateRes.mCheckRes = VLAppUpdateRes.UpdateForced;
                                    appUpdateRes.mPromptTitle = "更新" + appUpdateItem.getVersion();
                                    appUpdateRes.mPromptMessageForced = appUpdateItem.getLog();
                                    appUpdateRes.mDownloadFilepath = getConcretApplication().appExternalHome() + "/" + getConcretApplication().appName() + ".apk";
                                    appUpdateRes.mDownloadUrl = appUpdateItem.getUrl();
                                }
                            } else {
                                appUpdateRes.mPromptTitle = "当前已是最新版";
                                appUpdateRes.mPromptMessageNoNeed = null == appUpdateItem ? "" : appUpdateItem.getLog();
                            }
                            if (null != asyncHandler) {
                                asyncHandler.handlerSuccess();
                            }
                        } else {
                            if (null != asyncHandler) {
                                asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, Integer.parseInt(appUpdateBeanContentBean.getCode()), appUpdateBeanContentBean.getMsg());
                            }
                        }
                    }
                }));
    }

    public void releaseComposite() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
