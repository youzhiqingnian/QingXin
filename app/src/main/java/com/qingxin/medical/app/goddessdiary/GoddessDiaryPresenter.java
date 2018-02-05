package com.qingxin.medical.app.goddessdiary;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static com.qingxin.medical.app.goddessdiary.DiaryListContract.*;

/**
 *
 * Date 2018-02-03
 * @author zhikuo1
 */
public class GoddessDiaryPresenter implements Presenter {

    @NonNull
    private final View mGoddessDiaryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    public GoddessDiaryPresenter(View goddessDiaryView) {
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
    public void getGoddessDiaryList(int limit, int skip) {
        mCompositeSubscription.add(NetRequestListManager.getGoddessDiary(limit, skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<GoddessDiaryBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mGoddessDiaryView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(ContentBean<GoddessDiaryBean> diary) {
                        mGoddessDiaryView.onSuccess(diary.getContent());
                    }
                })
        );
    }
}