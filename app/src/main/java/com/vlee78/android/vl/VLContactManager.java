package com.vlee78.android.vl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

public class VLContactManager 
{
	private static VLContactManager sContactManager = null;
	public static synchronized VLContactManager instance()
	{
		if(sContactManager==null) sContactManager = new VLContactManager();
		return sContactManager;
	}

	private ContentResolver mContentResolver;
	
	public VLContactManager()
	{
		mContentResolver = VLApplication.instance().getContentResolver();
	}
	
	public static class VLContact
	{
		public String mName;
		public String mMobile;
	}
	
	public void dumpContactList(List<VLContact> contacts)
	{
		for(VLContact contact : contacts)
			Log.e("TAG", contact.mName + " : " + contact.mMobile);
	}
	
	public HashMap<String,String> getContacts()
	{
		HashMap<String,String> contacts = new HashMap<>();
		String[] projContacts = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
		String[] projPhones = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
		String[] args = new String[]{null};
		Cursor cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, projContacts, null, null, null);
		while(cursor!=null && cursor.moveToNext())
		{
			args[0] = cursor.getString(0);
			String name = cursor.getString(1);
			int hasPhoneNumber = cursor.getInt(2);
			if(hasPhoneNumber<=0) continue;
			Cursor cursor2 = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projPhones, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", args, null);
			while(cursor2!=null && cursor2.moveToNext())
			{
				String mobile = cursor2.getString(0);
				String normalizedMobile = mobile.replaceAll(" ", "");
				normalizedMobile = normalizedMobile.replaceAll("\\-", "");
				if(normalizedMobile.length()>11) normalizedMobile = normalizedMobile.substring(normalizedMobile.length()-11);
				if(VLUtils.stringValidatePhoneNumber(normalizedMobile))
				{
					contacts.put(normalizedMobile, name);
					break;
				}
			}
			if(cursor2!=null) cursor2.close();
		}
		if(cursor!=null) cursor.close();
		return contacts;
	}
}
