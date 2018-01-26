package com.vlee78.android.vl;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Iterator;

public abstract class VLAsyncHandler<T> {
    private static HashSet<VLAsyncHandler<?>> gAsyncHandlers = new HashSet<>();

    private static synchronized void addAsyncHandler(VLAsyncHandler<?> asyncHandler) {
        VLDebug.Assert(gAsyncHandlers.add(asyncHandler));
    }

    private static synchronized boolean finishAsyncHandler(VLAsyncHandler<?> asyncHandler) {
        return gAsyncHandlers.remove(asyncHandler);
    }

    public static synchronized void cancelByHolder(Object holder) {
        if (holder == null) return;
        Iterator<VLAsyncHandler<?>> it = gAsyncHandlers.iterator();
        while (it.hasNext()) {
            VLAsyncHandler<?> asyncHandler = it.next();

            if (asyncHandler.getHolder() == holder) {
                asyncHandler.mIsCancelled = true;
                asyncHandler.handlerInternal(false, VLAsyncRes.VLAsyncResCanceled, null, 0, null);
                it.remove();
            }
        }
    }

    public static synchronized void cancelByHandler(VLAsyncHandler<?> asyncHandler) {
        if (!gAsyncHandlers.remove(asyncHandler)) return;
        asyncHandler.mIsCancelled = true;
        asyncHandler.handlerInternal(false, VLAsyncRes.VLAsyncResCanceled, null, 0, null);
    }

    public enum VLAsyncRes {
        VLAsyncResSuccess,
        VLAsyncResCanceled,
        VLAsyncResFailed,
    }

    private Object mHolder;
    private int mThread;
    private String mDesc;
    private int mDelayMs;
    private VLAsyncRes mRes;
    private int mCode;
    private String mStr;
    private T mParam;
    private boolean mIsCancelled;


    public VLAsyncHandler(Object holder, int thread) {
        mHolder = holder;
        mThread = thread;
        mDelayMs = 0;
        mIsCancelled = false;
        mRes = VLAsyncRes.VLAsyncResSuccess;
        mCode = 0;
        mStr = null;
        mParam = null;
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        mDesc = stackTraceElement.getClassName() + "::" + stackTraceElement.getMethodName() + "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber();
        addAsyncHandler(this);
    }

    public VLAsyncHandler(Object holder, int thread, int delayMs) {
        mHolder = holder;
        mThread = thread;
        mDelayMs = delayMs;
        mIsCancelled = false;
        mRes = VLAsyncRes.VLAsyncResSuccess;
        mCode = 0;
        mStr = null;
        mParam = null;
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        mDesc = stackTraceElement.getClassName() + "::" + stackTraceElement.getMethodName() + "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber();
        addAsyncHandler(this);
    }

    public boolean setRes(boolean succeed, VLAsyncRes res, int code, String str) {
        mRes = res;
        mCode = code;
        mStr = str;
        return succeed;
    }

    public void setDelay(int delayMs) {
        mDelayMs = delayMs;
    }

    public Object getHolder() {
        return mHolder;
    }

    public boolean isCancelled() {
        return mIsCancelled;
    }

    public void handlerSuccess() {
        handlerSuccess(null);
    }

    public void handlerSuccess(T param) {
        if (!finishAsyncHandler(this)) return;//已经被处理过了(cancelled)
        handlerInternal(true, VLAsyncRes.VLAsyncResSuccess, param, 0, null);
    }

    public void handlerError(VLAsyncRes res, String str) {
        if (!finishAsyncHandler(this)) return;//已经被处理过了(cancelled)
        handlerInternal(false, res, null, 0, str);
    }

    public void handlerError(VLAsyncRes res, int code, String str) {
        if (!finishAsyncHandler(this)) return;//已经被处理过了(cancelled)
        handlerInternal(false, res, null, code, str);
    }

    private void handlerInternal(final boolean succeed, VLAsyncRes res, T param, int code, String str) {
        mRes = res;
        mParam = param;
        mCode = code;
        mStr = str;
        VLScheduler.instance.schedule(mDelayMs, mThread, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                handler(succeed);
            }
        });
    }

    protected void progress(T param) {
    }

    protected abstract void handler(boolean succeed);

    public int getThread() {
        return mThread;
    }

    protected VLAsyncRes getRes() {
        return mRes;
    }

    protected int getCode() {
        return mCode;
    }

    protected String getStr() {
        if(TextUtils.isEmpty(mStr)){
//            mStr = "联网失败，请检查网络";
        } else if (mStr.trim().startsWith("Unable to resolve host")) {
            mStr = "联网失败，请检查网络";
        }
        return mStr;
    }

    protected T getParam() {
        return mParam;
    }

    protected String getDesc() {
        return mDesc;
    }
}
