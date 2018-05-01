package com.ksblletba.orangemusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PlayService extends Service implements MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener {

    public static final int STATE_IDLE = 0, STATE_INITIALIZED = 1, STATE_PREPARING = 2,
            STATE_PREPARED = 3, STATE_STARTED = 4, STATE_PAUSED = 5, STATE_STOPPED = 6,
            STATE_COMPLETED = 7, STATE_RELEASED = 8, STATE_ERROR = -1;

    public PlayService() {
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        setPlayerState(STATE_PREPARED);
//        doStartPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        setPlayerState(STATE_COMPLETED);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
//        setPlayerState(STATE_ERROR);
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
    }

    @IntDef({STATE_IDLE, STATE_INITIALIZED, STATE_PREPARING,
            STATE_PREPARED, STATE_STARTED, STATE_PAUSED,
            STATE_STOPPED, STATE_COMPLETED, STATE_RELEASED,
            STATE_ERROR})

    @Retention(RetentionPolicy.SOURCE)

    public @interface State {}

    private @State int mState = STATE_IDLE;

    private MediaPlayer mMediaPlayer = null;

    private PlayStateChangeListener mPlayStateChangeListener;
    private PlayBinder mBinder = null;

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder==null) {
            mBinder = new PlayBinder();
        }
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.v(TAG, "onStartCommand flags=" + flags + " startId=" + startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mPlayStateChangeListener != null) {
            mPlayStateChangeListener.onShutdown();
        }
        super.onDestroy();
    }

    public void setPlayState(@State int state){
        if(mState==state){
            return;
        }
        mState=state;
        if(mPlayStateChangeListener==null){
            mPlayStateChangeListener.onStateChanged(state);
        }
    }

    public void ensurePlayer(){
        if (mMediaPlayer==null) {
            mMediaPlayer = new MediaPlayer();
        }
        setPlayState(STATE_IDLE);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
    }

    public void startPlayer(String path){
        ensurePlayer();
        try {
            mMediaPlayer.setDataSource(path);
            setPlayState(STATE_INITIALIZED);
            mMediaPlayer.prepareAsync();
            setPlayState(STATE_PREPARED);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void doStartPlayer(){
        mMediaPlayer.start();
        setPlayState(STATE_STARTED);
    }

    public void resumePlayer(){
        if (isPlaying()) {
            doStartPlayer();
        }
    }

    public void pausePlayer(){
        if (isPlaying()) {
            mMediaPlayer.pause();
            setPlayState(STATE_PAUSED);
        }
    }

    public void stopPlayer(){
        if (isPlaying()||isPaused()) {
            mMediaPlayer.stop();
            setPlayState(STATE_STOPPED);
        }
    }

    public void realeasePlayer(){
        if (mMediaPlayer!=null) {
            mMediaPlayer.release();
            mMediaPlayer=null;
            setPlayState(STATE_RELEASED);
        }
    }

    public boolean isPlaying(){
        return mState == STATE_STARTED;
    }

    public boolean isPaused(){
        return mState==STATE_PAUSED;
    }

    public boolean isRealeased(){
        return mState==STATE_RELEASED;
    }

    public @State int getState(){
        return mState;
    }

    public int getPosition(){
        if (mMediaPlayer==null) {
            return 0;
        }
        try{
            return mMediaPlayer.getCurrentPosition();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public void seekTo(int position){
        if(isPaused()||isPlaying()){
            mMediaPlayer.seekTo(position);
        }
    }

    public class PlayBinder extends Binder{
        public PlayService getService(){
            return PlayService.this;
        }
    }

    public void setPlayStateChangeListener(PlayStateChangeListener playStateChangeListener){
        mPlayStateChangeListener = playStateChangeListener;
    }

    public interface PlayStateChangeListener{
        void onStateChanged(@State int state);
        void onShutdown();
    }
}
