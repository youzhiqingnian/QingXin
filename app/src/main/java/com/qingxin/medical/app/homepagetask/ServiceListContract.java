package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.home.ListBean;

/**
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class ServiceListContract {

    public interface View extends BaseView<Presenter> {

        void onSuccess(ListBean<ServiceBean> diary);

        void onError(String result);
    }

    public interface Presenter extends BasePresenter {
        void getExclusiveService(int limit, int skip);
    }
}
