package com.vlee78.android.vl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class VLFragment extends Fragment implements VLMessageManager.VLMessageHandler {
    public enum VLFragmentState {
        FragmentInited,
        FragmentAttached,
        FragmentCreated,
        FragmentCreateViewed,
        FragmentViewCreated,
        FragmentStarted,
        FragmentResumed,
        FragmentPaused,
        FragmentStopped,
        FragmentDestroyViewed,
        FragmentDestroyed,
        FragmentDetached,
    }

    private String mClassName;
    private VLFragmentState mState;
    private FrameLayout mFrameLayout;
    private SparseArray<View> mViews;
    private boolean mFirstVisible;

    public VLFragment() {
        mClassName = getClass().getName();
        mFrameLayout = null;
        mViews = new SparseArray<>();
        mState = VLFragmentState.FragmentInited;
        mFirstVisible = false;
        VLDebug.logV("Fragment construct : " + mClassName + "()");
    }

    public VLFragmentState getState() {
        return mState;
    }


    private static LongSparseArray<VLFragment> mBuffer = new LongSparseArray<>();
    private long mId = -1;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VLDebug.logV("Fragment activitystart : " + mClassName + ".onActivityCreated()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VLDebug.logV("Fragment activityresult : " + mClassName + ".onActivityResult()");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mState = VLFragmentState.FragmentAttached;
        VLDebug.logV("Fragment attach : " + mClassName + ".onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mState = VLFragmentState.FragmentDetached;
        VLDebug.logV("Fragment detach : " + mClassName + ".onDetach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mState = VLFragmentState.FragmentDestroyed;
        VLDebug.logV("Fragment destroy : " + mClassName + ".onDestroy()");
        System.gc();
    }

    @Override
    public void onStart() {
        super.onStart();
        mState = VLFragmentState.FragmentStarted;
        VLDebug.logV("Fragment start : " + mClassName + ".onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        mState = VLFragmentState.FragmentStopped;
        VLDebug.logV("Fragment stop : " + mClassName + ".onStop()");
    }

    @Override
    public void onPause() {
        super.onPause();
        mState = VLFragmentState.FragmentPaused;
        VLDebug.logV("Fragment pause : " + mClassName + ".onPause()");
        boolean visible = getUserVisibleHint();
        if (visible) onInvisible();
    }

    @Override
    public void onResume() {
        super.onResume();
        mState = VLFragmentState.FragmentResumed;
        VLDebug.logV("Fragment resume : " + mClassName + ".onResume()");
        boolean visible = getUserVisibleHint();
        if (visible) onVisible(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mState = VLFragmentState.FragmentCreated;
        if (savedInstanceState != null) {
            mId = savedInstanceState.getLong("ID", -1);
            VLFragment vlFragment = mBuffer.get(mId);
            if (vlFragment != null) {
                vlFragment.release();
            }
        }
        VLDebug.logV("Fragment create : " + mClassName + ".onCreate()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mId == -1) {
            mId = System.currentTimeMillis();
        }
        outState.putLong("ID", mId);
        mBuffer.put(mId, this);
        super.onSaveInstanceState(outState);
        VLDebug.logV("Fragment savestate : " + mClassName + ".onSaveInstanceState()");
    }

    private void release() {
        getVLApplication().getMessageManager().unregisterMessageHandler(this);
        VLAsyncHandler.cancelByHolder(this);
        mBuffer.remove(mId);
        mId = -1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mState = VLFragmentState.FragmentDestroyViewed;
        VLDebug.logV("Fragment destroyview : " + mClassName + ".onDestroyView()");
        boolean visible = getUserVisibleHint();
        if (visible) onInvisible();
        release();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mState = VLFragmentState.FragmentViewCreated;
        VLDebug.logV("Fragment createview : " + mClassName + ".onCreateView()");
        mFrameLayout = new FrameLayout(getActivity());
        View view = onCreateContent(inflater, container, savedInstanceState);
        mFrameLayout.addView(view, 0, VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        return mFrameLayout;
    }


    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    protected View showView(int layoutResId) {
        hideView(layoutResId);
        View view = mViews.get(layoutResId);
        if (view == null || view.getParent() != null)
            view = LayoutInflater.from(getActivity()).inflate(layoutResId, mFrameLayout, false);
        mFrameLayout.addView(view, VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        mViews.put(layoutResId, view);
        return view;
    }

    public void hideView(int layoutResId) {
        View view = mViews.get(layoutResId);
        if (view != null) {
            mFrameLayout.removeView(view);
            mViews.remove(layoutResId);
        }
    }

    public View showViewBelowActionBar(int layoutResId, int actionBarH) {
        hideView(layoutResId);
        View view = mViews.get(layoutResId);
        if (view == null || view.getParent() != null)
            view = LayoutInflater.from(getActivity()).inflate(layoutResId, mFrameLayout, false);
        FrameLayout.LayoutParams fl = VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT);
        fl.topMargin = actionBarH;
        mFrameLayout.addView(view, fl);
        mViews.put(layoutResId, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mState = VLFragmentState.FragmentViewCreated;
        VLDebug.logV("Fragment viewcreated : " + mClassName + ".onViewCreated()");

        boolean visible = getUserVisibleHint();
        if (!mFirstVisible && visible) {
            onVisible(true);
            mFirstVisible = true;
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        VLDebug.logV("Fragment viewstaterestore : " + mClassName + ".onViewStateRestored()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        VLDebug.logV("Fragment configurationchanged : " + mClassName + ".onConfigurationChanged()");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        VLDebug.logV("Fragment hiddenchanged : " + mClassName + ".onHiddenChanged()");
    }

    @Override
    public void onMessage(int msgId, Object msgParam) {
    }

    public VLApplication getVLApplication() {
        return VLApplication.instance();
    }

    @SuppressWarnings("unchecked")
    public <T extends VLApplication> T getConcretApplication() {
        return (T) VLApplication.instance();
    }

    public SharedPreferences getSharedPreferences() {
        return getConcretApplication().getSharedPreferences();
    }

    public <T> T getModel(Class<T> modelClass) {
        return getVLApplication().getModelManager().getModel(modelClass);
    }

    public void registerMessageIds(int... messageIds) {
        getVLApplication().getMessageManager().registerMessageHandler(this, messageIds);
    }

    protected void broadcastMessage(int msgId, Object msgParam, VLResHandler resHandler) {
        getVLApplication().getMessageManager().broadcastMessage(msgId, msgParam, resHandler);
    }

    public void startActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    public void showToast(final String msg) {
        if (getActivity() == null) {
            return;
        }
        if (VLUtils.threadInMain()) {
            VLToast.makeText(getVLApplication(), msg, 2000).show();
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    VLToast.makeText(getVLApplication(), msg, 2000).show();
                }
            });
        }
    }

    public VLActivity getVLActivity() {
        return (VLActivity) getActivity();
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mState == VLFragmentState.FragmentInited || mState == VLFragmentState.FragmentAttached || mState == VLFragmentState.FragmentCreated || mState == VLFragmentState.FragmentCreateViewed) {
            return;
        }
        if (isVisibleToUser) {
            if (!mFirstVisible) {
                onVisible(true);
                mFirstVisible = true;
            } else onVisible(false);
        } else onInvisible();
    }

    protected void onVisible(boolean first) {
    }

    protected void onInvisible() {
    }

    public Context getContext() {
        return getVLActivity();
    }

    public String getStr(int resource){
        return getActivity().getResources().getString(resource);
    }
}