package com.ksblletba.orangemusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PlayService extends Service implements MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener{

    private static final String TAG = PlayService.class.getSimpleName();

    public static final int STATE_IDLE = 0, STATE_INITIALIZED = 1, STATE_PREPARING = 2,
            STATE_PREPARED = 3, STATE_STARTED = 4, STATE_PAUSED = 5, STATE_STOPPED = 6,
            STATE_COMPLETED = 7, STATE_RELEASED = 8, STATE_ERROR = -1,STATE_INITIALIZED_NET=9,
            STATE_PREPARING_NET = 10,STATE_PREPARED_NET = 11,STATE_STARTED_NET = 12,STATE_PAUSED_NET = 13;

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        switch (mState) {
            case STATE_PREPARING:
                setPlayerState(STATE_PREPARED);
                doStartPlayer();
                break;
            case STATE_PREPARING_NET:
                setPlayerState(STATE_PREPARED_NET);
                doStartPlayer();
                break;
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setPlayerState(STATE_COMPLETED);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        setPlayerState(STATE_ERROR);
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
    }

    @IntDef({STATE_IDLE, STATE_INITIALIZED, STATE_PREPARING,
            STATE_PREPARED, STATE_STARTED, STATE_PAUSED,
            STATE_STOPPED, STATE_COMPLETED, STATE_RELEASED,
            STATE_ERROR,STATE_INITIALIZED_NET,
            STATE_PREPARING_NET,STATE_PREPARED_NET,
            STATE_STARTED_NET,STATE_PAUSED_NET})

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    private @State int mState = STATE_IDLE;

    private MediaPlayer mPlayer = null;
    //private Song mCurrentSong;

    private PlayBinder mBinder = null;

    private PlayStateChangeListener mStateListener;

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new PlayBinder();
        }
        Log.v(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand flags=" + flags + " startId=" + startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mStateListener != null) {
            mStateListener.onShutdown();
        }
        super.onDestroy();
        stopForeground(true);
        NotificationManagerCompat.from(this).cancelAll();
        Log.v(TAG, "onDestroy");
    }

    private void setPlayerState (@State int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        if (mStateListener != null) {
            mStateListener.onStateChanged(mState);
        }
    }

    private void ensurePlayer () {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        setPlayerState(STATE_IDLE);
        mPlayer.setOnInfoListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnSeekCompleteListener(this);
    }

    public void startPlayer (String path) {
        //releasePlayer();
        ensurePlayer();
        try {
            mPlayer.setDataSource(path);
            setPlayerState(STATE_INITIALIZED);
            mPlayer.prepareAsync();
            setPlayerState(STATE_PREPARING);
        } catch (IOException e) {
            e.printStackTrace();
            releasePlayer();
        }
    }

    public void startPlayerNet(String url) {
        ensurePlayer();
        try {
            Log.d("data", "startPlayerNet: "+url);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            setPlayerState(STATE_INITIALIZED_NET);
            mPlayer.prepareAsync();
            setPlayerState(STATE_PREPARING_NET);
        }catch (IOException e){
            releasePlayer();
            Log.d("data", "playNetSong: bad");
        }
    }

    public void doStartPlayer () {
        mPlayer.start();
        switch (mState) {
            case STATE_PREPARED_NET:
            case STATE_PAUSED_NET:
                setPlayerState(STATE_STARTED_NET);
                break;
            case STATE_PREPARED:
            case STATE_PAUSED:
                setPlayerState(STATE_STARTED);
                break;
        }

    }

    public void resumePlayer () {
        if (isPaused()) {
            doStartPlayer();
        }
    }

    public void pausePlayer () {
        if (isStarted()) {
            mPlayer.pause();
            switch (mState){
                case STATE_STARTED:
                    setPlayerState(STATE_PAUSED);
                    break;
                case STATE_STARTED_NET:
                    setPlayerState(STATE_PAUSED_NET);
            }

        }
    }

    public void stopPlayer () {
        if (isStarted() || isPaused()) {
            mPlayer.stop();
            setPlayerState(STATE_STOPPED);
        }
    }

    public void releasePlayer () {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            setPlayerState(STATE_RELEASED);
        }
    }

    public boolean isStarted () {
        return mState == STATE_STARTED||mState==STATE_STARTED_NET;
    }

    public boolean isPaused () {
        return mState == STATE_PAUSED||mState==STATE_PAUSED_NET;
    }

    public boolean isReleased () {
        return mState == STATE_RELEASED;
    }

    public @State int getState () {
        return mState;
    }

    public int getPosition () {
        if (mPlayer == null) {
            return 0;
        }

        try {
            return mPlayer.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void seekTo (int position) {
        if (isStarted() || isPaused()) {
            mPlayer.seekTo(position);
        }
    }

    public void setPlayStateChangeListener (PlayStateChangeListener listener) {
        mStateListener = listener;
    }

    public interface PlayStateChangeListener {
        void onStateChanged (@State int state);
        void onShutdown ();
    }

    public class PlayBinder extends Binder {
        public PlayService getService () {
            return PlayService.this;
        }
    }
}

