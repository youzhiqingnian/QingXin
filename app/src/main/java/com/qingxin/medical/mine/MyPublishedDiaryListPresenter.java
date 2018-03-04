package com.qingxin.medical.mine;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class MyPublishedDiaryListPresenter implements MyPublishedDiaryListContract.Presenter {

    @NonNull
    private final MyPublishedDiaryListContract.View mPublishedListryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public MyPublishedDiaryListPresenter(MyPublishedDiaryListContract.View vipListView) {
        mPublishedListryView = vipListView;
        mCompositeSubscription = new CompositeSubscription();
        mPublishedListryView.setPresenter(this);
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
    public void getMyPublishedDiaryList(String author, int limit, int skip) {
        mCompositeSubscription.add(NetRequestListManager.getMyPublishedDiaryList(author,limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<DiaryItemBean>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<DiaryItemBean>> vipList) {
                        if (!HandErrorUtils.isError(vipList.getCode())) {
                            mPublishedListryView.onSuccess(vipList.getContent());
                        } else {
                            ToastUtils.showToast(vipList.getMsg());
                        }
                    }
                })
        );
    }


}
