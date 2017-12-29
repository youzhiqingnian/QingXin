package com.vlee78.android.vl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class VLActivity extends FragmentActivity implements VLMessageManager.VLMessageHandler {
    public enum VLActivityState {
        ActivityInited,
        ActivityCreated,
        ActivityStarted,
        ActivityResumed,
        ActivityPaused,
        ActivityStopped,
        ActivityRestarted,
        ActivityDestroyed
    }

    public static final String KEY_VIEW_HINT = "key_view_hint";
    public static final String KEY_VIEW_PROMPT = "key_view_prompt";

    private String mClassName;
    private VLActivityState mState;
    private long mVolDownUpKeyTs;
    private VLActivityResultListener mActivityResultListener;

    private SparseArray<View> mViews;
    private FrameLayout mFrameLayout;

    private View mProgressBarView;
    private VLProgressBar mProgressBar;

    public VLActivityState getState() {
        return mState;
    }

    protected View showView(int layoutResId) {
        View view = mViews.get(layoutResId);
        if (view == null || view.getParent() != null)
            view = LayoutInflater.from(this).inflate(layoutResId, mFrameLayout, false);
        mFrameLayout.addView(view, VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        mViews.put(layoutResId, view);
        return view;
    }

    protected void hideView(int layoutResId) {
        View view = mViews.get(layoutResId);
        if (view != null) {
            mFrameLayout.removeView(view);
            mViews.remove(layoutResId);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassName = getClass().getName();
        mState = VLActivityState.ActivityInited;
        mVolDownUpKeyTs = 0;
        mActivityResultListener = null;
        mViews = new SparseArray<>();
        mFrameLayout = new FrameLayout(this);
        mState = VLActivityState.ActivityCreated;
        VLDebug.logV("Activity create : " + mClassName + ".onCreate()");
        getConcretApplication().getActivityManager().activityCreate(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent intent = getIntent();
        String viewHint = intent.getStringExtra(KEY_VIEW_HINT);
        if (viewHint != null && viewHint.length() > 0) {
            intent.putExtra(KEY_VIEW_HINT, "");
            Toast.makeText(this, viewHint, Toast.LENGTH_LONG).show();
        }
        String viewPrompt = intent.getStringExtra(KEY_VIEW_PROMPT);
        if (viewPrompt != null && viewPrompt.length() > 0) {
            intent.putExtra(KEY_VIEW_PROMPT, "");
            showAlertDialog(null, viewPrompt, true, null);
        }
    }

    @Override
    public void setContentView(View view) {
        mFrameLayout.addView(view, 0, VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        super.setContentView(mFrameLayout);
    }

    @Override
    public void setContentView(int layoutId) {
        View view = LayoutInflater.from(this).inflate(layoutId, mFrameLayout, false);
        mFrameLayout.addView(view, 0, VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        super.setContentView(mFrameLayout);
    }

    public interface VLActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent intent);
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (mActivityResultListener != null) {
            VLActivityResultListener activityResultListener = mActivityResultListener;
            mActivityResultListener = null;
            activityResultListener.onActivityResult(requestCode, resultCode, intent);
        }
        synchronized (this) {
            if (VLActivityResultListenerMap != null) {
                for (Map.Entry<String, VLActivityResultListener> entry : VLActivityResultListenerMap.entrySet()) {
                    entry.getValue().onActivityResult(requestCode, resultCode, intent);
                }
            }
        }
    }

    HashMap<String, VLActivityResultListener> VLActivityResultListenerMap;

    public void putResultListener(@Nullable String key, @Nullable VLActivityResultListener vlActivityResultListener) {

        synchronized (this) {
            if (VLActivityResultListenerMap == null) {
                VLActivityResultListenerMap = new HashMap<>();
            }
            VLActivityResultListenerMap.put(key, vlActivityResultListener);
        }

    }

    public void removeResultListener(@Nullable String key) {
        synchronized (this) {
            if (VLActivityResultListenerMap != null) {
                VLActivityResultListenerMap.remove(key);
            }
        }
    }

    public void setActivityResultListener(VLActivityResultListener activityResultListener) {
        mActivityResultListener = activityResultListener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        VLDebug.logV("Activity start : " + mClassName + ".onStart()");
        mState = VLActivityState.ActivityStarted;
    }

    @Override
    protected void onResume() {
        super.onResume();
        VLDebug.logV("Activity resume : " + mClassName + ".onResume()");
        mState = VLActivityState.ActivityResumed;
        getConcretApplication().getActivityManager().activityResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        VLDebug.logV("Activity pause : " + mClassName + ".onPause()");
        mState = VLActivityState.ActivityPaused;
        getConcretApplication().getActivityManager().activityPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VLDebug.logV("Activity stop : " + mClassName + ".onStop()");
        mState = VLActivityState.ActivityStopped;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VLDebug.logV("Activity destroy : " + mClassName + ".onDestroy()");
        mState = VLActivityState.ActivityDestroyed;
        getConcretApplication().getActivityManager().activityDestroy(this);
        getConcretApplication().getMessageManager().unregisterMessageHandler(this);
        VLAsyncHandler.cancelByHolder(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        VLDebug.logV("Activity restart : " + mClassName + ".onRestart()");
        mState = VLActivityState.ActivityRestarted;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        VLDebug.logD("Activity newintent : " + mClassName + ".onNewIntent() intent=" + intent);
    }

    public void setWindowNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setWindowFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public VLApplication getVLApplication() {
        return VLApplication.instance();
    }

    @SuppressWarnings("unchecked")
    public <T extends VLApplication> T getConcretApplication() {
        return (T) VLApplication.instance();
    }

    public <T> T getModel(Class<T> modelClass) {
        return getConcretApplication().getModel(modelClass);
    }

    public SharedPreferences getSharedPreferences() {
        return getConcretApplication().getSharedPreferences();
    }

    public void showKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void hideKeyboardByIMM() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }

    public void showKeyboardByIMM(View view) {
        view.setFocusable(true);
        boolean isRequestFocuse = view.requestFocus();
        VLDebug.logD("showKeyboardByIMM=" + isRequestFocuse);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hideKeyboardByIMM(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showToast(final String msg) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            VLToast.makeText(VLActivity.this, msg).show();
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    VLToast.makeText(VLActivity.this, msg).show();
                }
            });
        }
    }

    public void showToast(final int resId) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            VLToast.makeText(VLActivity.this, getString(resId)).show();
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    VLToast.makeText(VLActivity.this, getString(resId)).show();
                }
            });
        }
    }

    private ProgressDialog mProgressDialog;

    public void showOkCancelProgressDialog(final String title, final CharSequence message, final String okLabel, final String cancelLabel, final boolean cancelable, final VLResHandler resHandler) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            if (mProgressDialog != null) {
                VLDialog.hideProgressDialog(mProgressDialog);
                mProgressDialog = null;
            }
            mProgressDialog = VLDialog.showOkCancelProgressDialog(this, title, message, okLabel, cancelLabel, cancelable, resHandler);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mProgressDialog != null) {
                        VLDialog.hideProgressDialog(mProgressDialog);
                        mProgressDialog = null;
                    }
                    mProgressDialog = VLDialog.showOkCancelProgressDialog(VLActivity.this, title, message, okLabel, cancelLabel, cancelable, resHandler);
                }
            });
        }
    }

    public void showOkCancelDialog(final String title, final CharSequence message, final String okLabel, final String cancelLabel, final boolean cancelable, final VLResHandler resHandler) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            VLDialog.showOkCancelDialog(this, title, message, okLabel, cancelLabel, false, resHandler);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    VLDialog.showOkCancelDialog(VLActivity.this, title, message, okLabel, cancelLabel, false, resHandler);
                }
            });
        }
    }

    public void showAlertDialog(final String title, final CharSequence message, final boolean cancelable, final VLResHandler resHandler) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            VLDialog.showAlertDialog(this, title, message, cancelable, resHandler);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    VLDialog.showAlertDialog(VLActivity.this, title, message, cancelable, resHandler);
                }
            });
        }
    }

    /**
     * 设置进度条进度
     *
     * @param progress 当前进度
     * @return true表示设置成功, false表示当前未启用进度条
     */
    public boolean setProgressBarProgress(final int progress) {
        final AtomicBoolean result = new AtomicBoolean();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                if (mProgressBar != null) {
                    mProgressBar.setProgress(progress);
                    result.set(true);
                }
                result.set(false);
            }
        };
        if (VLUtils.threadInMain()) {
            task.run();
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    task.run();
                }
            });
        }
        return result.get();
    }

    public void showProgressBar(final @LayoutRes int layoutResId, final @IdRes int progressBarId, final ProgressBarState state) {
        showProgressBar(layoutResId, progressBarId, state, false);
    }

    private void _showProgressBar(final @LayoutRes int layoutResId, final @IdRes int progressBarId, final ProgressBarState state, final boolean global) {
        if (mProgressBar == null) {
            View view = LayoutInflater.from(VLActivity.this).inflate(layoutResId, mFrameLayout, false);
            VLProgressBar progressBar = (VLProgressBar) view.findViewById(progressBarId);
            if (state != null) {
                if (state.title != null)
                    progressBar.setTitle(state.getTitle());
            }
            progressBar.setGlobal(global);
            mFrameLayout.addView(view, VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.WRAP_CONTENT));
            mProgressBarView = view;
            mProgressBar = progressBar;
        }
        mProgressBar.setMax(state.max);
        mProgressBar.setProgress(state.progress);
    }

    public void showProgressBar(final @LayoutRes int layoutResId, final @IdRes int progressBarId, final ProgressBarState state, final boolean global) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            _showProgressBar(layoutResId, progressBarId, state, global);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    _showProgressBar(layoutResId, progressBarId, state, global);
                }
            });
        }
    }

    public boolean isGlobalProgressBar() {
        return mProgressBar != null && mProgressBar.isGlobal();
    }

    public void hideProgressBar() {
        if (mProgressBar == null)
            return;
        if (VLUtils.threadInMain()) {
            if (mProgressBar != null) {
                mFrameLayout.removeView(mProgressBarView);
                mProgressBarView = null;
                mProgressBar = null;
            }
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mProgressBar != null) {
                        mFrameLayout.removeView(mProgressBarView);
                        mProgressBarView = null;
                        mProgressBar = null;
                    }
                }
            });
        }
    }

    public void showProgressDialog(final String title, final String message, final boolean cancelable) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            if (mProgressDialog != null) {
                VLDialog.hideProgressDialog(mProgressDialog);
                mProgressDialog = null;
            }
            mProgressDialog = VLDialog.showProgressDialog(this, title, message, cancelable);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mProgressDialog != null) {
                        VLDialog.hideProgressDialog(mProgressDialog);
                        mProgressDialog = null;
                    }
                    mProgressDialog = VLDialog.showProgressDialog(VLActivity.this, title, message, cancelable);
                }
            });
        }
    }

    public void showProgressDialog(final String title, final String message, final boolean cancelable, final OnCancelListener cancelListener) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (VLUtils.threadInMain()) {
            if (mProgressDialog != null) {
                VLDialog.hideProgressDialog(mProgressDialog);
                mProgressDialog = null;
            }
            mProgressDialog = VLDialog.showProgressDialog(this, title, message, cancelable);
            mProgressDialog.setOnCancelListener(cancelListener);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mProgressDialog != null) {
                        VLDialog.hideProgressDialog(mProgressDialog);
                        mProgressDialog = null;
                    }
                    mProgressDialog = VLDialog.showProgressDialog(VLActivity.this, title, message, cancelable);
                    mProgressDialog.setOnCancelListener(cancelListener);
                }
            });
        }
    }

    public void updateProgressDialog(final String title, final String message) {
        if (mState == VLActivityState.ActivityDestroyed)
            return;
        if (mProgressDialog == null)
            return;
        if (VLUtils.threadInMain()) {
            if (mProgressDialog != null) {
                VLDialog.updateProgressDialog(mProgressDialog, title, message);
            }
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mProgressDialog != null)
                        VLDialog.updateProgressDialog(mProgressDialog, title, message);
                }
            });
        }
    }

    protected View showViewBelowActionBar(int layoutResId, int actionBarH) {
        hideView(layoutResId);
        View view = mViews.get(layoutResId);
        if (view == null || view.getParent() != null)
            view = LayoutInflater.from(this).inflate(layoutResId, mFrameLayout, false);
        FrameLayout.LayoutParams fl = VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT);
        fl.topMargin = actionBarH;
        mFrameLayout.addView(view, fl);
        mViews.put(layoutResId, view);
        return view;
    }

    public void hideProgressDialog() {
        if (mProgressDialog == null)
            return;
        if (VLUtils.threadInMain()) {
            if (mProgressDialog != null) {
                VLDialog.hideProgressDialog(mProgressDialog);
                mProgressDialog = null;
            }
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    if (mProgressDialog != null) {
                        VLDialog.hideProgressDialog(mProgressDialog);
                        mProgressDialog = null;
                    }
                }
            });
        }
    }

    protected void restart() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) && mVolDownUpKeyTs == 0) {
            mVolDownUpKeyTs = System.currentTimeMillis();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) && mVolDownUpKeyTs != 0) {
            long holdMs = System.currentTimeMillis() - mVolDownUpKeyTs;
            getConcretApplication().getActivityManager().keyHold(keyCode, holdMs);
            mVolDownUpKeyTs = 0;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void startActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivity(Class<? extends Activity> cls, String hint) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(KEY_VIEW_HINT, hint);
        startActivity(intent);
    }

    public void registerMessageIds(int... messageIds) {
        getConcretApplication().getMessageManager().registerMessageHandler(this, messageIds);
    }

    protected void broadcastMessage(int msgId, Object msgParam, VLResHandler resHandler) {
        getConcretApplication().getMessageManager().broadcastMessage(msgId, msgParam, resHandler);
    }

    @Override
    public void onMessage(int msgId, Object msgParam) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//suppress and ignore unused exception
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    public VLUmengStatModel getVLUmengStatModel() {
        return null;
    }

    public static class ProgressBarState {
        private String title;
        private int max;
        private int progress;
        private boolean animate;

        public ProgressBarState(int max, int progress) {
            this(null, max, progress);
        }

        public ProgressBarState(String title, int max, int progress) {
            this.title = title;
            this.max = max;
            this.progress = progress;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

    }
}
