package com.benny.library.dynamicview.widget;

public interface VideoPlayerControl {
    void start();
    void pause();
    int getDuration();
    int getCurrentPosition();
    void seekTo(int pos);
    boolean isPlaying();
    int getBufferPercentage();
    void setOnRenderingListener(OnRenderingListener listener);
    void setOnCompleteListener(OnCompleteListener listener);
    void setOnPauseListener(OnPauseListener listener);

    interface OnRenderingListener {
        void onRendering(VideoPlayerControl control);
    }

    interface OnCompleteListener {
        void onComplete(VideoPlayerControl control);
    }

    interface OnPauseListener {
        void onPause(VideoPlayerControl control);
    }
}
