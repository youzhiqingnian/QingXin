package com.qingxin.medical.app.goddessdiary;

import android.support.annotation.NonNull;
import com.qingxin.medical.service.manager.NetRequestListManager;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhikuo1 on 2018-02-02.
 */

public class GoddessDiaryDetailPresenter implements DiaryDetailContract.Presenter {

    @NonNull
    private final DiaryDetailContract.View mDiaryDetailView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;


    private GoddessDiaryDetailBean mDiaryDetailBean;

    private CollectBean mCollectBean;

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
                .subscribe(new Observer<GoddessDiaryDetailBean>() {
                    @Override
                    public void onCompleted() {
                        if (mDiaryDetailBean != null) {
                            mDiaryDetailView.onSuccess(mDiaryDetailBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mDiaryDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(GoddessDiaryDetailBean diaryDetailBean) {
                        mDiaryDetailBean = diaryDetailBean;
                    }
                })
        );
    }

    @Override
    public void collectDiary(String id) {

        mCompositeSubscription.add(NetRequestListManager.collectDiary(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectBean>() {
                    @Override
                    public void onCompleted() {
                        if (mCollectBean != null) {
                            mDiaryDetailView.onSuccess(mCollectBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mDiaryDetailView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(CollectBean collectBean) {
                        mCollectBean = collectBean;
                    }
                })
        );

    }

}
