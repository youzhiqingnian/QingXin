package com.qingxin.medical.mine;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.upload.UploadResult;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */
public class PersonalInformationDataContract {

    public interface View extends BaseView<Presenter> {
        void onModifyPersonalInfoSuccess(MemBean diaryPublishResult);
        void onSessionSuccess(com.qingxin.medical.base.MemBean memBean);
        void onUploadHeadSuccess(UploadResult uploadResultBean);
        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void headUpload(@NonNull DiaryPublishParams diaryPublishParams);

        void getSession();

        void saveMyInfo();
    }
}
