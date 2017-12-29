package com.vlee78.android.vl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.SparseArray;

import com.vlee78.android.vl.VLAsyncHandler.VLAsyncRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VLScheduler {

    protected static class BlockItem implements Runnable {
        public static final int STATUS_IDLED = 0;
        public static final int STATUS_SCHEDULED = 1;
        public static final int STATUS_RUNNING = 2;
        public static final int STATUS_CANCELED = 3;
        public static final int STATUS_WAIT = 4;
        public VLBlock mBlock = null;
        public Handler mHandler = null;
        public int mStatus = STATUS_IDLED;
        public int mPriority = 0;

        public void run() {
            synchronized (instance) {
                if (mStatus == BlockItem.STATUS_WAIT) {
                    mStatus = BlockItem.STATUS_SCHEDULED;
                    instance.postToQueue(this);
                    return;
                }
                if (mStatus != BlockItem.STATUS_SCHEDULED) {
                    return;
                }
                mStatus = STATUS_RUNNING;
                if (mPriority == THREAD_BG_PROMPTLY) {
                    instance.promptlyExecutor(new Runnable() {
                        @Override
                        public void run() {
                            executor();
                        }
                    });
                    return;
                }
            }
            executor();
        }

        private void executor() {
            try {
                if (mBlock != null) {
                    try {
                        mBlock.process(false);
                    } catch (Throwable e) {
                        VLDebug.logE("mBlock.desc = " + mBlock.mCreateDesc);
                        VLDebug.Assert(false, e);
                    }
                }
                VLDebug.Assert(mStatus == BlockItem.STATUS_RUNNING);
                synchronized (instance) {
                    instance.reuseBlockItem(this);
                }
            } catch (Exception e) {
                VLDebug.Assert(false, e);
            }
        }
    }

    public static final VLScheduler instance = new VLScheduler();
    private List<BlockItem> mBlockItems = new ArrayList<>();
    private int mBlocksCount = 0;
    private Handler mMainHandler = null;
    private Handler mSchedulerHandler = null;

    public static final int THREAD_MAIN = 0;       //在UI中执行
    public static final int THREAD_BG_HIGH = 1;    //在后台线程池中执行优先级最高
    public static final int THREAD_BG_NORMAL = 2;  //在后台线程池中执行优先级低于THREAD_BG_HIGH
    public static final int THREAD_BG_LOW = 3;      //在后台线程池中执行优先级低于THREAD_BG_NORMAL
    public static final int THREAD_BG_IDLE = 4;     //在后台线程池中执行优先级低于THREAD_BG_LOW
    public static final int THREAD_BG_PROMPTLY = 6;  // 在后台立即执行

    private int mTaskSize = 0;
    private int mMaxTaskSize = 4;
    private Vector<BlockItem> mPriorityQueues = new Vector<>();
    private ExecutorService mExecutorService = Executors.newScheduledThreadPool(mMaxTaskSize);
    private ExecutorService mPromptlyService = Executors.newScheduledThreadPool(2);
    private final Object mTaskSizeLock = new Object();

    private class ThreadConfig {

        private volatile boolean mIsOrder = false;
        private volatile boolean mIsStartScheduler = false;


        public synchronized boolean isIsOrder() {
            return mIsOrder;
        }

        public synchronized void setIsOrder(boolean isOrder) {
            this.mIsOrder = isOrder;
        }

        public synchronized boolean isIsStartScheduler() {
            return mIsStartScheduler;
        }

        public synchronized void setIsStartScheduler(boolean isStartScheduler) {
            this.mIsStartScheduler = isStartScheduler;
        }
    }

    private volatile SparseArray<ThreadConfig> mThreadConfigMap;

    private void initThreadConfigMap() {
        mThreadConfigMap = new SparseArray<>();
        mThreadConfigMap.append(THREAD_BG_HIGH, new ThreadConfig());
        mThreadConfigMap.append(THREAD_BG_NORMAL, new ThreadConfig());
        mThreadConfigMap.append(THREAD_BG_LOW, new ThreadConfig());
        mThreadConfigMap.append(THREAD_BG_IDLE, new ThreadConfig());
    }


    private synchronized void promptlyExecutor(Runnable runnable) {
        mPromptlyService.execute(runnable);
    }

    private synchronized void postToQueue(BlockItem blockItem) {
        VLDebug.logD("VLScheduler:postToQueue" + "添加到执行队列");

        mPriorityQueues.addElement(blockItem);
        Collections.sort(mPriorityQueues, new Comparator<BlockItem>() {
            @Override
            public int compare(BlockItem lhs, BlockItem rhs) {
                return lhs.mPriority > rhs.mPriority ? 1 : -1;
            }
        });
        synchronized (mTaskSizeLock) {
            if (mTaskSize < mMaxTaskSize) {
                mTaskSize++;
                mExecutorService.execute(getExecutorRunnable());
            }
        }
    }

    private VLScheduler() {
        initThreadConfigMap();
        setTheadConfig(THREAD_BG_HIGH, true);
    }

    private synchronized BlockItem getNextBlockItem() {
        BlockItem blockIte = null;
        for (int i = 0; i < mPriorityQueues.size(); i++) {
            blockIte = mPriorityQueues.get(i);
            ThreadConfig threadConfig = mThreadConfigMap.get(blockIte.mPriority);
            if (threadConfig.isIsOrder()) {
                if (!threadConfig.isIsStartScheduler()) {
                    threadConfig.setIsStartScheduler(true);
                    break;
                }
            } else {
                break;
            }
        }
        if (null != blockIte) {
            mPriorityQueues.remove(blockIte);
        }
        return blockIte;
    }

    public void setTheadConfig(int scheduleThread, boolean isOrder) {
        mThreadConfigMap.get(scheduleThread).setIsOrder(isOrder);
    }

    public void setTheadSize(int theadSize) {
        mMaxTaskSize = theadSize;
    }

    private Runnable getExecutorRunnable() {

        return new Runnable() {
            public void run() {
                while (true) {
                    BlockItem blockItem = getNextBlockItem();
                    if (null != blockItem) {
                        ThreadConfig threadConfig = mThreadConfigMap.get(blockItem.mPriority);
                        VLDebug.logD("VLScheduler Executor 执行开始=" + blockItem + " " + blockItem.mPriority + " >> " + threadConfig.isIsOrder());
                        blockItem.run();
                        mThreadConfigMap.get(blockItem.mPriority).setIsStartScheduler(false);
                        VLDebug.logD("VLScheduler Executor 执行结束" + blockItem + " " + blockItem.mPriority + " >> " + threadConfig.isIsOrder());
                    } else {
                        synchronized (mTaskSizeLock) {
                            mTaskSize--;
                        }
                        return;
                    }
                }
            }
        };
    }


    public synchronized Handler getHandler(int scheduleThread) {
        if (scheduleThread == THREAD_MAIN) {
            if (mMainHandler == null) {
                mMainHandler = new Handler(Looper.getMainLooper());
            }
            return mMainHandler;

        } else if (scheduleThread == THREAD_BG_HIGH
                || scheduleThread == THREAD_BG_NORMAL
                || scheduleThread == THREAD_BG_LOW
                || scheduleThread == THREAD_BG_IDLE
                || scheduleThread == THREAD_BG_PROMPTLY) {
            if (mSchedulerHandler == null) {
                HandlerThread handlerThread = new HandlerThread("SchedulerHandler");
                handlerThread.start();
                mSchedulerHandler = new Handler(handlerThread.getLooper());
            }
            return mSchedulerHandler;

        } else VLDebug.Assert(false);
        return null;
    }

    public static class VLScheduleRepeater {
        private int mIndex;
        private int mTotal;
        private boolean mCanceled;

        public VLScheduleRepeater(int total) {
            mIndex = 0;
            mTotal = total;
            mCanceled = false;
        }

        public void setCanceled() {
            mCanceled = true;
        }

        public int getIndex() {
            return mIndex;
        }

        public int getTotal() {
            return mTotal;
        }

        public boolean getCanceled() {
            return mCanceled;
        }

        public boolean endOfSchedule() {
            return (mIndex >= mTotal || mCanceled);
        }
    }

    private synchronized void scheduleRepeatFun(final VLScheduleRepeater repeater, final int delayMs, final int intervalMs, final VLAsyncHandler<VLScheduleRepeater> asyncHandler) {
        if (asyncHandler != null && asyncHandler.isCancelled()) return;
        if (repeater.getCanceled()) {
            if (asyncHandler != null)
                asyncHandler.handlerError(VLAsyncRes.VLAsyncResCanceled, null);
            return;
        }
        if (repeater.getIndex() >= repeater.getTotal()) {
            if (asyncHandler != null) asyncHandler.handlerSuccess(repeater);
            return;
        }
        schedule((repeater.getIndex() == 0 ? delayMs : intervalMs), asyncHandler.getThread(), new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                if (repeater.getCanceled()) {
                    asyncHandler.handlerError(VLAsyncRes.VLAsyncResCanceled, null);
                    return;
                }
                if (repeater.getIndex() >= repeater.getTotal()) {
                    asyncHandler.handlerSuccess(repeater);
                    return;
                }
                asyncHandler.progress(repeater);
                repeater.mIndex++;
                scheduleRepeatFun(repeater, delayMs, intervalMs, asyncHandler);
            }
        });
    }

    public synchronized VLScheduleRepeater scheduleRepeat(int delayMs, int intervalMs, int total, VLAsyncHandler<VLScheduleRepeater> asyncHandler) {
        VLDebug.Assert(delayMs >= 0 && intervalMs >= 0 && total >= 0);
        VLScheduleRepeater repeater = new VLScheduleRepeater(total);
        scheduleRepeatFun(repeater, delayMs, intervalMs, asyncHandler);
        return repeater;
    }

    public synchronized VLBlock schedule(int delayInMs, int scheduleThread, VLBlock block) {
        VLDebug.Assert(block != null && block.mRefBlockItem == null && !block.mFlag && delayInMs >= 0);
        block.mFlag = true;
        BlockItem blockItem = null;
        for (BlockItem blockItem2 : mBlockItems) {
            if (blockItem2.mStatus == BlockItem.STATUS_IDLED) {
                blockItem = blockItem2;
                break;
            }
        }
        if (blockItem == null) {
            blockItem = new BlockItem();
            mBlockItems.add(blockItem);
        }
        allocBlockItem(blockItem, block, scheduleThread);

        if (scheduleThread == THREAD_BG_LOW || scheduleThread == THREAD_BG_HIGH || scheduleThread == THREAD_BG_NORMAL || scheduleThread == THREAD_BG_IDLE) {
            blockItem.mStatus = BlockItem.STATUS_WAIT;
        } else {
            blockItem.mStatus = BlockItem.STATUS_SCHEDULED;
        }
        if (delayInMs == 0)
            VLDebug.Assert(blockItem.mHandler.post(blockItem));
        else
            VLDebug.Assert(blockItem.mHandler.postDelayed(blockItem, delayInMs));
        return block;
    }

    private void allocBlockItem(BlockItem blockItem, VLBlock block, int scheduleThread) {
        blockItem.mBlock = block;
        blockItem.mHandler = getHandler(scheduleThread);
        blockItem.mBlock.mRefBlockItem = blockItem;
        blockItem.mPriority = scheduleThread;
        mBlocksCount++;
    }

    private void reuseBlockItem(BlockItem blockItem) {
        if (blockItem.mBlock != null) {
            blockItem.mBlock.mFlag = false;
            blockItem.mBlock.mRefBlockItem = null;
        }
        blockItem.mBlock = null;
        blockItem.mHandler = null;
        blockItem.mStatus = BlockItem.STATUS_IDLED;
        mBlocksCount--;
        VLDebug.Assert(mBlocksCount >= 0);
    }

    public synchronized boolean cancel(final VLBlock block, boolean notify) {

        VLDebug.logD("VLScheduler cancel  block = " + (block.mRefBlockItem != null));

        if (block.mRefBlockItem == null) return false;
        final BlockItem blockItem = block.mRefBlockItem;
        VLDebug.logD("VLScheduler cancel  = " + (blockItem.mBlock) + " : " + block + (blockItem.mBlock == block) + "  mStatus=" + blockItem.mStatus);

        if (blockItem.mBlock != block || (blockItem.mStatus != BlockItem.STATUS_SCHEDULED && blockItem.mStatus != BlockItem.STATUS_WAIT))
            return false;
        blockItem.mStatus = BlockItem.STATUS_CANCELED;
        blockItem.mHandler.removeCallbacks(blockItem);
        if (notify) {
            blockItem.mHandler.postAtFrontOfQueue(new Runnable() {
                public void run() {
                    try {
                        blockItem.mBlock.process(true);
                    } catch (Throwable e) {
                        VLDebug.Assert(false, e);
                    }
                    synchronized (this) {
                        reuseBlockItem(blockItem);
                    }
                }
            });
        } else {
            synchronized (this) {
                reuseBlockItem(blockItem);
            }
        }
        return true;
    }
}
