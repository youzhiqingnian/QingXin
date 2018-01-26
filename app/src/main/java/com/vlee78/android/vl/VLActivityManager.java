package com.vlee78.android.vl;

import android.view.KeyEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VLActivityManager 
{
	public interface VLActivityListener
	{
		void onManagerCreate(VLApplication application);
		void onManagerDestroy(VLApplication application);
		void onActivityCreate(VLActivity activity);
		void onActivityResume(VLActivity activity);
		void onActivityPause(VLActivity activity);
		void onActivityDestroy(VLActivity activity);
	}

	private List<VLActivityListener> mListeners;
	private List<VLActivity> mActivities;
	private VLActivity mCurrentActivity;
	private List<WeakReference<VLActivity>> mWeakActivities;

	protected  VLActivityManager()
	{
		mListeners = new ArrayList<>();
		mActivities = new ArrayList<>();
		mWeakActivities = new ArrayList<>();
		mCurrentActivity = null;
	}

	protected void keyHold(int keyCode, long holdMs)
	{
		if(holdMs>=3000)
		{
			if(keyCode== KeyEvent.KEYCODE_VOLUME_DOWN)
			{
				String desDir = VLApplication.instance().dumpDatabase();
				if(desDir!=null) showToast("成功dump数据库: " + desDir);
			}
			else if(keyCode== KeyEvent.KEYCODE_VOLUME_UP)
			{
				dumpActivities();
				VLApplication.instance().dumpTaskInfos();
				if(holdMs>=5000)
				{
					VLScheduler.instance.schedule(5000, VLScheduler.THREAD_MAIN, new VLBlock()
					{
						@Override
						protected void process(boolean canceled)
						{
							dumpActivities();
							VLApplication.instance().dumpTaskInfos();
						}
					});
				}
			}
		}
	}

	protected void managerCreate(VLApplication application)
	{
		for(VLActivityListener listener : mListeners) listener.onManagerCreate(application);
	}

	protected void managerDestroy(VLApplication application)
	{
		finishAllActivies();
		for(VLActivityListener listener : mListeners) listener.onManagerDestroy(application);
	}

	protected final void activityCreate(VLActivity activity)
	{
		mActivities.add(activity);
		mWeakActivities.add(new WeakReference<>(activity));
		if(mCurrentActivity==null) mCurrentActivity = activity;
		for(VLActivityListener listener : mListeners) listener.onActivityCreate(activity);
	}

	protected final void activityResume(VLActivity activity)
	{
		mCurrentActivity = activity;
		for(VLActivityListener listener : mListeners) listener.onActivityResume(activity);
	}

	protected final void activityPause(VLActivity activity)
	{
		for(VLActivityListener listener : mListeners) listener.onActivityPause(activity);
	}

	protected final void activityDestroy(VLActivity activity)
	{
		mActivities.remove(activity);
		if(mCurrentActivity==activity)
		{
			if(mActivities.size()>0)
				mCurrentActivity = mActivities.get(mActivities.size()-1);
			else
				mCurrentActivity = null;
		}
	}

	public void addListener(VLActivityListener listener)
	{
		mListeners.add(listener);
	}

	public void removeListener(VLActivityListener listener)
	{
		mListeners.remove(listener);
	}

	public int getActivityCount()
	{
		return mActivities.size();
	}

	public VLActivity getCurrentActivity()
	{
		return mCurrentActivity;
	}

	@SuppressWarnings("unchecked")
	public <T> T getActivity(Class<T> cls)
	{
		for(VLActivity activity : mActivities)
		{
			if(activity.getClass()==cls) return (T)activity;
		}
		return null;
	}

	public void showToast(final String msg)
	{
		if(mCurrentActivity!=null) mCurrentActivity.showToast(msg);
	}

	public void finishAllActivies()
	{//mCurrentActivity会在收到OnDestroy后正确赋值
		for(int i = mActivities.size() - 1; i >= 0; i--)
		{
			VLActivity activity = mActivities.get(i);
			activity.finish();
		}
		mActivities.clear();
	}

	public void dumpActivities()
	{
		VLDebug.logI("=========================================================");
		VLDebug.logI("Current activities in stack of size: " + mActivities.size());
		for(VLActivity activity : mActivities)
			VLDebug.logI("{" + activity.getClass().getName() + "," + activity.getState() + "}");
		Map<Class<?>, Integer> counter = new HashMap<>();
		for(WeakReference<VLActivity> weakActivity : mWeakActivities)
		{
            VLActivity activity = weakActivity.get();
            if(activity==null) continue;
            Class<?> cls = activity.getClass();
            Integer count = counter.get(cls);
            if(count==null) count = 0;
            counter.put(cls, count+1);
		}
		VLDebug.logI("Current weak activities");
		for(Entry<Class<?>,Integer> entry : counter.entrySet())
			VLDebug.logI("{" + entry.getKey().getName() + " : " + entry.getValue());
		VLDebug.logI("=========================================================");
	}
}
