package com.qingxin.medical.app.goddessdiary;

import android.support.annotation.NonNull;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *
 * Date 2018-02-02
 * @author zhikuo1
 */

public class GoddessDiaryDetailPresenter implements DiaryDetailContract.Presenter {

    @NonNull
    private final DiaryDetailContract.View mDiaryDetailView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public GoddessDiaryDetailPresenter(DiaryDetailContract.View diaryDetailView) {
        mDiaryDetailView = diaryDetailView;
        mCompositeSubscription = new CompositeSubscription();
        mDiaryDetailView.setPresenter(this);
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
    public void getGoddessDiaryDetail(String id) {
        mCompositeSubscription.add(NetRequestListManager.getGoddessDiaryDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<GoddessDiaryDetailBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mDiaryDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<GoddessDiaryDetailBean> diaryDetailBean) {
                        mDiaryDetailView.onSuccess(diaryDetailBean.getContent());
                    }
                })
        );
    }

    @Override
    public void collectDiary(String id) {

        mCompositeSubscription.add(NetRequestListManager.collectDiary(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<CollectBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mDiaryDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<CollectBean> collectBean) {
                        mDiaryDetailView.onSuccess(collectBean.getContent());
                    }
                })
        );

    }

}
