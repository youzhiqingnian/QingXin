package com.vlee78.android.vl;

import android.os.Handler;
import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

public class VLHttpClient 
{
	private int mTaskId;
	private SparseArray<VLHttpTask> mTasks;
	private String mReqUserAgent;
	private String mReqAccept;
	private String mReqAcceptEncoding;

	public VLHttpClient()
	{
		mTaskId = 0; 
		mTasks = new SparseArray<>();
		mReqUserAgent = "Mozilla/5.0 (compatible)";
		mReqAccept = "*/*";
		mReqAcceptEncoding = "gzip";
	}
	
	private static class VLHttpTask
	{
		public static final int STATE_INIT 			= 0;
		public static final int STATE_SCHEDULED 	= 1;
		public static final int STATE_REQUESTED		= 2;
		public static final int STATE_RESPONSED		= 3;
		public static final int STATE_FINISHED		= 4;
		
		public static final int ERROR_NONE			= 0;
		public static final int ERROR_USER_CANCEL	= 1;
		public static final int ERROR_EXCEPTION		= 2;
		public static final int ERROR_OUTPUT_FILE	= 3;
		public static final int ERROR_SERVER_FAILED	= 4;
		
		public int mTaskId;
		public int mState;
		public boolean mCancelFlag;
		public List<VLResHandler> mCancelListeners;
	}

	public synchronized boolean httpCancelTask(int taskId, VLResHandler resHandler)
	{
		VLHttpTask httpTask = mTasks.get(taskId);
		if(httpTask==null || httpTask.mState== VLHttpTask.STATE_FINISHED) return false;
		if(resHandler!=null)
		{
			if(httpTask.mCancelListeners==null) httpTask.mCancelListeners = new ArrayList<>();
			httpTask.mCancelListeners.add(resHandler);
		}
		httpTask.mCancelFlag = true;
		return true;
	}

	public interface VLHttpFileDownloadListener
	{
		void onResProgress(int currentLength, int contentLength);
	}

	public synchronized int httpFileDownloadTask(final boolean isFollowRedirect, final String url, final String filepath, final int bufferLength, final VLHttpFileDownloadListener listener, final VLAsyncHandler<Object> asyncHandler)
	{
		int taskId = ++mTaskId;
		final VLHttpTask httpTask = new VLHttpTask();
		mTasks.put(taskId, httpTask);
		httpTask.mTaskId = taskId;
		httpTask.mState = VLHttpTask.STATE_INIT;
		httpTask.mCancelFlag = false;
		httpTask.mCancelListeners = null;

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
		 		HttpURLConnection urlConnection = null;
		 		OutputStream os = null;
		 		InputStream is = null;
				try
				{
					os = VLUtils.fileOpenToWrite(filepath);
					if(os==null)
					{
						finish(VLHttpTask.ERROR_OUTPUT_FILE, "open output file failed : " + filepath, os, is, asyncHandler);
						return;
					}

					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, asyncHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_SCHEDULED;

					urlConnection = (HttpURLConnection)(new URL(url)).openConnection();
					urlConnection.setDoOutput(false);
					urlConnection.setDoInput(true);
					urlConnection.setInstanceFollowRedirects(isFollowRedirect);
					urlConnection.setRequestProperty("User-Agent", mReqUserAgent);
					urlConnection.setRequestProperty("Accept", mReqAccept);
					urlConnection.setRequestProperty("Accept-Encoding", mReqAcceptEncoding);

					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, asyncHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_REQUESTED;

					int resCode = urlConnection.getResponseCode();
					int contentLength = urlConnection.getContentLength();
					String contentEncoding = VLUtils.V(urlConnection.getContentEncoding()).trim().toLowerCase(Locale.getDefault());

					if(resCode!=200)
					{
						finish(VLHttpTask.ERROR_SERVER_FAILED, "server error : " + resCode, os, is, asyncHandler);
						return;
					}
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, asyncHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_RESPONSED;

					is = urlConnection.getInputStream();
					if(contentEncoding.equals("gzip")) is = new GZIPInputStream(is);

					byte[] buffer = new byte[bufferLength];
					int currentLength = 0;
					while(!httpTask.mCancelFlag)
					{
						int len = is.read(buffer);
						if(len==-1) break;
						currentLength += len;
						os.write(buffer, 0, len);
						if(listener!=null) listener.onResProgress(currentLength, contentLength);
					}
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, asyncHandler);
						return;
					}
					finish(VLHttpTask.ERROR_NONE, "succeed", os, is, asyncHandler);
				}
				catch(Exception e)
				{
					finish(VLHttpTask.ERROR_EXCEPTION, e.getMessage(), os, is, asyncHandler);
				}
				finally
				{
					if(urlConnection!=null) urlConnection.disconnect();
					mTasks.remove(httpTask.mTaskId);
				}
			}

			public void finish(int errorCode, String errorMsg, OutputStream fos, InputStream is, VLAsyncHandler<Object> asyncHandler)
			{
				try
				{
					if(fos!=null) fos.close();
					if(is!=null) is.close();
				}
				catch(Exception e)
				{

				}

				httpTask.mState = VLHttpTask.STATE_FINISHED;
				if(errorCode== VLHttpTask.ERROR_NONE)
				{
					if(asyncHandler!=null) asyncHandler.handlerSuccess();
				}
				else if(errorCode== VLHttpTask.ERROR_USER_CANCEL)
				{
					if(asyncHandler!=null) asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResCanceled, "user canceled");
					if(httpTask.mCancelListeners!=null)
					{
						for(VLResHandler cancelListener : httpTask.mCancelListeners)
						{
							if(cancelListener!=null) cancelListener.handlerSuccess();
						}
					}
				}
				else
				{
					if(asyncHandler!=null) asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, errorMsg);
				}
			}

		}).start();
		return taskId;
	}

	public synchronized int httpPostTask(Handler handler, final String url, final String contentType, final byte[] body, final VLResHandler resHandler)
	{
		int taskId = ++mTaskId;
		final VLHttpTask httpTask = new VLHttpTask();
		mTasks.put(taskId, httpTask);
		httpTask.mTaskId = taskId;
		httpTask.mState = VLHttpTask.STATE_INIT;
		httpTask.mCancelFlag = false;
		httpTask.mCancelListeners = null;

		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
		 		HttpURLConnection urlConnection = null;
		 		ByteArrayOutputStream baos = null;
		 		InputStream is = null;
		 		OutputStream os = null;
				try
				{
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", baos, is, os, resHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_SCHEDULED;

					urlConnection = (HttpURLConnection)(new URL(url)).openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					urlConnection.setInstanceFollowRedirects(false);
					urlConnection.setConnectTimeout(30*1000);
					urlConnection.setRequestProperty("User-Agent", mReqUserAgent);
					urlConnection.setRequestProperty("Accept", mReqAccept);
					urlConnection.setRequestProperty("Accept-Encoding", mReqAcceptEncoding);
					urlConnection.setRequestProperty("Content-Type", contentType);

					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", baos, is, os, resHandler);
						return;
					}

					os = urlConnection.getOutputStream();
					os.write(body);
					os.close();
					os = null;

					httpTask.mState = VLHttpTask.STATE_REQUESTED;
					int resCode = urlConnection.getResponseCode();
					String contentEncoding = VLUtils.V(urlConnection.getContentEncoding()).trim().toLowerCase(Locale.getDefault());

					if(resCode!=200)
					{
						finish(VLHttpTask.ERROR_SERVER_FAILED, "server error : " + resCode, baos, is, os, resHandler);
						return;
					}
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", baos, is, os, resHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_RESPONSED;

					is = urlConnection.getInputStream();
					if(contentEncoding.equals("gzip")) is = new GZIPInputStream(is);

					baos = new ByteArrayOutputStream();
					int bufferLength = 4096;
					byte[] buffer = new byte[bufferLength];
					while(!httpTask.mCancelFlag)
					{
						int len = is.read(buffer);
						if(len==-1) break;
						baos.write(buffer, 0, len);
					}
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", baos, is, os, resHandler);
						return;
					}
					finish(VLHttpTask.ERROR_NONE, "succeed", baos, is, os, resHandler);
				}
				catch(Exception e)
				{
					finish(VLHttpTask.ERROR_EXCEPTION, e.getMessage(), baos, is, os, resHandler);
				}
				finally
				{
					if(urlConnection!=null) urlConnection.disconnect();
					mTasks.remove(httpTask.mTaskId);
				}
			}

			public void finish(int errorCode, String errorMsg, ByteArrayOutputStream baos, InputStream is, OutputStream os, VLResHandler resHandler)
			{
				try
				{
					if(baos!=null) baos.close();
					if(is!=null) is.close();
					if(os!=null) os.close();
				}
				catch(Exception e)
				{
				}

				httpTask.mState = VLHttpTask.STATE_FINISHED;
				if(errorCode== VLHttpTask.ERROR_NONE)
				{
					byte[] bytes = baos.toByteArray();
					resHandler.setParam(bytes);
					resHandler.handlerSuccess();
				}
				else if(errorCode== VLHttpTask.ERROR_USER_CANCEL)
				{
					if(resHandler!=null) resHandler.handlerError(VLResHandler.CANCEL, "user canceled");
					if(httpTask.mCancelListeners!=null)
					{
						for(VLResHandler cancelListener : httpTask.mCancelListeners)
						{
							if(cancelListener!=null) cancelListener.handlerSuccess();
						}
					}
				}
				else
				{
					if(resHandler!=null) resHandler.handlerError(VLResHandler.ERROR, errorMsg);
				}
			}
		});
		return taskId;
	}

	public synchronized int httpGetTask(Handler handler, final String url, final VLResHandler resHandler)
	{
		int taskId = ++mTaskId;
		final VLHttpTask httpTask = new VLHttpTask();
		mTasks.put(taskId, httpTask);
		httpTask.mTaskId = taskId;
		httpTask.mState = VLHttpTask.STATE_INIT;
		httpTask.mCancelFlag = false;
		httpTask.mCancelListeners = null;

		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
		 		HttpURLConnection urlConnection = null;
		 		ByteArrayOutputStream os = null;
		 		InputStream is = null;
				try
				{
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, resHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_SCHEDULED;

					urlConnection = (HttpURLConnection)(new URL(url)).openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(false);
					urlConnection.setDoInput(true);
					urlConnection.setInstanceFollowRedirects(false);
					urlConnection.setConnectTimeout(30*1000);
					urlConnection.setRequestProperty("User-Agent", mReqUserAgent);
					urlConnection.setRequestProperty("Accept", mReqAccept);
					urlConnection.setRequestProperty("Accept-Encoding", mReqAcceptEncoding);

					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, resHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_REQUESTED;

					int resCode = urlConnection.getResponseCode();
					String contentEncoding = VLUtils.V(urlConnection.getContentEncoding()).trim().toLowerCase(Locale.getDefault());

//					for(Map.Entry<String, List<String>> entry : urlConnection.getHeaderFields().entrySet())
//					{
//						VLDebug.logE(entry.getKey() + " : " + entry.getValue().get(0));
//					}

					if(resCode!=200)
					{
						finish(VLHttpTask.ERROR_SERVER_FAILED, "server error : " + resCode, os, is, resHandler);
						return;
					}
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, resHandler);
						return;
					}
					httpTask.mState = VLHttpTask.STATE_RESPONSED;

					is = urlConnection.getInputStream();
					if(contentEncoding.equals("gzip")) is = new GZIPInputStream(is);

					os = new ByteArrayOutputStream();
					int bufferLength = 4096;
					byte[] buffer = new byte[bufferLength];
					while(!httpTask.mCancelFlag)
					{
						int len = is.read(buffer);
						if(len==-1) break;
						os.write(buffer, 0, len);
					}
					if(httpTask.mCancelFlag)
					{//取消运行
						finish(VLHttpTask.ERROR_USER_CANCEL, "user canceled", os, is, resHandler);
						return;
					}
					finish(VLHttpTask.ERROR_NONE, "succeed", os, is, resHandler);
				}
				catch(Exception e)
				{
					finish(VLHttpTask.ERROR_EXCEPTION, e.getMessage(), os, is, resHandler);
				}
				finally
				{
					if(urlConnection!=null) urlConnection.disconnect();
					mTasks.remove(httpTask.mTaskId);
				}
			}

			public void finish(int errorCode, String errorMsg, ByteArrayOutputStream baos, InputStream is, VLResHandler resHandler)
			{
				try
				{
					if(baos!=null) baos.close();
					if(is!=null) is.close();
				}
				catch(Exception e)
				{
				}

				httpTask.mState = VLHttpTask.STATE_FINISHED;
				if(errorCode== VLHttpTask.ERROR_NONE)
				{
					byte[] bytes = baos.toByteArray();
					resHandler.setParam(bytes);
					resHandler.handlerSuccess();
				}
				else if(errorCode== VLHttpTask.ERROR_USER_CANCEL)
				{
					if(resHandler!=null) resHandler.handlerError(VLResHandler.CANCEL, "user canceled");
					if(httpTask.mCancelListeners!=null)
					{
						for(VLResHandler cancelListener : httpTask.mCancelListeners)
						{
							if(cancelListener!=null) cancelListener.handlerSuccess();
						}
					}
				}
				else
				{
					if(resHandler!=null) resHandler.handlerError(VLResHandler.ERROR, errorMsg);
				}
			}
		});
		return taskId;
	}
}
