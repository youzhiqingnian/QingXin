package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.service.entity.Book;

/**
 * Created by user on 2018-01-23.
 */

public class HomePageTaskContract {

    interface View extends BaseView<Presenter> {

        void onSuccess(Book mBook);

        void onError(String result);

        void setTitle(String title);

    }

    interface Presenter extends BasePresenter {

        void populateTask();

        void getSearchBooks(String name,String tag,int start,int count);

        boolean isDataMissing();

    }


}
