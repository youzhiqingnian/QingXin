package com.qingxin.medical.base;

import com.qingxin.medical.fresco.QingXinFrescoModel;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.vlee78.android.vl.VLApplication;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLModelManager;

/**
 * QingXinApplication
 *
 * @author zhikuo
 */
public class QingXinApplication extends VLApplication {

    private static volatile QingXinApplication instance;

    /**
     * 获取当前应用单例
     */
    public static QingXinApplication getInstance() {
        VLDebug.Assert(instance != null);
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected void onConfigModels() {
        super.onConfigModels();
        VLModelManager manager = getModelManager();
        manager.registerModel(QingXinFrescoModel.class);
        manager.registerModel(RetrofitModel.class);
    }

    @Override
    protected void onConfigLogger() {
        super.onConfigLogger();
        VLDebug.configDebug(this, (appIsDebug() ? VLDebug.VLLogLevel.Debug : VLDebug.VLLogLevel.Error), 24 * 60 * 60 * 1000, 3 * 24 * 60 * 60 * 1000);
    }
}
