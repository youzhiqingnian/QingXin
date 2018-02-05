package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.service.entity.Book;

/**
 *
 * Date 2018-02-05
 * @author zhikuo1
 */
public class HomePageTaskContract {

    interface View extends BaseView<Presenter> {

        void onSuccess(HomeBean homeBean);

        void onError(String result);

    }

    interface Presenter extends BasePresenter {

        void getHomeData(String banner_size, String product_size, String diary_size);

    }


}
