package com.vlee78.android.vl;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class VLSmsManager extends ContentObserver
{
	public interface VLSmsListener
	{
		void onSmsReceived(long id, String address, String person, String body);
	}
		
	private static VLSmsManager gSmsManager;
	
	public static VLSmsManager instance()
	{
		if(gSmsManager==null) gSmsManager = new VLSmsManager();
		return gSmsManager;
	}
	
	private ContentResolver mContentResolver;
	private List<VLSmsListener> mListeners;
	private long mLastId;
	
	public VLSmsManager()
	{
		super(null);
		mContentResolver = VLApplication.instance().getContentResolver();
		mListeners = new ArrayList<>();
		mLastId = -1;
	}
	
	public void addSmsListener(VLSmsListener listener)
	{
		if(mListeners.size()==0)
			mContentResolver.registerContentObserver(Uri.parse("content://sms/inbox"), true, this);
		mListeners.add(listener);
	}
	
	public void removeSmsListener(VLSmsListener listener)
	{
		mListeners.remove(listener);
		if(mListeners.size()==0)
			mContentResolver.unregisterContentObserver(this);
	}
	
	@Override
	public void onChange(boolean selfChange) 
	{
		super.onChange(selfChange);
		Cursor cursor = null;
		try
		{
			cursor = mContentResolver.query(Uri.parse("content://sms/inbox"), new String[] {"_id", "body", "address", "person" }, null, null, "_id desc");
			if(cursor!=null && cursor.moveToNext())
			{
				long id = cursor.getLong(cursor.getColumnIndex("_id"));
				String address = cursor.getString(cursor.getColumnIndex("address"));
				String person = cursor.getString(cursor.getColumnIndex("person"));
				String body = cursor.getString(cursor.getColumnIndex("body"));
				if(id!=mLastId)
				{
					mLastId = id;
					onNewMessage(id, address, person, body);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		finally
		{
			if(cursor!=null) cursor.close();
		}
	}
	
	private void onNewMessage(final long id, final String address, final String person, final String body)
	{
		VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock()
		{
			@Override
			protected void process(boolean canceled) 
			{
				for(VLSmsListener listener : mListeners)
					listener.onSmsReceived(id, address, person, body);
			}
		});
	}
}
