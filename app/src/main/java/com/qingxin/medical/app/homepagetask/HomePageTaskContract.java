package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.app.homepagetask.model.HomeBanner;
import com.qingxin.medical.app.homepagetask.model.HomeProduct;
import com.qingxin.medical.service.entity.Book;

/**
 * Created by user on 2018-01-23.
 */

public class HomePageTaskContract {

    interface View extends BaseView<Presenter> {

        void onSuccess(Book mBook);

        void onSuccess(HomeBanner mBanner);

        void onSuccess(GoddessDiary mDiary);

        void onSuccess(HomeProduct mProduct);

        void onError(String result);

    }

    interface Presenter extends BasePresenter {

        void populateTask();

        void getSearchBooks(String name,String tag,int start,int count);

        void getBannerList(String limit);

        void getGoddessDiaryList(String limit,String skip);

        void getHomeProductList(String limit,String skip,String order,String isvip);

        boolean isDataMissing();

    }


}
