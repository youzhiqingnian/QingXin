package com.qingxin.medical.app.goddessdiary;

import android.content.Context;
import android.support.annotation.NonNull;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.service.manager.NetRequestListManager;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhikuo1 on 2018-01-31.
 */

public class GoddessDiaryPresenter implements GoddessDiaryContract.Presenter {

    @NonNull
    private Context mContext;

    @NonNull
    private final GoddessDiaryContract.View mGoddessDiaryView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    private GoddessDiary mDiary;

    GoddessDiaryPresenter(@NonNull Context context, GoddessDiaryContract.View goddessDiaryView){
        mContext = context;
        mGoddessDiaryView = goddessDiaryView;
        mCompositeSubscription = new CompositeSubscription();
        mGoddessDiaryView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void populateTask() {

    }

    @Override
    public void getGoddessDiaryList(String limit, String skip) {
        mCompositeSubscription.add(NetRequestListManager.getGoddessDiary(limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoddessDiary>() {
                    @Override
                    public void onCompleted() {
                        if (mDiary != null){
                            mGoddessDiaryView.onSuccess(mDiary);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mGoddessDiaryView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(GoddessDiary diary) {
                        mDiary = diary;
                    }
                })
        );
    }

    @Override
    public boolean isDataMissing() {
        return false;
    }
}
