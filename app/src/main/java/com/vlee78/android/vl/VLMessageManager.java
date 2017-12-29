package com.vlee78.android.vl;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class VLMessageManager {
	private SparseArray<List<VLMessageHandler>> mMessageHandlers;

	public interface VLMessageHandler {
		void onMessage(int msgId, Object msgParam);
	}

	public static class VLAsynHandlersRegistry {
		private boolean mFlag;
		private int mFinishCount;
		private List<VLAsyncHandler<?>> mAsyncHandlers;
		private VLAsyncHandler<Object> mFinishHandler;

		public VLAsynHandlersRegistry() {
			mFlag = false;
			mFinishCount = 0;
			mAsyncHandlers = new ArrayList<>();
			mFinishHandler = null;
		}

		public synchronized <T> VLAsyncHandler<T> registerAsyncHandler(Object holder, int thread) {
			VLDebug.Assert(!mFlag);
			VLAsyncHandler<T> asyncHandler = new VLAsyncHandler<T>(holder, thread) {
				@Override
				protected void handler(boolean succeed) {
					synchronized (VLAsynHandlersRegistry.this) {
						mFinishCount++;
						if (mFlag && mFinishCount == mAsyncHandlers.size())
							finish();
					}
				}
			};
			mAsyncHandlers.add(asyncHandler);
			return asyncHandler;
		}

		public synchronized void process(VLAsyncHandler<Object> asyncHandler) {
			VLDebug.Assert(!mFlag);
			mFlag = true;
			mFinishHandler = asyncHandler;
			if (mFinishCount == mAsyncHandlers.size())
				finish();
		}

		private void finish() {
			if (mFinishHandler != null) {
				mFinishHandler.handlerSuccess();
			}
		}
	}

	public VLMessageManager() {
		mMessageHandlers = new SparseArray<>();
	}

	public synchronized void registerMessageHandler(VLMessageHandler messageHandler, int... msgIds) {
		for (int msgId : msgIds) {
			List<VLMessageHandler> handlers = mMessageHandlers.get(msgId);
			if (handlers == null) {
				handlers = new ArrayList<>();
				mMessageHandlers.put(msgId, handlers);
			}
			VLDebug.Assert(!handlers.contains(messageHandler));
			handlers.add(messageHandler);
		}
	}

	public void unregisterMessageHandler(VLMessageHandler messageHandler) {
		for (int i = 0; i < mMessageHandlers.size(); i++) {
			List<VLMessageHandler> handlers = mMessageHandlers.valueAt(i);
			handlers.remove(messageHandler);
		}
	}

	public void broadcastMessage(final int msgId, final Object msgParam, final VLResHandler resHandler) {
		final List<VLMessageHandler> handlers = mMessageHandlers.get(msgId);
		if (null == handlers){
			return;
		}
		VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_PROMPTLY, new VLBlock() {
			@Override
			protected void process(boolean canceled) {
				for (final VLMessageHandler messageHandler : handlers) {
					VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
						@Override
						protected void process(boolean canceled) {
							messageHandler.onMessage(msgId, msgParam);
							if (resHandler != null)
								resHandler.handlerSuccess();
						}
					});
				}
			}
		});
	}
}
