package com.qingxin.medical.app.homepagetask;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.qingxin.medical.service.entity.Book;
import com.qingxin.medical.service.manager.NetRequestListManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by user on 2018-01-23.
 */

public class HomePageTaskPresenter implements HomePageTaskContract.Presenter{

    @NonNull
    private final HomePageTaskContract.View mHomePageTaskView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    @NonNull
    private Context mContext;

    private Book mBook;

    private boolean mIsDataMissing;

    private Bundle mOutState;

    /**
     * Creates a presenter for the add/edit view.
     * @param homePageTaskView
      */
    public HomePageTaskPresenter(Bundle outState,@NonNull Context context,
                                 HomePageTaskContract.View homePageTaskView
                            ) {
        mContext = context;
        mOutState = outState;
        mHomePageTaskView = checkNotNull(homePageTaskView);
        mIsDataMissing = outState.getBoolean(HomePageTaskActivity.SHOULD_LOAD_DATA_FROM_REPO_KEY,false);
        mCompositeSubscription = new CompositeSubscription();
        mHomePageTaskView.setPresenter(this);
    }


    @Override
    public void subscribe() {
        if(isDataMissing()){
            populateTask();
        }
    }

    @Override
    public void unsubscribe() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }


    @Override
    public void populateTask() {
        getSearchBooks(mOutState.getString("name",""),"",0,1);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void getSearchBooks(String name, String tag, int start, int count) {
        mCompositeSubscription.add(NetRequestListManager.getSearchBooks(mContext,name,tag,start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onCompleted() {
                        if (mBook != null){
                            mHomePageTaskView.onSuccess(mBook);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mHomePageTaskView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(Book book) {
                        mBook = book;
                    }
                })
        );
    }




}
