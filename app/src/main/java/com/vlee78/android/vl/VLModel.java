package com.vlee78.android.vl;

import android.content.SharedPreferences;

public class VLModel implements VLMessageManager.VLMessageHandler {
    private VLMessageManager mMessageManager;
    private VLModelManager mModelManager;

    public VLModel() {
        mMessageManager = VLApplication.instance().getMessageManager();
        mModelManager = VLApplication.instance().getModelManager();
    }

    protected void onCreate() {
    }

    protected void onBeforeAfterCreate() {
    }

    protected void onAfterCreate() {
    }

    protected void onAfterAfterCreate() {

    }

    @SuppressWarnings("unchecked")
    public <T extends VLApplication> T getApplication() {
        VLApplication application = VLApplication.instance();
        return (T) application;
    }

    public VLApplication getConcretApplication() {
        return VLApplication.instance();
    }

    public SharedPreferences getSharedPreferences() {
        return getConcretApplication().getSharedPreferences();
    }

    public SharedPreferences getCachePreferences() {
        return getConcretApplication().getCachePreferences();
    }


    protected <T> T getModel(Class<T> modelClass) {
        return mModelManager.getModel(modelClass);
    }

    protected void registerMessageIds(int... msgIds) {
        mMessageManager.registerMessageHandler(this, msgIds);
    }

    @Override
    public void onMessage(int msgId, Object msgParam) {
    }

    protected void broadcastMessage(int msgId, Object msgParam, VLResHandler resHandler) {
        mMessageManager.broadcastMessage(msgId, msgParam, resHandler);
    }
}
