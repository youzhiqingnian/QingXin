package com.qingxin.medical.app.goddessdiary;

import android.support.annotation.NonNull;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class GoddessDiaryPresenter implements DiaryListContract.Presenter {

    @NonNull
    private final DiaryListContract.View mGoddessDiaryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    GoddessDiaryPresenter(@NonNull DiaryListContract.View goddessDiaryView) {
        mGoddessDiaryView = goddessDiaryView;
        mCompositeSubscription = new CompositeSubscription();
        mGoddessDiaryView.setPresenter(this);
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
    public void getGoddessDiaryList(int limit, int skip, String search) {
        mCompositeSubscription.add(NetRequestListManager.getGoddessDiary(limit, skip, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<DiaryItemBean>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mGoddessDiaryView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<DiaryItemBean>> diary) {
                        if (!HandErrorUtils.isError(diary.getCode())) {
                            mGoddessDiaryView.onSuccess(diary.getContent());
                        } else {
                            mGoddessDiaryView.onError(new QingXinError(diary.getMsg()));
                        }
                    }
                })
        );
    }
}
