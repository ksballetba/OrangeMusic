package com.ksblletba.orangemusic.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ksblletba.orangemusic.MainActivity;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.NetworkSong;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.ruler.Rule;
import com.ksblletba.orangemusic.manager.ruler.Rulers;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.MediaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/5/3.
 */

public class PlayManager implements PlayService.PlayStateChangeListener {

    private static PlayManager sManager = null;
    private Handler mHandler;
    private Context mContext;
    private List<Callback> mCallbacks;
    private List<ProgressCallback> mProgressCallbacks;
    private Song mSong = null;
    private NetworkSong mNetSong;
    private List<Album> mTotalAlbumList;
    private List<Song> mTotalList;
    private List<Song> mCurrentList;
    private Album mCurrentAlbum;
    private int mState = PlayService.STATE_IDLE;
    private Rule mPlayRule = Rulers.RULER_LIST_LOOP;
    private PlayService mService;
    private String playAdress;


    public int getmState() {
        return mState;
    }


    public static synchronized PlayManager getInstance(Context context){
        if (sManager==null) {
            sManager = new PlayManager(context.getApplicationContext());
        }
        return sManager;
    }


    public PlayManager(Context context){
        mContext = context;
        mCallbacks = new ArrayList<>();
        mProgressCallbacks = new ArrayList<>();
        mHandler = new Handler();
    }


    public void setmCurrentList(List<Song> mCurrentList) {
        this.mCurrentList = mCurrentList;
    }

    public void bindPlayService () {
        mContext.bindService(new Intent(mContext, PlayService.class), mConnection, Context.BIND_AUTO_CREATE);
    }
    public void startPlayService () {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N_MR1){
            mContext.startForegroundService(new Intent(mContext,PlayService.class));
        }
        mContext.startService(new Intent(mContext, PlayService.class));
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((PlayService.PlayBinder)service).getService();
            mService.setPlayStateChangeListener(PlayManager.this);
            Log.v(TAG, "onServiceConnected");
//            if (!isPlaying()&&mContext) {
//                dispatch(mSong,"dispatch");
//            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "onServiceDisconnected " + name);
            mService.setPlayStateChangeListener(null);
            mService = null;
            startPlayService();
            bindPlayService();
        }
    };

    public boolean isService(){
        return mService!=null;
    }

    public void dispatch(final Song song, String by) {
        Log.v(TAG, "dispatch BY=" + by);
        Log.v(TAG, "dispatch song=" + song);
        Log.v(TAG, "dispatch getAudioFocus mService=" + mService);
        if (mService != null) {
            mNetSong=null;
             if (song.equals(mSong)) {
                if (mService.isStarted()) {
                    //Do really this action by user
                    pause();
                } else if (mService.isPaused()){
                    resume();
                } else {
                    mService.releasePlayer();
                        mSong = song;
                        mService.startPlayer(song.getPath());

                }
            } else {
                mService.releasePlayer();
                mSong = song;
                mService.startPlayer(song.getPath());
            }

        } else {
            Log.d("data", "dispatch mService == null");
            mSong = song;
            bindPlayService();
            startPlayService();
        }

    }

    public void playNetSong(NetworkSong networkSong,String adress){
        if (mService != null) {
            if(networkSong.equals(mNetSong)){
                if (!mService.isStarted()){
                    resume();
                } else {
                    mService.releasePlayer();
                    mNetSong = networkSong;
                    mService.startPlayerNet(adress);
                }
            } else {
                mService.releasePlayer();
                mNetSong = networkSong;
                mService.startPlayerNet(adress);
            }
        } else {
            mNetSong = networkSong;
            bindPlayService();
            startPlayService();
            Log.d("data", "wtf");
        }
    }



    private int mPeriod = 1000;
    private boolean isProgressUpdating = false;
    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCallbacks != null && !mCallbacks.isEmpty()) {
                for (ProgressCallback callback : mProgressCallbacks) {
                    if (mSong!=null&&mNetSong==null) {
                        callback.onProgress(mService.getPosition(), mSong.getDuration());
                    }
                }
                mHandler.postDelayed(this, mPeriod);
                isProgressUpdating = true;
            } else {
                isProgressUpdating = false;
            }
        }
    };

    private int mPeriodNet = 1000;
    private boolean isUIUpdating = false;
    private Runnable mUIRunnable = new Runnable() {
        @Override
        public void run() {
            if (mService!=null&&mCallbacks != null && !mCallbacks.isEmpty()) {
                for (ProgressCallback callback : mProgressCallbacks) {
                    if (mNetSong!=null) {
                        callback.setMusicInfoNet(mNetSong);
                        callback.onProgress(mService.getPosition(),mNetSong.getDuration());
                    }
                }
                mHandler.postDelayed(this, mPeriodNet);
                isUIUpdating = true;
            } else {
                isUIUpdating = false;
            }
        }
    };


    public void PlayPause(){
        if(mService.isStarted()){
            pause();
        } else if(mService.isPaused()){
            resume();
        }
    }





    public Album getAlbum (int albumId) {
        for (Album album : mTotalAlbumList) {
            if (album.getId() == albumId) {
                return album;
            }
        }
        return null;
    }

    private void startUpdateProgressIfNeed() {
        if (!isProgressUpdating) {
            mHandler.post(mProgressRunnable);
        }
        if(!isUIUpdating){
            mHandler.post(mUIRunnable);
        }
    }




    public void registerProgressCallback(ProgressCallback callback) {
        if (mProgressCallbacks.contains(callback)) {
            return;
        }
        mProgressCallbacks.add(callback);
        startUpdateProgressIfNeed();
    }

    public void unregisterProgressCallback (ProgressCallback callback) {
        if (mProgressCallbacks.contains(callback)) {
            mProgressCallbacks.remove(callback);
        }
    }

    public void registerCallback (Callback callback) {
        registerCallback(callback, false);
    }

    public void registerCallback (Callback callback, boolean updateOnceNow) {
        if (mCallbacks.contains(callback)) {
            return;
        }
        mCallbacks.add(callback);
        if (updateOnceNow) {
////            callback.onPlayListPrepared(mTotalList);
////            callback.onPlayRuleChanged(mPlayRule);
            callback.onPlayStateChanged(mState, mSong);
        }
    }

    public void unregisterCallback (Callback callback) {
        if (mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    public NetworkSong getmNetSong() {
        return mNetSong;
    }

    public boolean isPlayInNet(){
        return mState>8;
    }

    public void next() {
        next(true);
    }

    /**
     * next song triggered by {@link #onStateChanged(int)} and {@link PlayService#STATE_COMPLETED}
     * @param isUserAction
     */
    private void next(boolean isUserAction) {
        dispatch(mPlayRule.next(mSong, mCurrentList, isUserAction), "next(boolean isUserAction)");
    }

    public Song getNextFirst(){
        return mPlayRule.next(mSong,mCurrentList,true);
    }

    public Song getPreviousFirst(){
        return mPlayRule.previous(mSong,mCurrentList,true);
    }

    /**
     * previous song by user action
     */
    public void previous () {
        previous(true);
    }

    private void previous (boolean isUserAction) {
        dispatch(mPlayRule.previous(mSong, mCurrentList, isUserAction), "previous (boolean isUserAction)");
    }

    @Override
    public void onStateChanged(int state) {
        mState = state;
        switch (state) {
            case PlayService.STATE_COMPLETED:
                next(false);
                break;
        }
        for (Callback callback : mCallbacks) {
            callback.onPlayStateChanged(state,mSong);
        }
    }

    @Override
    public void onShutdown() {

    }

    public List<Song> getmCurrentList() {
        return mCurrentList;
    }

    public void dispatch () {
        dispatch(mSong, "dispatch ()");
    }


    public void seekTo(int position){
        if (mService!=null) {
            mService.seekTo(position);
        }
    }

    public Song getCurrentSong() {
        return mSong;
    }


    public boolean isPlaying(){
        return mService!=null&&mService.isStarted();
    }

    public boolean isPaused(){
        return mService!=null&&mService.isPaused();
    }

    public void pause(){
        mService.pausePlayer();
    }

    public void resume(){
        mService.resumePlayer();
    }


    public interface Callback {
        void onPlayListPrepared (List<Song> songs);
        void onAlbumListPrepared (List<Album> albums);
        void onPlayStateChanged (@PlayService.State int state, Song song);
//        void onShutdown ();
        void onPlayRuleChanged (Rule rule);
    }

    public void setRule (@NonNull Rule rule) {
        mPlayRule = rule;
        for (Callback callback : mCallbacks) {
            callback.onPlayRuleChanged(mPlayRule);
        }
    }



    /**
     *
     * @return the current {@link Rule}
     */
    public Rule getRule () {
        return mPlayRule;
    }


    public interface ProgressCallback {
        void onProgress (int progress, int duration);
        void setMusicInfoNet(NetworkSong song);
    }
}
