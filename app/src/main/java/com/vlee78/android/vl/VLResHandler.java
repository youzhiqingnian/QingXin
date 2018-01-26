package com.vlee78.android.vl;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class VLResHandler {
    private static final HashSet<VLResHandler> gResHandlers = new HashSet<>();

    public static void cancelResHandlerByCallee(Object callee) {
        if (callee == null)
            return;
        List<VLResHandler> tempHandlers = new ArrayList<>();
        synchronized (gResHandlers) {
            for (VLResHandler resHandler : gResHandlers) {
                if (resHandler.mCallee == callee)
                    tempHandlers.add(resHandler);
            }
        }
        for (VLResHandler resHandler : tempHandlers)
            resHandler.handlerError(VLResHandler.CANCEL, "");
    }

    public static final int SUCCEED = 0;
    public static final int CANCEL = -1;
    public static final int TIMEOUT = -2;
    public static final int ERROR = -3;
    public static final int SHUTDOWN = -4;
    public static final int CONCURRENT = -5;

    private boolean mSucceed;
    private int mErrorCode;
    private String mErrorString;
    private Object mParam;
    private int mResThread;
    public String mCreateDesc;
    private Handler mHandler;
    private boolean mHandlered;
    private Object mCallee;

    public VLResHandler() {
        init(VLScheduler.THREAD_MAIN, null, null);
    }

    public VLResHandler(int resThread) {
        init(resThread, null, null);
    }

    public VLResHandler(Handler handler) {
        init(-1, handler, null);
    }

    public VLResHandler(int resThread, Object callee) {
        init(resThread, null, callee);
    }

    public VLResHandler(Object callee) {
        init(VLScheduler.THREAD_MAIN, null, callee);
    }

    private void init(int resThread, Handler handler, Object callee) {
        mSucceed = false;
        mErrorCode = 0;
        mErrorString = null;
        mParam = null;
        mResThread = resThread;
        mHandler = handler;
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        mCreateDesc = stackTraceElement.getClassName() + "::" + stackTraceElement.getMethodName() + "("
                + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber();
        mHandlered = false;
        mCallee = callee;
        if (callee != null) {
            synchronized (gResHandlers) {
                gResHandlers.add(this);
            }
        }
    }

    public int resThread() {
        return mResThread;
    }

    public boolean succeed() {
        return mSucceed;
    }

    public int errorCode() {
        return mErrorCode;
    }

    public String errorString() {
        if (mErrorString == null)
            mErrorString = "";
        return mErrorString;
    }

    public String errorMsg() {
        return "{" + mErrorCode + ":" + errorString() + "}";
    }

    public void setParam(Object param) {
        mParam = param;
    }

    public Object param() {
        return mParam;
    }

    protected abstract void handler(boolean succeed);

    public void handlerSuccess() {
        handlerCommon(true, SUCCEED, null, null);
    }

    public void handlerError(int errorCode, String errorString) {
        handlerCommon(false, errorCode, errorString, null);
    }

    public void handlerSuccess(VLResHandler postHandler) {
        handlerCommon(true, SUCCEED, null, postHandler);
    }

    public void handlerError(int errorCode, String errorString, VLResHandler postHandler) {
        handlerCommon(false, errorCode, errorString, postHandler);
    }

    private void handlerCommon(final boolean succeed, int errorCode, String errorString, final VLResHandler postHandler) {
        synchronized (gResHandlers) {
            if (mHandlered)
                return;
            mHandlered = true;
            if (mCallee != null)
                gResHandlers.remove(this);
        }

        mSucceed = succeed;
        mErrorCode = errorCode;
        mErrorString = errorString;

        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    handler(succeed);
                    if (postHandler != null)
                        postHandler.handlerSuccess();
                }
            });
        } else {
            VLScheduler.instance.schedule(0, mResThread, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    handler(succeed);
                    if (postHandler != null)
                        postHandler.handlerSuccess();
                }
            });
        }
    }
}
