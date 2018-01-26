package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

public class VLNotificationManager 
{
	private VLApplication mApplication;
	private NotificationManager mNotificationManager;
	private int mIconResId;
	private int mId;

	public VLNotificationManager(VLApplication application)
	{
		mApplication = application;
		mNotificationManager = (NotificationManager)mApplication.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		mIconResId = application.appInfo().icon;
		mId = 0;
	}
	
	public void setIcon(int iconResId)
	{
		mIconResId = iconResId;
	}
	
	@SuppressLint("NewApi")
	private int innerSendNotification(String title, String text, Intent intent)
	{
		Notification.Builder builder = new Notification.Builder(mApplication);
		if(title!=null) builder.setContentTitle(title);
		if(text!=null) builder.setContentText(text);
		if(mIconResId!=0) builder.setSmallIcon(mIconResId);
		builder.setAutoCancel(true);
		if(intent==null) intent = new Intent();
		builder.setContentIntent(PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
		
		@SuppressWarnings("deprecation")
		Notification notification = (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN ? builder.build() : builder.getNotification());
		notification.defaults = Notification.DEFAULT_SOUND;
		mNotificationManager.notify(++mId, notification);
		return mId;
	}
	
	public void clearNotification(int id)
	{
		mNotificationManager.cancel(id);
	}
	
	public void clearAllNotifications()
	{
		mNotificationManager.cancelAll();
	}
	
	public int sendNotification(Intent intent)
	{
		return innerSendNotification(null, null, intent);
	}
	
	public int sendNotification(String text, Intent intent)
	{
		return innerSendNotification(null, text, intent);
	}
	
	public int sendNotification(String title, String text, Intent intent)
	{
		return innerSendNotification(title, text, intent);
	}	
}
