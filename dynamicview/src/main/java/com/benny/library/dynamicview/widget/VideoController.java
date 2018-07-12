package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class VideoController extends FrameLayout implements ViewType.View,
        VideoPlayerControl.OnRenderingListener,
        VideoPlayerControl.OnPauseListener,
        VideoPlayerControl.OnCompleteListener {
    private Image vPlayControl;
    private Image vThumbnail;
    private LinearLayout vControlBar;
    private SeekBar vSeeker;
    private Image vFullScreen;
    private ProgressBar vLoading;

    private boolean isShowing = false;

    private VideoPlayerControl videoControl;
    private String playIcon = "res://play";
    private String pauseIcon = "res://pause";

    private OnClickListener playControlListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoControl.isPlaying()) {
                vPlayControl.setSrc(playIcon);
                videoControl.pause();
            }
            else {
                videoControl.start();
                vPlayControl.setSrc(pauseIcon);
                if (!isShowing) {
                    vPlayControl.setVisibility(GONE);
                }
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekChangeListener = new SeekBar.OnSeekBarChangeListener() {
        private int position = 0;
        private boolean positionChanged = false;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long duration = videoControl.getDuration();
            position = (int) ((duration * progress) / 100);
            positionChanged = true;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (positionChanged) {
                positionChanged = false;
                videoControl.seekTo(position);
            }
        }
    };

    private OnClickListener frameClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isShowing) {
                hide();
            }
            else {
                show(3000);
            }
        }
    };

    private Runnable fadeRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private Runnable setProgressRunnable = new Runnable() {
        @Override
        public void run() {
            setProgress();
        }
    };

    public VideoController(@NonNull Context context) {
        super(context);
        setupView();
    }

    private void setupView() {
        createChildren();

        setOnClickListener(frameClickListener);
    }

    private void createChildren() {
        vThumbnail = new Image(getContext());
        vThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(vThumbnail, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        vPlayControl = new Image(getContext());
        vPlayControl.setSrc(playIcon);
        vPlayControl.setOnClickListener(playControlListener);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(vPlayControl, params);

        vLoading = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        vLoading.setVisibility(GONE);
        addView(vLoading, params);

        vControlBar = new LinearLayout(getContext());
        vControlBar.setVisibility(GONE);

        vSeeker = new SeekBar(getContext());
        vSeeker.setOnSeekBarChangeListener(seekChangeListener);
        LinearLayout.LayoutParams seekerParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        seekerParams.weight = 1;
        vControlBar.addView(vSeeker, seekerParams);

        vFullScreen = new Image(getContext());
        vControlBar.addView(vFullScreen, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        addView(vControlBar, params);
    }

    private void setupControl(VideoPlayerControl control) {
        control.setOnCompleteListener(this);
        control.setOnPauseListener(this);
        control.setOnRenderingListener(this);
    }

    public void setThumbnail(String thumbnail) {
        vThumbnail.setSrc(thumbnail);
    }

    public void setPlayIcon(String playIcon) {
        this.playIcon = playIcon;
    }

    public void setPauseIcon(String pauseIcon) {
        this.pauseIcon = pauseIcon;
    }

    public void setFullScreenIcon(String fullScreenIcon) {
        vFullScreen.setSrc(fullScreenIcon);
    }

    public void setVideo(int videoId) {
        videoControl = ((ViewGroup) getParent()).findViewById(videoId);
        setupControl(videoControl);
    }

    public void show(long delay) {
        isShowing = true;

        vControlBar.setVisibility(VISIBLE);
        vPlayControl.setVisibility(VISIBLE);
        setProgress();

        if (delay != 0) {
            postDelayed(fadeRunnable, delay);
        }
    }

    public void hide() {
        isShowing = false;
        vControlBar.setVisibility(INVISIBLE);
        vPlayControl.setVisibility(INVISIBLE);
        removeCallbacks(fadeRunnable);
    }

    public void setProgress() {
        int position = videoControl.getCurrentPosition();
        int duration = videoControl.getDuration();
        if (vSeeker != null) {
            if (duration > 0) {
                vSeeker.setProgress(100 * position / duration);
            }
            int percent = videoControl.getBufferPercentage();
            vSeeker.setSecondaryProgress(percent * 10);
        }

        if (isShowing) {
            postDelayed(setProgressRunnable, 1000);
        }
    }

    @Override
    public void onRendering(VideoPlayerControl control) {
        vThumbnail.setVisibility(INVISIBLE);
        if (!isShowing) {
            vPlayControl.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onComplete(VideoPlayerControl control) {
        vThumbnail.setVisibility(VISIBLE);
        vPlayControl.setSrc(playIcon);
        vPlayControl.setVisibility(VISIBLE);
    }

    @Override
    public void onPause(VideoPlayerControl control) {
        vPlayControl.setSrc(playIcon);
        vPlayControl.setVisibility(VISIBLE);
    }
}
