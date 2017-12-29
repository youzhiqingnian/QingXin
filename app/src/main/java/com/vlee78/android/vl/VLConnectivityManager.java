package com.vlee78.android.vl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class VLConnectivityManager 
{
	public interface VLConnectivityListener
	{
		void onConnectivityChanged(int stateFr, int stateTo);
	}
	
	private static VLConnectivityManager gConnectivityManager = null;
	
	public static VLConnectivityManager instance()
	{
		if(gConnectivityManager==null) gConnectivityManager = new VLConnectivityManager();
		return gConnectivityManager;
	}
	
	public static boolean isConnected(int state)
	{
		return (state==STATE_CONNECTED_MOBILE || state==STATE_CONNECTED_WIFI || state==STATE_CONNECTED_UNKNOWN);
	}
	
	public static final int STATE_UNINIT = 0;
	public static final int STATE_DISCONNECT = 1;
	public static final int STATE_CONNECTED_MOBILE = 2;
	public static final int STATE_CONNECTED_WIFI = 3;
	public static final int STATE_CONNECTED_UNKNOWN = 4;
	
	private List<VLConnectivityListener> mListeners = new ArrayList<>();
	private ConnectivityManager mManager = null;
	private int mState = STATE_DISCONNECT;
	
	private void onState()
	{
		NetworkInfo networkInfo = mManager.getActiveNetworkInfo();
		int stateTo;
		if(networkInfo==null || !networkInfo.isConnected()) stateTo = STATE_DISCONNECT;
		else if(networkInfo.getType()== ConnectivityManager.TYPE_MOBILE) stateTo = STATE_CONNECTED_MOBILE;
		else if(networkInfo.getType()== ConnectivityManager.TYPE_WIFI) stateTo = STATE_CONNECTED_WIFI;
		else stateTo = STATE_CONNECTED_UNKNOWN;
		if(mState!=stateTo)
		{
			if(mState!=STATE_DISCONNECT && stateTo!=STATE_DISCONNECT)
			{
				int stateTmp = mState;
				mState = STATE_DISCONNECT;
				for(VLConnectivityListener listener : mListeners)
					listener.onConnectivityChanged(stateTmp, mState);
			}
			int stateFr = mState;
			mState = stateTo;
			for(VLConnectivityListener listener : mListeners)
				listener.onConnectivityChanged(stateFr, stateTo);
		}
	}
	
	private VLConnectivityManager()
	{
		Context context = VLApplication.instance();
		mManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				onState();
			}
		},intentFilter);
	    onState();
	}
	
	public void addConnectivityListener(VLConnectivityListener listener)
	{
		listener.onConnectivityChanged(STATE_UNINIT, mState);
		mListeners.add(listener);
	}
	
	public void removeConnectivityListener(VLConnectivityListener listener)
	{
		mListeners.remove(listener);
	}
	
	public int getState()
	{
		return mState;
	}
}
