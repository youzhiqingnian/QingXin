package com.vlee78.android.vl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class VLBroadcastReceiver extends BroadcastReceiver
{
	private String mClassName;
	
	public VLBroadcastReceiver()
	{
		mClassName = getClass().getName();
		VLDebug.logV("Receiver construct : " + mClassName + "()");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		VLDebug.logV("Receiver onReceive : " + mClassName + "() intent=" + intent + ",bundle=" + VLUtils.dumpBundle(intent.getExtras()));
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


