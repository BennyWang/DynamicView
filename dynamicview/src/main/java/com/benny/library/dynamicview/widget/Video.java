package com.benny.library.dynamicview.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.Surface;
import android.view.TextureView;

import com.benny.library.dynamicview.HttpCacheProxyManager;
import com.benny.library.dynamicview.LifeCycleCallbacksManager;
import com.benny.library.dynamicview.LifecycleCacheManager;
import com.benny.library.dynamicview.LifecycleCallbacks;
import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@DynamicView
public class Video extends TextureView implements
        ViewType.View,
        VideoPlayerControl,
        TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnErrorListener {

    private static final String POSITION_CACHE = "POSITION_CACHE";
    private static final Executor playWorkThread = Executors.newSingleThreadExecutor();

    private int videoWidth;
    private int videoHeight;
    private LruCache<String, Integer> positionCache;

    private Handler eventDispatcher = new Handler();
    private boolean autoPlay = false;
    private boolean looping = false;
    private String src;

    private volatile MediaPlayer player;
    private volatile Surface surface;
    private boolean requestPlay = false;
    private boolean isPrepared = false;

    private int currentBufferPercentage;
    private VideoPlayerControl.OnCompleteListener onCompleteListener;
    private VideoPlayerControl.OnPauseListener onPauseListener;
    private VideoPlayerControl.OnRenderingListener onRenderingListener;

    private LifecycleCallbacks lifecycleCallbacks = new LifecycleCallbacks() {
        @Override
        public void onActivityStopped(Activity activity) {
            if (activity == getContext()) {
                if (isPlaying()) {
                    requestPlay = true;
                    pause();
                }
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity == getContext()) {
                if (requestPlay || autoPlay) {
                    start();
                    dispatchRenderingEvent();
                }
            }
        }
    };

    public Video(Context context) {
        super(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LifeCycleCallbacksManager.registerCallbacks(lifecycleCallbacks);
        }

        positionCache = LifecycleCacheManager.getInstance().makeCache(getContext(), POSITION_CACHE, Integer.class);
        setupView();
    }

    private void setupView() {
        setOpaque(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        setSurfaceTextureListener(this);
    }

    public void setAutoPlay(Boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setLooping(Boolean looping) {
        this.looping = looping;
    }

    // should set this property after loop and autoPlay
    public void setSrc(String url) {
        if (!url.equals(src)) {
            this.src = url;
            onSourceChanged();
        }
    }

    @Override
    public void setOnRenderingListener(OnRenderingListener listener) {
        onRenderingListener = listener;
    }

    @Override
    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    @Override
    public void setOnPauseListener(OnPauseListener listener) {
        onPauseListener = listener;
    }

    public void start() {
        if (player != null && !player.isPlaying()) {
            if (isPrepared) {
                player.start();
                Integer pausePosition = positionCache.get(src);
                if (player.getCurrentPosition() == 0 && pausePosition != null) {
                    player.seekTo(pausePosition);
                }
                requestPlay = false;
            } else {
                requestPlay = true;
            }
        }
    }

    public void pause() {
        if (isPlaying()) {
            player.pause();
            positionCache.put(src, player.getCurrentPosition());
            dispatchPauseEvent();
        }
    }

    public void seekTo(int position) {
        if (isPlaying()) {
            player.seekTo(position);
        }
        else {
            positionCache.put(src, position);
        }
    }

    @Override
    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (player != null) {
            return currentBufferPercentage;
        }
        return 0;
    }

    public int getDuration() {
        if (player != null) {
            return player.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    private void create() {
        release();

        if (surface == null) {
            return;
        }

        try {
            MediaPlayer player = new MediaPlayer();
            player.setOnCompletionListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnPreparedListener(this);
            player.setOnInfoListener(this);
            player.setOnErrorListener(this);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setScreenOnWhilePlaying(true);
            player.setLooping(looping);
            player.setDataSource(HttpCacheProxyManager.getProxyUrl(src));
            player.setSurface(surface);
            player.prepareAsync();

            this.player = player;
        }
        catch (IOException e) {
            Log.e("DynamicViewEngine", "Video.create exception: " + Log.getStackTraceString(e));
        }
    }

    private void release() {
        if (player != null) {
            synchronized (this) {
                if (player != null) {
                    positionCache.put(src, player.getCurrentPosition());
                    player.release();
                    dispatchCompleteEvent();
                    player = null;
                    isPrepared = false;
                }
            }
        }
    }

    private void onSourceChanged() {
        boolean isPlaying = isPlaying();
        if (isPlaying || (autoPlay && surface != null)) {
            create();
            if (autoPlay) {
                start();
            }
        }
        currentBufferPercentage = 0;
    }

    private void dispatchRenderingEvent() {
        eventDispatcher.post(new Runnable() {
            @Override
            public void run() {
                if (onRenderingListener != null) {
                    onRenderingListener.onRendering(Video.this);
                }
            }
        });
    }

    private void dispatchPauseEvent() {
        eventDispatcher.post(new Runnable() {
            @Override
            public void run() {
                if (onPauseListener != null) {
                    onPauseListener.onPause(Video.this);
                }
            }
        });
    }

    private void dispatchCompleteEvent() {
        eventDispatcher.post(new Runnable() {
            @Override
            public void run() {
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(Video.this);
                }
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = new Surface(surface);
        playWorkThread.execute(new Runnable() {
            @Override
            public void run() {
                create();
                if (requestPlay || autoPlay) {
                    start();
                }
            }
        });
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.surface = null;
        playWorkThread.execute(new Runnable() {
            @Override
            public void run() {
                release();
            }
        });
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        synchronized (this) {
            if (player == null) {
                return;
            }
            isPrepared = true;
            if (autoPlay || requestPlay) {
                start();
            }
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (videoWidth != width || videoHeight != height) {
            videoWidth = width;
            videoHeight = height;
            requestLayout();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // recreate player for trigger MEDIA_INFO_VIDEO_RENDERING_START
        create();
        // clear position and frame cache
        positionCache.remove(src);
        dispatchCompleteEvent();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.e("DynamicViewEngine", "Video.onInfo " + what);
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            dispatchRenderingEvent();
            return true;
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        currentBufferPercentage = percent;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("DynamicViewEngine", "Video.onError what: " + what);
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (videoWidth != 0 && videoHeight != 0 && width != 0 && height != 0) {
            float videoRatio = videoWidth / (float)videoHeight;
            float layoutRatio = width / height;
            if (videoRatio >= layoutRatio) {
                height = (int) (width / videoRatio);
            }
            else {
                width = (int) (height * videoRatio);
            }
            setMeasuredDimension(width, height);
        }
    }
}
