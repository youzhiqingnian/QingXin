package com.vlee78.android.vl;

import android.graphics.PixelFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

import com.i90s.app.frogs.video.VideoSurfaceView;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;

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
        playerHolder.mMediaPlayer = new KSYMediaPlayer.Builder(playerHolder.getFrameLayout().getContext()).build();
        playerHolder.mMediaPlayer.setBufferSize(4);
        //mMediaPlayer.setBufferTimeMax(5);
        //mMediaPlayer.setLooping(true); //循环播放

        playerHolder.mSurfaceView = new VideoSurfaceView(playerHolder.getFrameLayout().getContext());
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
                playerHolder.mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(IMediaPlayer iMediaPlayer) {
                        playerHolder.mIsVideoToBePlayed = true;
                        play();
                    }
                });

                playerHolder.mMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int i2, int i3) {
                        playerHolder.mIsVideoSizeKnown = true;
                        playerHolder.mVideoWidth = width;
                        playerHolder.mVideoHeight = height;
                        Log.e("width", String.valueOf(playerHolder.mVideoWidth));
                        Log.e("width", String.valueOf(playerHolder.mVideoHeight));
                        if (playerHolder.mSurfaceHolder != null)
                            playerHolder.mSurfaceHolder.setFixedSize(playerHolder.mVideoWidth, playerHolder.mVideoHeight);
                        play();
                    }
                });

                playerHolder.mMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                        playerHolder.mMediaPlayer.start();
                        long duration = iMediaPlayer.getDuration();
                        long currentPosition = iMediaPlayer.getCurrentPosition();
                        if (playerHolder.mOnVideoPlayListener != null)
                            playerHolder.mOnVideoPlayListener.onSeekComplete();
                        VLDebug.logD("duration,currentPosition~>>" + duration + ", " + currentPosition);
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
                playerHolder.mMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(IMediaPlayer iMediaPlayer) {
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

    public boolean dettach(PlayerHolder holder) {
        if (holder.mMediaPlayer != null) holder.mMediaPlayer.setDisplay(null);
        if (holder.mFrameLayout != null) holder.mFrameLayout.removeAllViews();
        return true;
    }

    private IMediaPlayer.OnErrorListener buildOnErrorListener(final PlayerHolder holder) {
        return new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (holder.mOnVideoPlayListener != null) holder.mOnVideoPlayListener.onError();
                VLDebug.logD("OnErrorListener ErrorCode>>" + extra);
                switch (what) {
                    case KSYMediaPlayer.MEDIA_ERROR_UNKNOWN:
                        break;
                    default:
                }
                return false;
            }
        };
    }

    private IMediaPlayer.OnInfoListener buildOnInfoListener(final PlayerHolder holder) {
        return new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                switch (i) {
                    case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        VLDebug.logD("Buffering Start." + i1);
                        break;
                    case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        VLDebug.logD("Buffering End." + i1);
                        break;
                    case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        VLDebug.logD("Audio Rendering Start.");
                        break;
                    case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        VLDebug.logD("Video Rendering Start.");
                        break;
                    case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:
                        // Player find a new stream(video or audio), and we could reload the video.
                        VLDebug.logD("MEDIA_INFO_SUGGEST_RELOAD.");
                        if (holder.mMediaPlayer != null)
                            holder.mMediaPlayer.reload(holder.mUrl, false);
                        break;
                }
                return false;
            }
        };
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
        private VideoSurfaceView mSurfaceView;
        private SurfaceHolder mSurfaceHolder;
        private KSYMediaPlayer mMediaPlayer;
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

        public KSYMediaPlayer getMediaPlayer() {
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
