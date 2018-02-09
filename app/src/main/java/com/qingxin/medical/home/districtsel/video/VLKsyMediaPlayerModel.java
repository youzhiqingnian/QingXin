package com.qingxin.medical.home.districtsel.video;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLModel;
import com.vlee78.android.vl.VLUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VLKsyMediaPlayerModel extends VLModel {

    private SparseArray<PlayerHolder> mRunningHolders = new SparseArray<>();
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    private void initMediaPlayer(final PlayerHolder playerHolder) {
        playerHolder.mMediaPlayer = new MediaPlayer();

        playerHolder.mSurfaceView = new SurfaceView(playerHolder.getFrameLayout().getContext());
        playerHolder.mSurfaceView.setLayoutParams(VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));

        playerHolder.mFrameLayout.removeAllViews();
        playerHolder.mFrameLayout.addView(playerHolder.mSurfaceView);
        playerHolder.mSurfaceHolder = playerHolder.mSurfaceView.getHolder();
        playerHolder.mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);

        playerHolder.mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (playerHolder.mMediaPlayer != null) {
                    playerHolder.mMediaPlayer.setDisplay(null);
                }
            }

            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                playerHolder.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        playerHolder.mIsVideoToBePlayed = true;
                        play();
                    }
                });

                playerHolder.mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        playerHolder.mIsVideoSizeKnown = true;
                        playerHolder.mVideoWidth = width;
                        playerHolder.mVideoHeight = height;
                        if (playerHolder.mSurfaceHolder != null)
                            playerHolder.mSurfaceHolder.setFixedSize(playerHolder.mVideoWidth, playerHolder.mVideoHeight);
                        play();
                    }
                });

                playerHolder.mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        playerHolder.mMediaPlayer.start();
                        if (playerHolder.mOnVideoPlayListener != null)
                            playerHolder.mOnVideoPlayListener.onSeekComplete();
                    }
                });
                playerHolder.mMediaPlayer.setOnErrorListener(buildOnErrorListener(playerHolder));
                playerHolder.mMediaPlayer.setOnInfoListener(buildOnInfoListener(playerHolder));
                playerHolder.mMediaPlayer.setDisplay(playerHolder.mSurfaceHolder);
                try {
                    playerHolder.mMediaPlayer.setDataSource(playerHolder.mUrl);
                    playerHolder.mMediaPlayer.prepareAsync();
                } catch (Exception e) {
                    VLDebug.logEx(Thread.currentThread(), e);
                }
                playerHolder.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (playerHolder.mOnVideoPlayListener != null)
                            playerHolder.mOnVideoPlayListener.onComplete();
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void play() {
                if (playerHolder.mIsVideoSizeKnown && playerHolder.mIsVideoToBePlayed) {
                    playerHolder.mMediaPlayer.setScreenOnWhilePlaying(true);
                    playerHolder.mIsVideoToBePlayed = false;
                    if (playerHolder.mOnVideoPlayListener != null)
                        playerHolder.mOnVideoPlayListener.onStart(playerHolder.mVideoWidth, playerHolder.mVideoHeight);
                    if (playerHolder.mMediaPlayer != null)
                        playerHolder.mMediaPlayer.start();
                }
            }
        });
    }

    private void closePlayersExcept(int playerId) {
        for (int i = 0; i < mRunningHolders.size(); i++) {
            PlayerHolder holder = mRunningHolders.valueAt(i);
            if (null != holder && holder.mId != playerId) {
                this.release(holder, false);
                mRunningHolders.remove(i);
                break;
            }
        }
    }

    public int start(PlayerHolder holder, boolean closeOtherRunningPlayers) {
        assert holder != null;
        VLDebug.Assert(holder.getUrl() != null);
        VLDebug.Assert(holder.getFrameLayout() != null);
        if (holder.getOnVideoPlayListener() != null) holder.getOnVideoPlayListener().onPrepare();
        initMediaPlayer(holder);
        int id = ID_GENERATOR.incrementAndGet();
        holder.mId = id;
        mRunningHolders.put(id, holder);
        if (closeOtherRunningPlayers) {
            this.closePlayersExcept(id);
        }
        return id;
    }

    public int start(PlayerHolder holder) {
        return this.start(holder, true);
    }

    public int resume(int playerId) {
        PlayerHolder holder = mRunningHolders.get(playerId);
        if (holder != null) return this.resume(holder);
        return -1;
    }

    public int resume(PlayerHolder holder) {
        assert holder != null;
        if (holder.mMediaPlayer != null) {
            holder.mMediaPlayer.start();
            return holder.mId;
        }
        return -1;
    }

    public long getCurrentPosition(PlayerHolder holder) {
        assert holder != null;
        return holder.mMediaPlayer != null ? holder.mMediaPlayer.getCurrentPosition() : 0;
    }

    public long getDuration(PlayerHolder holder) {
        assert holder != null;
        return holder.mMediaPlayer != null ? holder.mMediaPlayer.getDuration() : 0;
    }

    public void releaseAllByTag(String tagKey, Object value) {
        assert value != null;
        for (int i = 0; i < mRunningHolders.size(); i++) {
            PlayerHolder holder = mRunningHolders.valueAt(i);
            if (holder != null && value.equals(holder.getTag(tagKey))) {
                this.release(holder, false);
                mRunningHolders.remove(i);
                break;
            }
        }
    }

    public int release(int playerId) {
        PlayerHolder holder = mRunningHolders.get(playerId);
        if (holder != null) return this.release(holder);
        return -1;
    }

    public int release(PlayerHolder holder) {
        return release(holder, true);
    }

    public int release(PlayerHolder holder, boolean removed) {
        if (holder.mMediaPlayer != null) {
            holder.mMediaPlayer.release();
            if (holder.getOnVideoPlayListener() != null)
                holder.getOnVideoPlayListener().onRelease();
        }
        if (removed)
            mRunningHolders.remove(holder.mId);
        return holder.mId;
    }

    public void dettach(PlayerHolder holder) {
        if (holder.mMediaPlayer != null) {
            holder.mMediaPlayer.setDisplay(null);
        }
        if (holder.mFrameLayout != null) holder.mFrameLayout.removeAllViews();
    }

    private MediaPlayer.OnErrorListener buildOnErrorListener(final PlayerHolder holder) {
        return (mp, what, extra) -> {
            if (holder.mOnVideoPlayListener != null) holder.mOnVideoPlayListener.onError();
            return false;
        };
    }

    private MediaPlayer.OnInfoListener buildOnInfoListener(final PlayerHolder holder) {
        return (mp, what, extra) -> false;
    }

    public void pause(PlayerHolder holder) {
        if (holder.mMediaPlayer != null) {
            holder.mMediaPlayer.pause();
            holder.getOnVideoPlayListener().onPause();
        }
    }

    public interface OnVideoPlayListener {
        void onPrepare();

        void onStart(int width, int height);

        void onComplete();

        void onSeekComplete();

        void onError();

        void onRelease();

        void onPause();
    }

    public static class PlayerHolder {
        private int mId;
        private SurfaceView mSurfaceView;
        private SurfaceHolder mSurfaceHolder;
        private MediaPlayer mMediaPlayer;
        private FrameLayout mFrameLayout;
        private int mVideoWidth;
        private int mVideoHeight;
        private String mUrl;
        private boolean mIsVideoToBePlayed;
        private boolean mIsVideoSizeKnown;
        private OnVideoPlayListener mOnVideoPlayListener;
        private Map<String, Object> mTags;

        public PlayerHolder() {
        }

        public Object setTag(String tagKey, Object tagVal) {
            if (mTags == null) mTags = new HashMap<>();
            return mTags.put(tagKey, tagVal);
        }

        public Object getTag(String tagKey) {
            if (mTags == null) return null;
            return mTags.get(tagKey);
        }

        public MediaPlayer getMediaPlayer() {
            return mMediaPlayer;
        }

        public FrameLayout getFrameLayout() {
            return mFrameLayout;
        }

        public void setFrameLayout(FrameLayout frameLayout) {
            mFrameLayout = frameLayout;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            mUrl = url;
        }

        public OnVideoPlayListener getOnVideoPlayListener() {
            return mOnVideoPlayListener;
        }

        public void setOnVideoPlayListener(OnVideoPlayListener onVideoPlayListener) {
            mOnVideoPlayListener = onVideoPlayListener;
        }

        public void init() {
            mMediaPlayer = null;
            mVideoWidth = 0;
            mVideoHeight = 0;
            mUrl = null;
            mMediaPlayer = null;
            mSurfaceHolder = null;
            mFrameLayout = null;
            mIsVideoToBePlayed = false;
            mIsVideoSizeKnown = false;
        }
    }
}
