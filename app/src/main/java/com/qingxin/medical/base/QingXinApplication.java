package com.qingxin.medical.base;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.QingXinUmengModel;
import com.qingxin.medical.config.ConfigModel;
import com.qingxin.medical.fresco.QingXinFrescoModel;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyModel;
import com.qingxin.medical.map.GaoDeMapModel;
import com.qingxin.medical.map.LocationService;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.update.QingXinAppUpdateMode;
import com.qingxin.medical.user.SessionModel;
import com.qingxin.medical.user.User;
import com.qingxin.medical.user.UserModel;
import com.vlee78.android.vl.VLApplication;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLModelManager;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * QingXinApplication
 *
 * @author zhikuo
 */
public class QingXinApplication extends VLApplication {

    private static QingXinApplication instance;
    /**
     * Json序列化mapper
     */
    private final static ObjectMapper MAPPER = new ObjectMapper();
    private User mUser;

    private MemBean mSessionBean;

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
        this.mUser = loadPersistentUser();
        this.mSessionBean = loadPersistentSession();
    }

    @Override
    protected void onConfigModels() {
        super.onConfigModels();
        VLModelManager manager = getModelManager();
        manager.registerModel(GaoDeMapModel.class);
        manager.registerModel(QingXinFrescoModel.class);
        manager.registerModel(RetrofitModel.class);
        manager.registerModel(UserModel.class);
        manager.registerModel(MedicalBeautyModel.class);
        manager.registerModel(SessionModel.class);
        manager.registerModel(ConfigModel.class);
        manager.registerModel(QingXinUmengModel.class);
        manager.registerModel(QingXinAppUpdateMode.class);
    }

    @Override
    protected void onConfigLogger() {
        super.onConfigLogger();
        VLDebug.configDebug(this, (appIsDebug() ? VLDebug.VLLogLevel.Debug : VLDebug.VLLogLevel.Error), 24 * 60 * 60 * 1000, 3 * 24 * 60 * 60 * 1000);
    }


    private User loadPersistentUser() {
        String serializedUser = getSharedPreferences().getString(QingXinConstants.KEY_PREFERENCES_USER, null);
        if (serializedUser != null) {
            try {
                return MAPPER.readValue(serializedUser, User.class);
            } catch (IOException e) {
                VLDebug.logE("反序列化登录用户数据异常", e);
            }
        } else {
            VLDebug.logD("应用启动, 未发现用户登录");
        }
        return null;
    }

    private MemBean loadPersistentSession() {
        String serializedSession = getSharedPreferences().getString(QingXinConstants.KEY_PREFERENCES_SESSION, null);
        if (serializedSession != null) {
            try {
                return MAPPER.readValue(serializedSession, MemBean.class);
            } catch (IOException e) {
                VLDebug.logE("反序列化登录用户数据异常", e);
            }
        } else {
            VLDebug.logD("应用启动, 未发现用户登录");
        }
        return null;
    }

    /**
     * 保存当前应用已登录用户信息.
     */
    public void saveLoginUser(@NonNull User user) {
        // 持久化
        SharedPreferences.Editor sharedEditor = getSharedPreferences().edit();
        String serializedUser = null;
        try {
            serializedUser = MAPPER.writeValueAsString(user);
        } catch (IOException e) {
            VLDebug.logE("序列化登录用户数据异常", e);
        }
        VLDebug.Assert(serializedUser != null);
        sharedEditor.putString(QingXinConstants.KEY_PREFERENCES_USER, serializedUser);
        sharedEditor.apply();
        this.mUser = user;
    }

    public void saveMemberBean(@NonNull MemBean memBean) {
        SharedPreferences.Editor sharedEditor = getSharedPreferences().edit();
        String serializedUser = null;
        try {
            serializedUser = MAPPER.writeValueAsString(memBean);
        } catch (IOException e) {
            VLDebug.logE("序列化登录用户数据异常", e);
        }
        VLDebug.Assert(serializedUser != null);
        sharedEditor.putString(QingXinConstants.KEY_PREFERENCES_SESSION, serializedUser);
        sharedEditor.apply();
        this.mSessionBean = memBean;
    }


    /**
     * 删除当前应用保存的已登录用户信息.
     */
    public void removeLoginUser() {
        SharedPreferences.Editor sharedEditor = getSharedPreferences().edit();
        sharedEditor.remove(QingXinConstants.KEY_PREFERENCES_USER);
        sharedEditor.apply();
        this.mUser = null;
    }

    /**
     * 删除当前应用保存的已登录用户信息.
     */
    public void removeLoginSession() {
        SharedPreferences.Editor sharedEditor = getSharedPreferences().edit();
        sharedEditor.remove(QingXinConstants.KEY_PREFERENCES_SESSION);
        sharedEditor.apply();
        this.mSessionBean = null;
    }

    /**
     * 获取当前应用已登录用户信息. null表示尚未登录成功
     *
     * @return 用户信息
     */
    public User getLoginUser() {
        return this.mUser;
    }

    /**
     * 获取当前应用已登录用户信息. null表示尚未登录成功
     *
     * @return 用户信息
     */
    public MemBean getLoginSession() {
        return this.mSessionBean;
    }

    public LocationService getLocationService() {
        return this.getModel(GaoDeMapModel.class);
    }

    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (cacheDir != null) {
            if (!cacheDir.exists()) cacheDir.mkdirs();
            return cacheDir.getAbsolutePath();
        }
        return "";
    }
}
