package com.qingxin.medical.app.homepagetask;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.app.homepagetask.model.HomeBanner;
import com.qingxin.medical.app.homepagetask.model.HomeProduct;
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
    private HomeBanner mBanner;
    private GoddessDiary mDiary;
    private HomeProduct mProduct;

    private boolean mIsDataMissing;

//    private Bundle mOutState;

    /**
     * Creates a presenter for the add/edit view.
     * @param homePageTaskView
      */
    public HomePageTaskPresenter(@NonNull Context context,
                                 HomePageTaskContract.View homePageTaskView
                            ) {
        mContext = context;
        mHomePageTaskView = checkNotNull(homePageTaskView);
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
//        getSearchBooks(mOutState.getString("name",""),"",0,1);
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


    /**
     * 获取banner图
     * @param limit
     */
    @Override
    public void getBannerList(String limit) {
        mCompositeSubscription.add(NetRequestListManager.getBannerList(mContext,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeBanner>() {
                    @Override
                    public void onCompleted() {
                        if (mBanner != null){
                            mHomePageTaskView.onSuccess(mBanner);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mHomePageTaskView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(HomeBanner banner) {
                        mBanner = banner;
                    }
                })
        );
    }

    /**
     * 获取女神日记列表
     * @param limit
     * @param skip
     */
    @Override
    public void getGoddessDiaryList(String limit, String skip) {

        mCompositeSubscription.add(NetRequestListManager.getGoddessDiary(mContext,limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoddessDiary>() {
                    @Override
                    public void onCompleted() {
                        if (mDiary != null){
                            mHomePageTaskView.onSuccess(mDiary);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mHomePageTaskView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(GoddessDiary diary) {
                        mDiary = diary;
                    }
                })
        );

    }

    @Override
    public void getHomeProductList(String limit, String skip, String order, String isvip) {
        mCompositeSubscription.add(NetRequestListManager.getHomeProductList(mContext,limit,skip,order,isvip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeProduct>() {
                    @Override
                    public void onCompleted() {
                        if (mProduct != null){
                            mHomePageTaskView.onSuccess(mProduct);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mHomePageTaskView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(HomeProduct product) {
                        mProduct = product;
                    }
                })
        );
    }


}
