package com.qingxin.medical.app.goddessdiary;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;

/**
 *
 * Date 2018-01-31
 * @author zhikuo1
 */
public class DiaryListContract {

    public interface View extends BaseView<Presenter> {
        void onSuccess(ListBean<DiaryItemBean> diary);
        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void getGoddessDiaryList(int limit,int skip,String search);
    }
}
