package com.vlee78.android.vl;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class VLMessageManager
{
	private SparseArray<List<VLMessageHandler>> mMessageHandlers;

	public interface VLMessageHandler
	{
		public void onMessage(int msgId, Object msgParam);
	}

	public static class VLAsynHandlers
	{
		private boolean mFlag;
		private int mFinishCount;
		private List<VLResHandler> mResHandlers;
		private VLResHandler mFinishHandler;

		public VLAsynHandlers()
		{
			mFlag = false;
			mFinishCount = 0;
			mResHandlers = new ArrayList<VLResHandler>();
			mFinishHandler = null;
		}

		public synchronized VLResHandler registerHandler(int thread)
		{
			VLDebug.Assert(!mFlag);
			VLResHandler resHandler = new VLResHandler(thread)
			{
				@Override
				protected void handler(boolean succeed)
				{
					synchronized(VLAsynHandlers.this)
					{
						mFinishCount++;
						if(mFlag && mFinishCount==mResHandlers.size())
						{
							finish();
						}
					}
				}
			};
			mResHandlers.add(resHandler);
			return resHandler;
		}

		public synchronized void process(VLResHandler resHandler)
		{
			VLDebug.Assert(!mFlag);
			mFlag = true;
			mFinishHandler = resHandler;
			if(mFinishCount==mResHandlers.size()) finish();
		}

		private void finish()
		{
			if(mFinishHandler!=null)
			{
				mFinishHandler.setParam(mResHandlers);
				mFinishHandler.handlerSuccess();
			}
		}
	}

	public VLMessageManager()
	{
		mMessageHandlers = new SparseArray<List<VLMessageHandler>>();
	}

	public synchronized void registerMessageHandler(VLMessageHandler messageHandler, int... msgIds)
	{
		for(int msgId : msgIds)
		{
			List<VLMessageHandler> handlers = mMessageHandlers.get(msgId);
			if(handlers==null)
			{
				handlers = new ArrayList<VLMessageHandler>();
				mMessageHandlers.put(msgId, handlers);
			}
			handlers.add(messageHandler);
		}
	}

	public void unregisterMessageHandler(VLMessageHandler messageHandler)
	{
		for(int i=0; i<mMessageHandlers.size(); i++)
		{
			List<VLMessageHandler> handlers = mMessageHandlers.valueAt(i);
			handlers.remove(messageHandler);
		}
	}

	public void broadcastMessage(final int msgId, final Object msgParam, final VLResHandler resHandler)
	{
		VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_HIGH, new VLBlock()
		{
			@Override
			protected void process(boolean canceled)
			{
				List<VLMessageHandler> handlers = mMessageHandlers.get(msgId);
				if(handlers!=null)
				{
					List<VLMessageHandler> hCopy = new ArrayList<VLMessageHandler>();
					hCopy.addAll(handlers);
					for(VLMessageHandler handler : hCopy)
					{
						try
						{
							handler.onMessage(msgId, msgParam);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				if(resHandler!=null) resHandler.handlerSuccess();
			}
		});
	}
}
