package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishResult;
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.upload.UploadResult;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public class MineDataContract {

    public interface View extends BaseView<Presenter> {

        void onSuccess(MemBean diaryPublishResult);
        void onSuccess(com.qingxin.medical.base.MemBean memBean);
        void onSuccess(UploadResult uploadResultBean);

        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void headUpload(@NonNull DiaryPublishParams diaryPublishParams);

        void updateDiary(@NonNull DiaryPublishParams diaryPublishParams);


        void getSession();
    }

}
