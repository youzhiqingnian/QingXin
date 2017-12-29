package com.vlee78.android.vl;

public abstract class VLBlock
{
	protected VLScheduler.BlockItem mRefBlockItem;
	protected boolean mFlag;
	protected String mCreateDesc;
	
	public VLBlock()
	{
		mRefBlockItem = null;
		mFlag = false;
		
		
		if(VLApplication.instance().appIsDebug())
		{
			StackTraceElement stackTraceElement1 = Thread.currentThread().getStackTrace()[3];
			StackTraceElement stackTraceElement2 = Thread.currentThread().getStackTrace()[4];
			mCreateDesc = stackTraceElement2.getClassName() + "::" + stackTraceElement2.getMethodName() 
						+ "(" + stackTraceElement2.getFileName() + ":" + stackTraceElement2.getLineNumber() + ") ** "
						+ stackTraceElement1.getClassName() + "::" + stackTraceElement1.getMethodName() 
						+ "(" + stackTraceElement1.getFileName() + ":" + stackTraceElement1.getLineNumber() + ")";
		}
		else mCreateDesc = "";
	}
	
	protected abstract void process(boolean canceled);
}
