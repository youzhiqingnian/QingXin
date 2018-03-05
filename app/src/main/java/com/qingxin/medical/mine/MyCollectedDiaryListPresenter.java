package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.goddessdiary.CollectBean;
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
public class MyCollectedDiaryListPresenter implements MyCollectedDiaryListContract.Presenter {

    @NonNull
    private final MyCollectedDiaryListContract.View mCollectListryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public MyCollectedDiaryListPresenter(MyCollectedDiaryListContract.View vipListView) {
        mCollectListryView = vipListView;
        mCompositeSubscription = new CompositeSubscription();
        mCollectListryView.setPresenter(this);
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
    public void getMyCollectDiaryList(int limit, int skip, String type, String actyp) {
        mCompositeSubscription.add(NetRequestListManager.getMyCollectDiaryList(limit, skip, type, actyp)
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
                            mCollectListryView.onSuccess(vipList.getContent());
                        } else {
                            ToastUtils.showToast(vipList.getMsg());
                        }
                    }
                })
        );
    }

    @Override
    public void cancelCollectDiary(String id) {
        mCompositeSubscription.add(NetRequestListManager.collectVip(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<CollectBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<CollectBean> collectBean) {
                        if(!HandErrorUtils.isError(collectBean.getCode())){
                            mCollectListryView.onSuccess(collectBean.getContent());
                        }else{
                            ToastUtils.showToast(collectBean.getMsg());
                        }
                    }
                })
        );
    }


}
