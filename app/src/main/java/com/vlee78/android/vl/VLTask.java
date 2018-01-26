package com.vlee78.android.vl;

import java.util.ArrayList;
import java.util.List;

public abstract class VLTask
{
	private boolean							mServicing				= true;	//控制是否暂停新调度的运行
	private boolean							mTerminating			= false;//控制着任务是否接受新的requestRun，一旦requestTerminate之后就置为false不再接受run请求
	private int								mScheduleThread			= VLScheduler.THREAD_BG_NORMAL;
	private List<Object> mParams					= new ArrayList<>();
	private List<VLResHandler> mResHandlers			= new ArrayList<>();
	private Object mParam					= null;
	private VLResHandler mResHandler				= null;
	private int								mStatus					= STATUS_IDLE;
	private VLScheduler mScheduler				= VLScheduler.instance;
	private VLBlock mAsyncTimeoutBlock		= null;
	private VLResHandler mAsyncResHandler		= null;
	private VLResHandler mTerminateResHandler	= null;
	private int 							mMode 					= MODE_SINGLETON_FIFO;
	private int 							mDelayInMs 				= 0;
	private VLBlock mDelayScheduleBlcok 	= null;


	public static final int STATUS_IDLE						= 0;
	public static final int STATUS_READY 					= 1;
	public static final int STATUS_RERUN_READY				= 2;
	public static final int STATUS_DISPATCH_NEXT 			= 3;
	public static final int STATUS_HANDLE_NEXT 				= 4;
	public static final int STATUS_DISPATCH_ASYNC_TIMEOUT	= 5;
	public static final int STATUS_HANDLE_ASYNC_TIMEOUT		= 6;
	public static final int STATUS_DISPATCH_ASYNC_FINISH	= 7;
	public static final int STATUS_HANDLE_ASYNC_FINISH		= 8;
	public static final int STATUS_DISPATCH_FINISH 			= 9;
	public static final int STATUS_HANDLE_FINISH 			= 10;
	public static final int STATUS_DISPATCH_TERMINATE 		= 11;
	public static final int STATUS_HANDLE_TERMINATE 		= 12;
	public static final int STATUS_TERMINATED				= 13;

	public static final int MODE_SINGLETON_FIFO 			= 0;//单例顺序执行
	public static final int MODE_SINGLETON_MERGE 			= 1;//单例运行中的时候被再调度单次或多次，每个等待调度的任务在单例任务完成的时候，收到CONCURRENT错误而结束
	public static final int MODE_SINGLETON_RELAST 			= 2;//单例运行中的时候被再调度单次或多次，每个等待调度的任务在单例任务完成的时候，除最后那一次的等调度会再调度一次外，其他等待调度请求会收到CONCURRENT错误而结束

	public VLTask(int scheduleThread)
	{
		mScheduleThread = scheduleThread;
		mMode = MODE_SINGLETON_FIFO;
		mDelayInMs = 0;
		onCreate();
	}

	public VLTask(int scheduleThread, int mode, int delayInMs)
	{
		mScheduleThread = scheduleThread;
		mMode = mode;
		mDelayInMs = delayInMs;
		onCreate();
	}

	public void setDelay(int delayInMs)
	{
		mDelayInMs = delayInMs;
	}

	public synchronized boolean wakeup()
	{
		if(mStatus!=STATUS_TERMINATED) return false;
		VLDebug.Assert(mTerminating && mParams.size()==0 && mResHandlers.size()==0);
		mAsyncTimeoutBlock = null;
		mAsyncResHandler = null;
		mTerminateResHandler = null;
		mTerminating = false;
		mServicing = true;
		mStatus = STATUS_IDLE;
		return true;
	}

	protected void onCreate()
	{
	}

	protected void onDestroy()
	{
	}

	protected <T> T getModel(Class<T> modelClass)
	{
		return VLApplication.instance().getModel(modelClass);
	}

	protected void broadcastMessage(int msgId, Object msgParam, VLResHandler resHandler)
	{
		VLApplication.instance().getMessageManager().broadcastMessage(msgId, msgParam, resHandler);
	}

	public int scheduleThread()
	{
		return mScheduleThread;
	}

	public void setScheduleThread(int scheduleThread)
	{
		if(scheduleThread< VLScheduler.THREAD_MAIN) scheduleThread = VLScheduler.THREAD_MAIN;
		if(scheduleThread> VLScheduler.THREAD_BG_IDLE) scheduleThread = VLScheduler.THREAD_BG_IDLE;
		mScheduleThread = scheduleThread;
	}

	public synchronized void setServicing(boolean servicing)
	{
		if(mServicing==servicing) return;
		mServicing = servicing;
		if(servicing) loop();
	}

	public synchronized boolean requestRun(Object param, VLResHandler resHandler)
	{
		if(mTerminating) return false;
		mParams.add(param);
		mResHandlers.add(resHandler);
		if(mStatus==STATUS_IDLE) mStatus = STATUS_READY;
		loop();
		return true;
	}

	public synchronized boolean requestTerminate(VLResHandler terminateResHandler)
	{
		if (!mServicing) return false;
		if(mTerminating) return false;
		mTerminating = true;
		VLDebug.Assert(mTerminateResHandler==null);
		mTerminateResHandler = terminateResHandler;
		if(mStatus==STATUS_DISPATCH_ASYNC_TIMEOUT)
		{
			VLDebug.Assert(mAsyncTimeoutBlock!=null);
			VLDebug.Assert(mScheduler.cancel(mAsyncTimeoutBlock,true));
		}
		if(mStatus==STATUS_IDLE) mStatus = STATUS_READY;
		if(mDelayScheduleBlcok!=null)
		{
			VLDebug.Assert(mStatus==STATUS_DISPATCH_NEXT);
			VLScheduler.instance.cancel(mDelayScheduleBlcok, true);
			mDelayScheduleBlcok = null;
		}
		loop();
		return true;
	}

	private void loop()
	{
		VLScheduler.instance.schedule(0, mScheduleThread, new VLBlock()
		{
			@Override
			protected void process(boolean canceled)
			{
				innerloop();
			}
		});
	}

	private synchronized void innerloop()
	{
		if(mStatus!=STATUS_READY && mStatus!=STATUS_RERUN_READY) return;//loop只处理STATUS_READY的状态
		if(mParams.size()>0)
		{
			if(!mTerminating)
			{//分派到工作线程执行任务
				if(!mServicing) return;
				mParam = mParams.remove(0);
				mResHandler = mResHandlers.remove(0);
				int delay = (mStatus==STATUS_READY ? mDelayInMs : 0);
				mStatus = STATUS_DISPATCH_NEXT;
				mDelayScheduleBlcok = mScheduler.schedule(delay, mScheduleThread, new VLBlock()
				{//分派到工作线程
					public void process(boolean canceled)
					{
						Object param = null;
						synchronized(VLTask.this)
						{
							mDelayScheduleBlcok = null;
							VLDebug.Assert(mStatus==STATUS_DISPATCH_NEXT);
							mStatus = STATUS_HANDLE_NEXT;
							param = mParam;

							if(canceled)
							{
								notifyFinish(false, null, "schedule canceled");
								return;
							}
						}
						try
						{
							doTask(param);
						}
						catch(Throwable e)
						{
							VLDebug.Assert(false, e);
						}
					}
				});
			}
			else
			{//terminating,分派到主线程通知任务关闭
				mParam = mParams.remove(0);
				mResHandler = mResHandlers.remove(0);
				if(mResHandler==null)
				{
					mStatus = STATUS_READY;
					loop();
					return;
				}
				else
				{
					mStatus = STATUS_DISPATCH_NEXT;
					mScheduler.schedule(0, mResHandler.resThread(), new VLBlock()
					{
						public void process(boolean canceled)
						{
							synchronized(VLTask.this)
							{
								VLDebug.Assert(canceled==false && mStatus==STATUS_DISPATCH_NEXT);
								mStatus = STATUS_HANDLE_NEXT;
								try
								{
									mResHandler.handlerError(VLResHandler.SHUTDOWN, null);
								}
								catch(Throwable e)
								{
									VLDebug.Assert(false, e);
								}
								mStatus = STATUS_READY;
								loop();
							}
						}
					});
				}
			}
		}
		else
		{//队列执行完
			VLDebug.Assert(mStatus==STATUS_READY);
			if(mTerminating==false)
			{
				mStatus = STATUS_IDLE;
			}
			else
			{//terminating...
				if(mTerminateResHandler!=null)
				{
					mStatus = STATUS_DISPATCH_TERMINATE;
					mScheduler.schedule(0, mTerminateResHandler.resThread(), new VLBlock()
					{
						public void process(boolean canceled)
						{
							synchronized(VLTask.this)
							{
								VLDebug.Assert(canceled==false && mStatus==STATUS_DISPATCH_TERMINATE);
								try
								{
									mTerminateResHandler.handlerSuccess();
								}
								catch(Throwable e)
								{
									VLDebug.Assert(false, e);
								}
								mTerminateResHandler = null;
								mStatus = STATUS_TERMINATED;
							}
						}
					});
				}
				else
					mStatus = STATUS_TERMINATED;
			}
		}
	}

	public synchronized void notifyFinish(final boolean succeed, final Object param, final String errorMsg)
	{
		VLDebug.Assert(mStatus == STATUS_HANDLE_NEXT || mStatus==STATUS_HANDLE_ASYNC_TIMEOUT || mStatus==STATUS_HANDLE_ASYNC_FINISH);
		if(mResHandler==null)
		{//无需通知结果
			int pending = mParams.size();
			if((mMode==MODE_SINGLETON_MERGE && pending>0) || (mMode==MODE_SINGLETON_RELAST && pending>1))
			{//合并所有正在运行的，通知CONCURRENT错误
				if(mMode==MODE_SINGLETON_RELAST) pending--;
				for(int i=0; i<pending; i++)
				{
					mParams.remove(0);
					VLResHandler resHandler = mResHandlers.remove(0);
					if(resHandler!=null) resHandler.handlerError(VLResHandler.CONCURRENT, null);
				}
			}
			mStatus = STATUS_READY;
			loop();
			return;
		}
		else
		{//结果通知
			mStatus = STATUS_DISPATCH_FINISH;
			mScheduler.schedule(0, mResHandler.resThread(), new VLBlock()
			{
				public void process(boolean canceled)
				{
					synchronized (VLTask.this)
					{
						VLDebug.Assert(canceled==false && mStatus==STATUS_DISPATCH_FINISH);
						mStatus = STATUS_HANDLE_FINISH;
						try
						{
							mResHandler.setParam(param);
							try
							{
								if(succeed)
									mResHandler.handlerSuccess();
								else
									mResHandler.handlerError(VLResHandler.ERROR, errorMsg);
							}
							catch(Throwable e)
							{
								VLDebug.Assert(false, e);
							}
						}
						catch(Throwable e)
						{
							VLDebug.Assert(false, e);
						}
						int pending = mParams.size();
						if((mMode==MODE_SINGLETON_MERGE && pending>0) || (mMode==MODE_SINGLETON_RELAST && pending>1))
						{//合并所有正在运行的，通知CONCURRENT错误
							if(mMode==MODE_SINGLETON_RELAST) pending--;
							for(int i=0; i<pending; i++)
							{
								mParams.remove(0);
								VLResHandler resHandler = mResHandlers.remove(0);
								if(resHandler!=null) resHandler.handlerError(VLResHandler.CONCURRENT, null);
							}
						}
						mStatus = STATUS_READY;
						loop();
						return;
					}
				}
			});
		}
	}

	public synchronized void notifyAsyncTimeout(int timeoutInMs,VLResHandler asyncResHandler)
	{
		VLDebug.Assert(mStatus == STATUS_HANDLE_NEXT || mStatus==STATUS_HANDLE_ASYNC_TIMEOUT || mStatus==STATUS_HANDLE_ASYNC_FINISH);
		VLDebug.Assert(asyncResHandler!=null && mAsyncTimeoutBlock==null && mAsyncResHandler==null);
		mStatus = STATUS_DISPATCH_ASYNC_TIMEOUT;
		mAsyncResHandler = asyncResHandler;
		mAsyncTimeoutBlock = new VLBlock()
		{
			public void process(boolean canceled)
			{//真timeout了(canceled==false)或者等待timeout过程中requestTerminate(canceled==true),（被调用notifyAsyncFinish时不通知，所以理论上应该不会来到这里)
				synchronized(VLTask.this)
				{
					VLResHandler resHandler = mAsyncResHandler;
					if(mStatus!=STATUS_DISPATCH_ASYNC_TIMEOUT) return;//被cancel后改了状态但同时刚好timeout进入？
					VLDebug.Assert(mStatus==STATUS_DISPATCH_ASYNC_TIMEOUT && mAsyncTimeoutBlock!=null && mAsyncResHandler!=null);
					mStatus = STATUS_HANDLE_ASYNC_TIMEOUT;
					mAsyncTimeoutBlock = null;
					mAsyncResHandler = null;
					try
					{//现在状态为STATUS_HANDLE_ASYNC_TIMEOUT，asyncResHandler内必须调用notifyXXX继续下一状态，否则抛异常
						int errorCode = (canceled ? VLResHandler.SHUTDOWN : VLResHandler.TIMEOUT);//被terminate情形或真timeout情形
						resHandler.handlerError(errorCode, null, new VLResHandler(mScheduleThread)
						{
							@Override
							protected void handler(boolean succeed)
							{
								VLDebug.Assert(mStatus!=STATUS_DISPATCH_ASYNC_TIMEOUT);
							}
						});
					}
					catch(Throwable e)
					{
						VLDebug.Assert(false, e);
					}
				}
			}
		};
		mScheduler.schedule(timeoutInMs, mScheduleThread, mAsyncTimeoutBlock);
	}

	public synchronized void notifyAsyncFinish(final boolean succeed, final Object param, final String errorMsg)
	{
		if(mStatus!=STATUS_DISPATCH_ASYNC_TIMEOUT) return;//有可能已经timeout过了
		VLDebug.Assert(mStatus==STATUS_DISPATCH_ASYNC_TIMEOUT && mAsyncResHandler!=null && mAsyncTimeoutBlock!=null);
		VLDebug.Assert(mScheduler.cancel(mAsyncTimeoutBlock,false));
		mStatus = STATUS_DISPATCH_ASYNC_FINISH;
		mScheduler.schedule(0, mScheduleThread, new VLBlock()
		{
			public void process(boolean canceled)
			{
				synchronized(VLTask.this)
				{
					VLResHandler resHandler = mAsyncResHandler;
					VLDebug.Assert(canceled==false && mStatus==STATUS_DISPATCH_ASYNC_FINISH && mAsyncTimeoutBlock!=null && mAsyncResHandler!=null);
					mStatus = STATUS_HANDLE_ASYNC_FINISH;
					mAsyncTimeoutBlock = null;
					mAsyncResHandler = null;
					try
					{
						resHandler.setParam(param);
						if(succeed)
							resHandler.handlerSuccess();
						else
							resHandler.handlerError(VLResHandler.ERROR, errorMsg);
					}
					catch(Throwable e)
					{
						VLDebug.Assert(false, e);
					}
				}
			}
		});
	}
	
	public synchronized void notifyRerun(final Object param)
	{
		VLDebug.Assert(mStatus==STATUS_HANDLE_NEXT || mStatus==STATUS_HANDLE_ASYNC_TIMEOUT || mStatus==STATUS_HANDLE_ASYNC_FINISH);
		mParams.add(0, param);
		mResHandlers.add(0, mResHandler);
		mStatus = STATUS_RERUN_READY;
		loop();
	}
	
	protected abstract void doTask(final Object param);
}
