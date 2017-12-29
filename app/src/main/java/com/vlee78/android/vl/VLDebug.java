package com.vlee78.android.vl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class VLDebug
{
	public enum VLLogLevel
	{
		None, Error, Warning, Info, Debug, Verbose
	}
	
	private static VLLogLevel		sLogLevel			= VLLogLevel.None;
	private static String[]			sLogLevelDesc		= new String[] { "N", "E", "W", "I", "D", "V" };
	private static String sLogDefaultTag		= "VLDebug";
	private static long				sLogRotateBytes		= Long.MAX_VALUE;
	private static long				sLogPreserveMs		= Long.MAX_VALUE;
	private static SimpleDateFormat sLogDateFormat		= new SimpleDateFormat("MMdd_kkmmss.SSS", Locale.getDefault());
	private static FileWriter sLogWriter			= null;
	private static File sLogsDir			= null;
		
	public static final synchronized void configDebug(Context context, VLLogLevel logLevel, long logRotateBytes, long logPreserveMs)
	{
		sLogDefaultTag = context.getPackageName();
		sLogLevel = logLevel;
		sLogRotateBytes = (logRotateBytes<=0 ? Long.MAX_VALUE : logRotateBytes);
		sLogPreserveMs = (logPreserveMs<=0 ? Long.MAX_VALUE : logPreserveMs);
		if(!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) return;
		File logsDir = context.getExternalFilesDir(null);
		if(logsDir==null || logsDir.isFile()) return;
		logsDir = logsDir.getParentFile();
		if(logsDir==null || logsDir.isFile()) return;
		logsDir = new File(logsDir.getAbsolutePath() + File.separator + "logs");
		if(logsDir.isFile() || (!logsDir.exists() && !logsDir.mkdirs())) return;
		sLogsDir = logsDir;
		for(File file : logsDir.listFiles())
		{
			long now = System.currentTimeMillis();
			if(now-file.lastModified() > sLogPreserveMs) file.delete();
		}
		String appName = context.getPackageName();
		appName = appName.lastIndexOf('.')<0 ? appName : appName.substring(appName.lastIndexOf('.')+1);
		String logFilePath = logsDir.getAbsolutePath() + File.separator + appName + "_log.txt";
		try
		{
			File logFile = new File(logFilePath);
			if(!logFile.exists()) logFile.createNewFile();
			else if(logFile.length() > sLogRotateBytes)
			{
				String datetime = sLogDateFormat.format(new Date());
				String renameFilePath = logFilePath + "." + datetime + ".bak";
				logFile.renameTo(new File(renameFilePath));
				logFile = new File(logFilePath);
				if(!logFile.exists()) logFile.createNewFile();
			}
			sLogWriter = new FileWriter(logFile, true);
		}
		catch(Exception e)
		{
			sLogWriter = null;
		}
	}
	
	public static final String toDesc()
	{
		
		return "{logLevel=" + sLogLevel + ",logRotateBytes=" + sLogRotateBytes + ",logPreserveHours=" + (sLogPreserveMs/1000/60/60) + "}";
	}
	
	///////////////////////////////////////////////////////////////////////////

	public static final void logE(final String format, final Object... args)
	{
		if(VLLogLevel.Error.ordinal() > sLogLevel.ordinal()) return;
		log(true, VLLogLevel.Error, format, args);
	}

	public static final void logW(final String format, final Object... args)
	{
		if(VLLogLevel.Warning.ordinal() > sLogLevel.ordinal()) return;
		log(true, VLLogLevel.Warning, format, args);
	}

	public static final void logI(final String format, final Object... args)
	{
		if(VLLogLevel.Info.ordinal() > sLogLevel.ordinal()) return;
		log(true, VLLogLevel.Info, format, args);
	}

	public static final void logD(final String format, final Object... args)
	{
		if(VLLogLevel.Debug.ordinal() > sLogLevel.ordinal()) return;
		log(true, VLLogLevel.Debug, format, args);
	}

	public static final void logV(final String format, final Object... args)
	{
		if(VLLogLevel.Verbose.ordinal() > sLogLevel.ordinal()) return;
		log(true, VLLogLevel.Verbose, format, args);
	}
	
	public static final void screenE(final String format, final Object... args)
	{
		if(VLLogLevel.Error.ordinal() > sLogLevel.ordinal()) return;
		log(false, VLLogLevel.Error, format, args);
	}
	
	public static final void screenW(final String format, final Object... args)
	{
		if(VLLogLevel.Warning.ordinal() > sLogLevel.ordinal()) return;
		log(false, VLLogLevel.Warning, format, args);
	}

	public static final void screenI(final String format, final Object... args)
	{
		if(VLLogLevel.Info.ordinal() > sLogLevel.ordinal()) return;
		log(false, VLLogLevel.Info, format, args);
	}
	
	public static final void screenD(final String format, final Object... args)
	{
		if(VLLogLevel.Debug.ordinal() > sLogLevel.ordinal()) return;
		log(false, VLLogLevel.Debug, format, args);
	}
	
	public static final void screenV(final String format, final Object... args)
	{
		if(VLLogLevel.Verbose.ordinal() > sLogLevel.ordinal()) return;
		log(false, VLLogLevel.Verbose, format, args);
	}

	public static final void traceE()
	{
		logE("traceE");
	}

	public static final void traceW()
	{
		logW("traceW");
	}

	public static final void traceI()
	{
		logI("traceI");
	}

	public static final void traceD()
	{
		logD("traceD");
	}

	public static final void traceV()
	{
		logD("traceV");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static final void log(final boolean writeFile, final VLLogLevel level, final String format, final Object... args)
	{
		String className = Thread.currentThread().getStackTrace()[4].getFileName();
		if(className.length()>5) className = className.substring(0, className.length()-5);
		String msg = null;
		try
		{
			msg = className + ": " + String.format(format, args);
		}
		catch(Exception e)
		{
			msg = className + ": " + e.getMessage();
		}
		logToScreen(level, sLogDefaultTag, msg);
		if(writeFile && sLogWriter!=null) logToFile(level, sLogDefaultTag, msg);
	}
	
	private static final void logToScreen(final VLLogLevel level, final String tag, final String msg)
	{
		switch (level)
		{
		case None:
			break;
		case Error:
			Log.e(tag, msg);
			break;
		case Warning:
			Log.w(tag, msg);
			break;
		case Info:
			Log.i(tag, msg);
			break;
		case Debug:
			Log.d(tag, msg);
			break;
		case Verbose:
			Log.v(tag, msg);
			break;
		}
	}
	
	private static final void logToFile(final VLLogLevel level, String tag, String msg)
	{
		String datetime = sLogDateFormat.format(new Date());
		String loc = dumpStackTrace(getStackTraceLevel(2));
		String line = datetime + " " + sLogLevelDesc[level.ordinal()] + "/" + msg + "[" + loc + "]\r\n";
		try
		{
			sLogWriter.write(line);
			sLogWriter.flush();
		}
		catch (IOException e)
		{
			sLogWriter = null;
		}
	}
	
	
	public static final void logEx(final Thread thread, final Throwable th)
	{
		if(VLLogLevel.Error.ordinal() > sLogLevel.ordinal()) return;
		String msg = dumpExceptionStackTrace(thread, th);
		logToScreen(VLLogLevel.Error, sLogDefaultTag, msg);
		logToFile(VLLogLevel.Error, sLogDefaultTag, msg);
		logToEx(msg);
	}
	
	private static final void logToEx(String msg)
	{
		if(!VLUtils.externalStorageExist()) return;
		Date date = new Date();
		String datetime = sLogDateFormat.format(date);
		if(sLogsDir==null) return;
		String filePath = sLogsDir.getAbsolutePath() + File.separator + VLApplication.instance().appName() + "_" + datetime + ".txt";
		File logFile = new File(filePath);
		try
		{
			logFile.createNewFile();
			FileWriter fileWriter = new FileWriter(logFile, true);
			fileWriter.write(msg);
			fileWriter.flush();
			fileWriter.close();
		}
		catch (IOException e)
		{
			Log.e(sLogDefaultTag, filePath, e);
			return;
		}
		long now = System.currentTimeMillis();
		File files[] = sLogsDir.listFiles();
		for (File file : files)
		{
			if(file.getName().endsWith(".txt"))
			{
				long lastModifiedTime = file.lastModified();
				if(now-lastModifiedTime > sLogPreserveMs) file.delete();
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////

	public static final StackTraceElement getCurrentStackTrace()
	{
		return Thread.currentThread().getStackTrace()[3];
	}

	public static final StackTraceElement getParentStackTrace()
	{
		return Thread.currentThread().getStackTrace()[4];
	}

	public static final StackTraceElement getStackTraceLevel(int level)
	{
		return Thread.currentThread().getStackTrace()[4 + level];
	}

	public static final String dumpStackTrace(StackTraceElement stackTrace)
	{
		String className = stackTrace.getClassName();
		int i = className.lastIndexOf('.');
		if(i >= 0)
			className = className.substring(i + 1);
		return className + "::" + stackTrace.getMethodName() + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ")";
	}

	public static final String dumpExceptionStackTrace(Thread thread, Throwable th)
	{
		StringBuilder sb = new StringBuilder("Unhandled Exception In Thread :");
		sb.append("id=").append(thread.getId()).append(",");
		sb.append("name=").append(thread.getName()).append("\n");
		sb.append("exception=").append(th.getMessage()).append("\n");
		sb.append("Exception stacktaces : \n");
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		th.printStackTrace(printWriter);
		Throwable cause = th.getCause();
		while (cause != null)
		{
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		sb.append(writer.toString());
		return sb.toString();
	}

	public static final boolean Assert(boolean condition)
	{
		if(!condition)
		{
			logE(sLogDefaultTag + "Assert failed! " + dumpStackTrace(getParentStackTrace()));
			if(VLApplication.instance().appIsDebug()) throw new RuntimeException();
		}
		return condition;
	}
	
	public static final boolean Assert(boolean condition, Throwable e)
	{
		if(!condition)
		{
			String emsg = dumpExceptionStackTrace(Thread.currentThread(), e);
			logE(sLogDefaultTag + " Assert failed!");
			logE(sLogDefaultTag + " " + emsg);
			//if(VLDebug.DEBUG) throw new RuntimeException();
		}
		return condition;
	}
}
