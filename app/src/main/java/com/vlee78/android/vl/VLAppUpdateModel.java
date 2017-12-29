package com.vlee78.android.vl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Html;

import java.io.File;

public abstract class VLAppUpdateModel extends VLModel {
    public class VLAppUpdateRes {
        public static final int UpdateNoNeed = 0;
        public static final int UpdateSugguest = 1;
        public static final int UpdateForced = 2;

        public int mCheckRes = UpdateNoNeed;
        public int mNewVersionCode = 0;
        public String mPromptTitle = null;
        public String mPromptProgress = null;
        public String mPromptMessageError = null;
        public String mPromptMessageNoNeed = null;
        public String mPromptMessageSuggest = null;
        public String mPromptMessageForced = null;
        public String mPromptLabelUpdate = null;
        public String mPromptLabelIgnore = null;
        public String mPromptLabelBackgroundDownload = null;
        public String mPromptLabelCancelDownload = null;
        public String mPromptDownloadTitle = null;
        public String mPromptDownloadProgress = null;
        public String mDownloadUrl = null;
        public String mDownloadFilepath = null;
        public String mDownloadFileMd5 = null;
        private int mHttpTaskId = 0;
    }

    public interface VLAppUpdateDelegate {
        void onUpdateCheck(VLAppUpdateRes appUpdateRes, final VLResHandler resHandler);

        int onGetAutoCheckDelayInMs();//自动启动检查更新,返回0代表不自动启动检查
    }

    private VLAppUpdateRes mAppUpdateRes;
    private boolean mAppUpdatChecked;
    private int mAutoCheckDelayMs;
    private long mManualSuggestMuteMs;
    private boolean mIsUpdateForced;
    private int mSuggestPromptDelayMs;
    private volatile boolean mIsCheckUpdating = false;
    private volatile boolean mIsDoUpdateDownloading = false;


    public boolean isUpdateForced() {
        return mIsUpdateForced;
    }

    private static final String KEY_APP_UPDATE_LAST_REFUSE_MS = "KEY_APP_UPDATE_LAST_REFUSE_MS";

    @Override
    protected void onCreate() {
        super.onCreate();
        mAppUpdateRes = null;
        mAppUpdatChecked = false;
        mAutoCheckDelayMs = 0;
        mManualSuggestMuteMs = 0;
        mIsUpdateForced = false;
        mSuggestPromptDelayMs = 0;
    }

    protected void setSuggestPromptDelayMs(int suggestPromptDelayMs) {
        mSuggestPromptDelayMs = suggestPromptDelayMs;
    }

    protected void setAutoCheckDelay(int autoCheckDelayMs) {
        mAutoCheckDelayMs = autoCheckDelayMs;
    }

    protected void setManualSuggestMuteMs(long manualSuggestMuteMs) {
        if (manualSuggestMuteMs <= 0) manualSuggestMuteMs = 0;
        mManualSuggestMuteMs = manualSuggestMuteMs;
    }

    @Override
    protected void onAfterAfterCreate() {
        super.onAfterCreate();
        if (mAutoCheckDelayMs > 0) {
            VLScheduler.instance.schedule(mAutoCheckDelayMs, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mAppUpdatChecked) return;//已经update检查过,很可能是手动,不触发自动启动检查了
                    mAppUpdatChecked = true;
                    check(false);
                }
            });
        }
    }

    protected void onSetupUpdateCheck(VLAppUpdateRes appUpdateRes) {
    }

    protected abstract void onProcessUpdateCheck(VLAppUpdateRes appUpdateRes, final VLAsyncHandler<Object> asyncHandler);

    public void manualCheck() {
        mAppUpdatChecked = true;
        check(true);
    }

    private synchronized void check(boolean manually) {
        final VLActivity progressActivity = getConcretApplication().getActivityManager().getCurrentActivity();
        if (progressActivity == null) return;

        if (mIsDoUpdateDownloading) {//有后台下载更新中, 显示主截面
            if (mAppUpdateRes.mHttpTaskId > 0) doUpdateDownload();
            return;
        }
        if (mIsCheckUpdating) {//如果正在检测则不重复检测
            if (manually) {
                String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptTitle, "检查更新");
                String progressMsg = VLUtils.stringNullDefault(mAppUpdateRes.mPromptProgress, "正在联网检查版本更新...");
                progressActivity.showProgressDialog(title, progressMsg, false);//手动检查更新才显示菊花
            }
            return;
        }
        mAppUpdateRes = new VLAppUpdateRes();
        onSetupUpdateCheck(mAppUpdateRes);
        final boolean manualFlag = manually;
        if (manualFlag) {
            String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptTitle, "检查更新");
            String progressMsg = VLUtils.stringNullDefault(mAppUpdateRes.mPromptProgress, "正在联网检查版本更新...");
            progressActivity.showProgressDialog(title, progressMsg, false);//手动检查更新才显示菊花
        }
        mIsCheckUpdating = true;
        onProcessUpdateCheck(mAppUpdateRes, new VLAsyncHandler<Object>(null, VLScheduler.THREAD_BG_NORMAL) {
            @Override
            protected void handler(boolean succeed) {
                mIsCheckUpdating = false;
                mIsUpdateForced = false;
                if (manualFlag) progressActivity.hideProgressDialog();//显示菊花的activity
                final VLActivity currentActivity = VLApplication.instance().getActivityManager().getCurrentActivity();
                if (!succeed || currentActivity == null || mAppUpdateRes.mCheckRes < VLAppUpdateRes.UpdateNoNeed || mAppUpdateRes.mCheckRes > VLAppUpdateRes.UpdateForced) {//非正常情况,中止,do nothing
                    String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptTitle, "检查更新");
                    String prompt = VLUtils.stringNullDefault(mAppUpdateRes.mPromptMessageError, "网络错误，请检查网络后重试");
                    if (manualFlag && currentActivity != null)
                        currentActivity.showAlertDialog(title, prompt, true, null);
                    mAppUpdateRes = null;
                    return;
                }

                if (mAppUpdateRes.mCheckRes == VLAppUpdateRes.UpdateNoNeed) {//无需更新, 手动的话, 需要显示提示框
                    String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptTitle, "检查更新");
                    String prompt = VLUtils.stringNullDefault(mAppUpdateRes.mPromptMessageNoNeed, "当前已为最新版本");
                    if (manualFlag)
                        currentActivity.showAlertDialog(title, prompt, false, null);
                    mAppUpdateRes = null;
                } else if (mAppUpdateRes.mCheckRes == VLAppUpdateRes.UpdateSugguest) {//建议更新
                    if (!manualFlag && mManualSuggestMuteMs > 0) {//非手动更新的话, 一天之内不重复提示有更新的对话框
                        long lastRefustTs = getLastRefuseTS();
                        long now = System.currentTimeMillis();
                        if (now - lastRefustTs < mManualSuggestMuteMs) {
                            mAppUpdateRes = null;
                            return;
                        }
                    }
                    VLScheduler.instance.schedule(mSuggestPromptDelayMs, VLScheduler.THREAD_MAIN, new VLBlock() {
                        @Override
                        protected void process(boolean canceled) {
                            String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptTitle, "检查更新");
                            String prompt = VLUtils.stringNullDefault(mAppUpdateRes.mPromptMessageSuggest, "发现新版本，建议立即升级到最新版本");
                            String labelUpdate = VLUtils.stringNullDefault(mAppUpdateRes.mPromptLabelUpdate, "立即更新");
                            String labelIgnore = VLUtils.stringNullDefault(mAppUpdateRes.mPromptLabelIgnore, "暂不更新");

                            VLActivity curActivity = VLApplication.instance().getActivityManager().getCurrentActivity();
                            if (curActivity == null) return;
                            curActivity.showOkCancelDialog(title, prompt, labelUpdate, labelIgnore, false, new VLResHandler(VLScheduler.THREAD_BG_NORMAL) {//弹出对话框
                                @Override
                                protected void handler(boolean succeed) {
                                    if (!succeed) {//暂不更新
                                        saveLastRefuseTS();
                                        mAppUpdateRes = null;
                                        return;
                                    }
                                    doUpdateDownload();//启动下载
                                }
                            });
                        }
                    });
                } else if (mAppUpdateRes.mCheckRes == VLAppUpdateRes.UpdateForced) {//强制更新,显示只有确定按钮的对话框
                    mIsUpdateForced = true;
                    String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptTitle, "检查更新");
                    String prompt = VLUtils.stringNullDefault(mAppUpdateRes.mPromptMessageForced, "发现新版本，点确定升级到最新版本");
                    currentActivity.showAlertDialog(title, prompt, false, new VLResHandler(VLScheduler.THREAD_BG_NORMAL) {
                        @Override
                        protected void handler(boolean succeed) {//确定下载
                            doUpdateDownload();
                        }
                    });
                } else {//其他情况
                    mAppUpdateRes = null;
                }
            }
        });
    }


    private void doUpdateDownload() {
        mIsDoUpdateDownloading = true;
        if (!VLUtils.threadInMain()) {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    doUpdateDownload();
                }
            });
            return;
        }
        if (mAppUpdateRes == null) return;
        final VLActivity currentActivity = VLApplication.instance().getActivityManager().getCurrentActivity();
        if (currentActivity == null) return;
        if (mAppUpdateRes.mHttpTaskId == 0) {//下载任务没有开始,启动下载任务
            VLHttpClient httpClient = VLApplication.instance().getHttpClient();
            mAppUpdateRes.mHttpTaskId = httpClient.httpFileDownloadTask(true, mAppUpdateRes.mDownloadUrl, mAppUpdateRes.mDownloadFilepath, 32 * 1024, new VLHttpClient.VLHttpFileDownloadListener() {
                        public void onResProgress(int currentLength, int contentLength) {//鏇存柊涓嬭浇杩涘害
                            String downloadDesc;
                            if (contentLength > 0)
                                downloadDesc = (currentLength / 1024) + "k/" + (contentLength / 1024) + "k (" + (currentLength * 100 / contentLength) + "%)";
                            else
                                downloadDesc = (currentLength / 1024) + "k";
                            String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptDownloadTitle, "下载进度");
                            currentActivity.updateProgressDialog(title, downloadDesc);
                        }
                    },
                    new VLAsyncHandler<Object>(null, VLScheduler.THREAD_MAIN) {
                        @Override
                        protected void handler(boolean succeed) {//下载完成
                            currentActivity.hideProgressDialog();
                            if (!succeed) {//下载失败
                                if (getRes() != VLAsyncRes.VLAsyncResCanceled)//不是用户取消造成的下载失败,显示提示出错信息
                                    currentActivity.showToast("下载出错:" + getStr());
                            } else {//下载成功,启动安装,如果是强制更新则结束程序
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(new File(mAppUpdateRes.mDownloadFilepath)), "application/vnd.android.package-archive");
                                currentActivity.startActivityForResult(intent, 1);
                                if (mAppUpdateRes.mCheckRes == VLAppUpdateRes.UpdateForced)
                                    getConcretApplication().exit();
                            }
                            mAppUpdateRes.mHttpTaskId = 0;
                            mAppUpdateRes = null;
                            mIsDoUpdateDownloading = false;
                        }
                    });
        }

        //显示下载进度框
        String okLabel = null;
        String cancelLabel = null;
        if (mAppUpdateRes.mCheckRes != VLAppUpdateRes.UpdateForced) {//非强制更新的下载进度框, 提示后台下载和取消下载
            okLabel = VLUtils.stringNullDefault(mAppUpdateRes.mPromptLabelBackgroundDownload, "后台下载");
            cancelLabel = VLUtils.stringNullDefault(mAppUpdateRes.mPromptLabelCancelDownload, "取消下载");
        }
        String title = VLUtils.stringNullDefault(mAppUpdateRes.mPromptDownloadTitle, "下载进度");
        String prompt = VLUtils.stringNullDefault(mAppUpdateRes.mPromptDownloadProgress, "正在下载更新..");
        currentActivity.showOkCancelProgressDialog(title, Html.fromHtml(prompt), okLabel, cancelLabel, false, new VLResHandler() {//显示下载进度框, 非强制更新可以出现后台下载和取消下载按钮, 强制更新的话就不显示按钮
            @Override
            protected void handler(boolean succeed) {//下载结束或用户取消
                currentActivity.hideProgressDialog();
                if (!succeed && mAppUpdateRes.mHttpTaskId > 0) {//用户点击取消下载
                    getConcretApplication().getHttpClient().httpCancelTask(mAppUpdateRes.mHttpTaskId, null);
                    mIsDoUpdateDownloading = false;
                }
            }
        });
    }

    private long getLastRefuseTS() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getLong(KEY_APP_UPDATE_LAST_REFUSE_MS, 0);
    }

    private void saveLastRefuseTS() {
        long currentTs = System.currentTimeMillis();
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().putLong(KEY_APP_UPDATE_LAST_REFUSE_MS, currentTs).apply();
    }
}
