package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VLApplication extends Application implements VLMessageManager.VLMessageHandler {
    private static VLApplication gInstance;

    public static VLApplication instance() {
        if (gInstance == null) throw new RuntimeException();
        return gInstance;
    }

    public final static int TAG_DEFAULT_KEY = 0;

    private String mAppClassName;
    private String mAppPackageName;
    private String mAppName;
    private int mAppVersionCode;
    private String mAppVersionName;
    private String mAppExternalHome;
    private boolean mAppIsDebug;
    private boolean mAppIsMainProcess;
    private int mAppPid;
    private String mAppProcessName;
    private DisplayMetrics mAppDisplayMetrics;
    private Bundle mAppMetaData;
    private ApplicationInfo mAppInfo;

    private VLHttpClient mHttpClient;
    private VLActivityManager mActivityManager;
    private VLMessageManager mMessageManager;
    private VLModelManager mModelManager;
    private VLNotificationManager mNotificationManager;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences mCachePreferences;

    private static final String VL_PREFERENCES_NAME = "VL_PREFERENCES_NAME";
    private static final String VL_CACHE_NAME = "VL_CACHE_NAME";

    private final static Thread.UncaughtExceptionHandler DEFAULT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

    private List<VLApplicationListener> mApplicationListeners = new ArrayList<>();

    private SparseArray<Object> mTags;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gInstance = this;
        int pid = Process.myPid();
        String packageName = getPackageName();
        PackageInfo packageInfo = VLUtils.androidPackageInfo(this, 0);
        RunningAppProcessInfo processInfo = VLUtils.androidProcessInfo(this, pid);

        mAppClassName = ((Object) this).getClass().getName();
        mAppPid = pid;
        mAppPackageName = packageName;
        mAppName = VLUtils.stringLastToken(mAppPackageName, '.');
        mAppVersionCode = packageInfo.versionCode;
        mAppVersionName = packageInfo.versionName;
        mAppProcessName = processInfo.processName.substring(mAppPackageName.length());
        mAppExternalHome = VLUtils.androidExternalHome(this);
        mAppIsDebug = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        mAppIsMainProcess = (mAppProcessName.length() == 0);
        mAppDisplayMetrics = VLUtils.androidDisplayMetrics(this);
        mAppInfo = VLUtils.androidApplicationInfo(this, PackageManager.GET_META_DATA);
        mAppMetaData = mAppInfo.metaData;

        if (mAppProcessName.length() == 0)
            onConfigLogger();
        else
            onConfigLogger(mAppProcessName);
        Thread.setDefaultUncaughtExceptionHandler(new VLExceptionHandler());

        VLDebug.logI("============= App " + appPackageName() + "," + appVersionName() + "(" + appVersionCode() + "),Log:" + VLDebug.toDesc());
        VLDebug.logI("mAppClassName=" + mAppClassName + ",mAppPid=" + mAppPid + ",mAppPackageName=" + mAppPackageName
                + ",mAppName=" + mAppName + ",mAppIsDebug=" + mAppIsDebug + ",mAppVersionCode=" + mAppVersionCode
                + ",mAppVersionName=" + mAppVersionName + ",mAppProcessName=" + mAppProcessName + ",mAppExternalHome="
                + mAppExternalHome);
        VLDebug.logI(VLUtils.androidTelInfo(this));
        VLDebug.logI(VLUtils.androidBuildInfo());
        VLDebug.logV("Application create : " + mAppClassName + ".onCreate()");

        mHttpClient = new VLHttpClient();
        mModelManager = new VLModelManager();
        mMessageManager = new VLMessageManager();
        mActivityManager = new VLActivityManager();
        mNotificationManager = new VLNotificationManager(this);
        mSharedPreferences = this.getSharedPreferences(VL_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mCachePreferences = this.getSharedPreferences(VL_CACHE_NAME, Context.MODE_PRIVATE);

        if (mAppProcessName.length() == 0)
            onConfig3PartySdk();
        else
            onConfig3PartySdk(mAppProcessName);
        mActivityManager.managerCreate(this);

        if (mAppProcessName.length() == 0)
            onConfigModels();
        else
            onConfigModels(mAppProcessName);
        mModelManager.createAndInitModels();

        if (mAppProcessName.length() == 0)
            onConfigApplication();
        else
            onConfigApplication(mAppProcessName);

        registerActivityLifecycleCallbacks(new VLActivityLifecycleCallbacks(mApplicationListeners));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mAppIsMainProcess) return;
        VLDebug.logV("Application configurationchanged : " + mAppClassName + ".onConfigurationChanged()");
    }

    @SuppressLint("NewApi")
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (!mAppIsMainProcess) return;
        VLDebug.logV("Application trim memory : " + mAppClassName + ".onTrimMemory(level=" + level + ")");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (!mAppIsMainProcess) return;
        VLDebug.logV("Application terminate : " + mAppClassName + ".onTerminate()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!mAppIsMainProcess) return;
        VLDebug.logV("Application lowmemory : " + mAppClassName + ".onLowMemory()");
    }

    protected void onConfigLogger() {
    }

    protected void onConfig3PartySdk() {
    }

    protected void onConfigModels() {
    }

    protected void onConfigApplication() {
    }

    protected void onConfig3PartySdk(String processName) {
    }

    protected void onConfigApplication(String processName) {
    }

    protected void onConfigLogger(String processName) {
    }

    protected void onConfigModels(String processName) {
    }

    public void setTag(int k, Object tag) {
        VLDebug.Assert(k != TAG_DEFAULT_KEY && tag != null);
        if (mTags == null) mTags = new SparseArray<>();
        mTags.put(k, tag);
    }

    public void setTag(Object tag) {
        if (mTags == null) mTags = new SparseArray<>();
        mTags.put(TAG_DEFAULT_KEY, tag);
    }

    public void removeTag(int k) {
        if (mTags == null) return;
        mTags.remove(k);
    }

    public Object getTag(int k) {
        if (mTags == null) return null;
        return mTags.get(k);
    }

    public final int appPid() {
        return mAppPid;
    }

    public final String appProcessName() {
        return mAppProcessName;
    }

    public final String appPackageName() {
        return mAppPackageName;
    }

    public final String appName() {
        return mAppName;
    }

    public final int appVersionCode() {
        return mAppVersionCode;
    }

    public final String appVersionName() {
        return mAppVersionName;
    }

    public final String appExternalHome() {
        return mAppExternalHome;
    }

    public final boolean appIsDebug() {
        return mAppIsDebug;
    }

    public final boolean appIsRelease() {
        return !mAppIsDebug;
    }

    public final boolean appIsMainProcess() {
        return mAppIsMainProcess;
    }

    public int appDisplayWidth() {
        return mAppDisplayMetrics.widthPixels;
    }

    public int appDisplayHeight() {
        return mAppDisplayMetrics.heightPixels;
    }

    public float appDisplayDensity() {
        return mAppDisplayMetrics.density;
    }

    public Bundle appMetaData() {
        return mAppMetaData;
    }

    public ApplicationInfo appInfo() {
        return mAppInfo;
    }

    public boolean appForegroundRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        if (activityManager == null) return false;
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (taskInfos == null || taskInfos.size() == 0) return false;
        RunningTaskInfo taskInfo = taskInfos.get(0);
        if (taskInfo == null) return false;
        ComponentName componentName = taskInfo.topActivity;
        if (componentName == null) return false;
        String packageName = componentName.getPackageName();
        return packageName.equals(mAppPackageName);
    }

    public boolean appBackgroundRunning() {
        return !appForegroundRunning();
    }

    public void dumpTaskInfos() {
        VLDebug.logI("======== dump task infos ==============");
        ActivityManager activityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        if (activityManager != null) {
            @SuppressWarnings("deprecation")
            List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(10);
            if (taskInfos != null) {
                for (RunningTaskInfo taskInfo : taskInfos) {
                    if (taskInfo == null) continue;
                    VLDebug.logI("Task : baseActivity={" + taskInfo.baseActivity.getClassName() + "," + taskInfo.baseActivity.getPackageName() + "," + taskInfo.baseActivity.getShortClassName() + "}"
                            + ", id=" + taskInfo.id + ", numActivities=" + taskInfo.numActivities + ", numRunning=" + taskInfo.numRunning
                            + ", topActivity={" + taskInfo.topActivity.getClassName() + "," + taskInfo.topActivity.getPackageName() + "," + taskInfo.topActivity.getShortClassName() + "}");
                }
            }
        }
        VLDebug.logI("=======================================");
    }

    public String dumpDatabase() {
        try {
            File desDir = new File(mAppExternalHome + "/dump_" + System.currentTimeMillis());
            if (desDir.isFile()) VLUtils.E("desDir failed");
            if (!desDir.exists() && !desDir.mkdirs())
                VLUtils.E("make desDir failed");
            File srcDir = this.getDatabasePath("dummy.db");
            if (srcDir == null) VLUtils.E("get db dir failed");
            srcDir = srcDir.getParentFile();
            if (srcDir == null) VLUtils.E("get db parent failed");
            for (File srcFile : srcDir.listFiles()) {
                if (srcFile.isDirectory()) continue;
                File desFile = new File(desDir.getAbsoluteFile() + "/" + srcFile.getName());
                if (!desFile.createNewFile()) VLUtils.E("create dump file failed");
                VLUtils.copyFileByChannel(srcFile, desFile);
            }
            return desDir.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public SharedPreferences getCachePreferences() {
        return mCachePreferences;
    }


    public VLActivityManager getActivityManager() {
        return mActivityManager;
    }

    public VLMessageManager getMessageManager() {
        return mMessageManager;
    }

    public void registerMessageIds(int... msgIds) {
        mMessageManager.registerMessageHandler(this, msgIds);
    }

    public void registerApplicationListener(VLApplicationListener applicationListener) {
        mApplicationListeners.add(applicationListener);
    }

    @Override
    public void onMessage(int msgId, Object msgParam) {
    }

    public void broadcastMessage(int msgId, Object msgParam, VLResHandler resHandler) {
        mMessageManager.broadcastMessage(msgId, msgParam, resHandler);
    }

    public VLHttpClient getHttpClient() {
        return mHttpClient;
    }

    public VLModelManager getModelManager() {
        return mModelManager;
    }

    public VLNotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    public <T> T getModel(Class<T> modelClass) {
        return mModelManager.getModel(modelClass);
    }

    public void exit() {
        mActivityManager.managerDestroy(this);
    }

    public static class VLExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try {
//                String s = VLDebug.dumpExceptionStackTrace(thread, ex);
//                Log.e("Frogs", s);
                VLDebug.logEx(thread, ex);
                VLApplication.instance().exit();
                Process.killProcess(Process.myPid());
            } catch (Throwable ex1) {
                DEFAULT_EXCEPTION_HANDLER.uncaughtException(thread, ex);
            }

        }
    }

    public interface VLApplicationListener {

        void onApplicationToBackend();

        void onApplicationToFront();

    }

    public static class VLActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        private static int COUNT = 0;

        private List<VLApplicationListener> mApplicationListeners;

        private VLActivityLifecycleCallbacks(List<VLApplicationListener> applicationListeners) {
            this.mApplicationListeners = applicationListeners;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (COUNT == 0) {
                if (mApplicationListeners != null) {
                    for (VLApplicationListener listener : mApplicationListeners) {
                        listener.onApplicationToFront();
                    }
                }
            }
            COUNT++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            COUNT--;
            if (COUNT == 0) {
                if (mApplicationListeners != null)
                    for (VLApplicationListener listener : mApplicationListeners) {
                        listener.onApplicationToBackend();
                    }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    public void startActivityNewTask(Class<? extends VLActivity> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
