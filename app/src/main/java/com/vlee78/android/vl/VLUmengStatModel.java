package com.vlee78.android.vl;

import android.Manifest.permission;
import android.app.Service;
import android.content.Context;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public abstract class VLUmengStatModel extends VLModel implements VLActivityManager.VLActivityListener {
    private static final String[] PERMISSIONS = new String[]
            {
                    permission.ACCESS_WIFI_STATE,
                    permission.ACCESS_NETWORK_STATE,
                    permission.READ_PHONE_STATE,
                    permission.INTERNET,
            };

    private boolean mEnabled;

    @Override
    protected void onCreate() {
        super.onCreate();
        mEnabled = true;
        onConfig();
        if (mEnabled) {
            VLDebug.Assert(VLUtils.androidCheckUsePermission(getConcretApplication(), PERMISSIONS));
            getConcretApplication().getActivityManager().addListener(this);
        }
    }

    protected void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    protected void setAppKey(String appKey) {
        VLDebug.Assert(appKey != null && appKey.length() > 0);
        if (mEnabled) AnalyticsConfig.setAppkey(appKey);
    }

    protected void setChannel(String channel) {
        VLDebug.Assert(channel != null && channel.length() > 0);
        if (mEnabled) AnalyticsConfig.setChannel(channel);
    }

    protected abstract void onConfig();

    @Override
    public void onManagerCreate(VLApplication application) {

        if (mEnabled) {
            MobclickAgent.updateOnlineConfig(application);
            MobclickAgent.openActivityDurationTrack(false);
        }
    }

    @Override
    public void onManagerDestroy(VLApplication application) {
//		if(mEnabled) MobclickAgent.onKillProcess(application);//kill的时候友盟库会崩溃
    }

    @Override
    public void onActivityCreate(VLActivity activity) {
    }

    @Override
    public void onActivityResume(VLActivity activity) {
        if (mEnabled) MobclickAgent.onResume(activity);
        VLDebug.logD("VLUmengStatModel onActivityResume" + activity.getClass().getName());

    }


    @Override
    public void onActivityPause(VLActivity activity) {
        if (mEnabled) MobclickAgent.onPause(activity);
        VLDebug.logD("VLUmengStatModel onActivityPause" + activity.getClass().getName());
    }

    public void onPageStart(String pageName) {
        if (mEnabled) {
            MobclickAgent.onPageStart(pageName);
        }
    }

    public void onPageEnd(String pageName) {
        if (mEnabled) {
            MobclickAgent.onPageEnd(pageName);
        }
    }

    public void startService(Service service, String startPageName) {
        MobclickAgent.onPageStart(startPageName); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(service);
    }

    public void stopService(Service service, String stopPageName) {
        MobclickAgent.onPageEnd(stopPageName); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(service);
    }

    @Override
    public void onActivityDestroy(VLActivity activity) {
    }

    public void reportEvent(Context context, String eventId) {
        if (mEnabled) MobclickAgent.onEvent(context, eventId);
    }

    public void reportEvent(Context context, String eventId, HashMap<String, String> map) {
        if (mEnabled) MobclickAgent.onEvent(context, eventId, map);
    }

    public void onEventValue(Context context, String eventId, HashMap<String, String> map, int value) {
        if (mEnabled) MobclickAgent.onEventValue(context, eventId, map, value);
    }

    public void setDebugMode(boolean isDebug) {
        if (mEnabled) MobclickAgent.setDebugMode(isDebug);
    }

}
