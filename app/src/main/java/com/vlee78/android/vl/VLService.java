package com.vlee78.android.vl;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;

public class VLService extends Service
{
	private String mClassName;
	
	public VLService()
	{
		mClassName = getClass().getName();
		VLDebug.logV("Service construct : " + mClassName + "()");
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		VLDebug.logV("Service bind : " + mClassName + ".onBind() intent=" + intent);
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		VLDebug.logV("Service create : " + mClassName + ".onCreate()");
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		VLDebug.logV("Service destroy : " + mClassName + ".onDestroy()");
	}
	
	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
		VLDebug.logV("Service lowmemory : " + mClassName + ".onLowMemory()");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);
		VLDebug.logV("Service start : " + mClassName + ".onStart() intent=" + intent + ",startId=" + startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		int res = super.onStartCommand(intent, flags, startId);
		VLDebug.logV("Service startcommand : " + mClassName + ".onStartCommand() intent=" + intent + ",flags=" + flags + ",startId=" + startId);
		return res;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		VLDebug.logV("Service configurationchanged : " + mClassName + ".onConfigurationChanged() newConfig=" + newConfig);
	}
	
	@Override
	public void onTaskRemoved(Intent rootIntent) 
	{
		super.onTaskRemoved(rootIntent);
		VLDebug.logV("Service taskremoved : " + mClassName + ".onTaskRemoved() rootIntent=" + rootIntent);
	}
	
	@Override
	public void onTrimMemory(int level) 
	{
		super.onTrimMemory(level);
		VLDebug.logV("Service trimmemory : " + mClassName + ".onTrimMemory() level=" + level);
	}
	
	@Override
	public void onRebind(Intent intent) 
	{
		super.onRebind(intent);
		VLDebug.logV("Service rebind : " + mClassName + ".onRebind() intent=" + intent);
	}
	
	@Override
	public boolean onUnbind(Intent intent) 
	{
		boolean res = super.onUnbind(intent);
		VLDebug.logV("Service unbind : " + mClassName + ".onUnbind() intent=" + intent);
		return res;
	}
	
	public VLApplication getVLApplication()
	{
		return VLApplication.instance();
	}

	@SuppressWarnings("unchecked")
	public <T extends VLApplication> T getConcretApplication()
	{
		T application = (T) VLApplication.instance();
		return application;
	}
	
	public <T> T getModel(Class<T> modelClass)
	{
		return getConcretApplication().getModel(modelClass);
	}
	
	public SharedPreferences getSharedPreferences()
	{
		return getConcretApplication().getSharedPreferences();
	}
}
