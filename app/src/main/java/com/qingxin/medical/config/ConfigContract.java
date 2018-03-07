package com.qingxin.medical.config;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */
public class ConfigContract {

    public interface View extends BaseView<Presenter> {
        void onSuccess(ContentBean<ConfigBean> diary);

        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void getConfigBean();
    }
}
