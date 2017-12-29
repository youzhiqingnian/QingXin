package com.vlee78.android.vl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class VLScheduler {
    protected static class BlockItem implements Runnable {
        public static final int STATUS_IDLED = 0;
        public static final int STATUS_SCHEDULED = 1;
        public static final int STATUS_RUNNING = 2;
        public static final int STATUS_CANCELED = 3;

        public VLBlock mBlock = null;
        public Handler mHandler = null;
        public int mStatus = STATUS_IDLED;

        public void run() {
            synchronized (instance) {
                if (mStatus != BlockItem.STATUS_SCHEDULED) return;
                mStatus = STATUS_RUNNING;
            }
            if (mBlock != null) {
                try {
                    mBlock.process(false);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            VLDebug.Assert(mStatus == BlockItem.STATUS_RUNNING);
            synchronized (instance) {
                instance.reuseBlockItem(this);
            }
        }
    }

    public static final VLScheduler instance = new VLScheduler();
    private List<BlockItem> mBlockItems = new ArrayList<>();
    private int mBlocksCount = 0;
    private Handler mMainHandler = null;
    private HandlerThread mBgHighThread = new HandlerThread("high_" + this.toString());
    private Handler mBgHighHandler = null;
    private HandlerThread mBgNormalThread = new HandlerThread("normal_" + this.toString());
    private Handler mBgNormalHandler = null;
    private HandlerThread mBgLowThread = new HandlerThread("low_" + this.toString());
    private Handler mBgLowHandler = null;
    private HandlerThread mBgIdleThread = new HandlerThread("idle_" + this.toString());
    private Handler mBgIdleHandler = null;

    public static final int THREAD_MAIN = 0;
    public static final int THREAD_BG_HIGH = 1;
    public static final int THREAD_BG_NORMAL = 2;
    public static final int THREAD_BG_LOW = 3;
    public static final int THREAD_BG_IDLE = 4;

    public long getThreadId(int thread) {
        if (thread == THREAD_MAIN)
            return Looper.getMainLooper().getThread().getId();
        else if (thread == THREAD_BG_HIGH)
            return mBgHighThread.getThreadId();
        else if (thread == THREAD_BG_NORMAL)
            return mBgNormalThread.getThreadId();
        else if (thread == THREAD_BG_LOW)
            return mBgLowThread.getThreadId();
        else if (thread == THREAD_BG_IDLE)
            return mBgIdleThread.getThreadId();
        else
            return -1;
    }

    public synchronized Handler getHandler(int scheduleThread) {
        if (scheduleThread == THREAD_MAIN) {
            if (mMainHandler == null)
                mMainHandler = new Handler(Looper.getMainLooper());
            return mMainHandler;
        } else if (scheduleThread == THREAD_BG_HIGH) {
            if (mBgHighHandler == null) {
                mBgHighThread.start();
                mBgHighThread.setPriority(Thread.MAX_PRIORITY);
                mBgHighHandler = new Handler(mBgHighThread.getLooper());
            }
            VLDebug.Assert(mBgHighThread != null);
            return mBgHighHandler;
        } else if (scheduleThread == THREAD_BG_NORMAL) {
            if (mBgNormalHandler == null) {
                mBgNormalThread.start();
                mBgNormalThread.setPriority(Thread.NORM_PRIORITY);
                mBgNormalHandler = new Handler(mBgNormalThread.getLooper());
            }
            VLDebug.Assert(mBgNormalThread != null);
            return mBgNormalHandler;
        } else if (scheduleThread == THREAD_BG_LOW) {
            if (mBgLowHandler == null) {
                mBgLowThread.start();
                mBgLowThread.setPriority(Thread.MIN_PRIORITY);
                mBgLowHandler = new Handler(mBgLowThread.getLooper());
            }
            VLDebug.Assert(mBgLowThread != null);
            return mBgLowHandler;
        } else if (scheduleThread == THREAD_BG_IDLE) {
            if (mBgIdleHandler == null) {
                mBgIdleThread.start();
                mBgIdleThread.setPriority(Thread.MIN_PRIORITY);
                mBgIdleHandler = new Handler(mBgIdleThread.getLooper());
            }
            VLDebug.Assert(mBgIdleThread != null);
            return mBgIdleHandler;
        } else VLDebug.Assert(false);
        return null;
    }

    public void run(int scheduleThread, VLBlock block) {
        if (Thread.currentThread().getId() == getThreadId(scheduleThread))
            block.process(false);
        else
            schedule(0, THREAD_MAIN, block);
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
        if (delayInMs == 0)
            blockItem.mHandler.post(blockItem);
        else
            blockItem.mHandler.postDelayed(blockItem, delayInMs);
        return block;
    }

    private void allocBlockItem(BlockItem blockItem, VLBlock block, int scheduleThread) {
        blockItem.mBlock = block;
        blockItem.mHandler = getHandler(scheduleThread);
        blockItem.mStatus = BlockItem.STATUS_SCHEDULED;
        blockItem.mBlock.mRefBlockItem = blockItem;
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
        if (block == null || block.mRefBlockItem == null) return false;
        final BlockItem blockItem = block.mRefBlockItem;
        if (blockItem.mBlock != block || blockItem.mStatus != BlockItem.STATUS_SCHEDULED)
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
