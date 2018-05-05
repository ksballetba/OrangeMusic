package com.ksblletba.orangemusic.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.ruler.Rule;
import com.ksblletba.orangemusic.manager.ruler.Rulers;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.MediaUtils;

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
    private List<Album> mTotalAlbumList;
    private List<Song> mTotalList;
    private List<Song> mCurrentList;
    private Album mCurrentAlbum;
    private int mState = PlayService.STATE_IDLE;
    private Rule mPlayRule = Rulers.RULER_LIST_LOOP;
    private PlayService mService;



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
        mCurrentList = MediaUtils.getAudioList(context);
    }

    private void bindPlayService () {
        mContext.bindService(new Intent(mContext, PlayService.class), mConnection, Context.BIND_AUTO_CREATE);
    }
    private void startPlayService () {
        mContext.startService(new Intent(mContext, PlayService.class));
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((PlayService.PlayBinder)service).getService();
            mService.setPlayStateChangeListener(PlayManager.this);
            Log.v(TAG, "onServiceConnected");
//            startRemoteControl();
            if (!isPlaying()) {
                dispatch(mSong,"dispatch");
            }

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
        bindPlayService();
        startPlayService();
//        if (mCurrentList == null || mCurrentList.isEmpty() || song == null) {
//            return;
//        }
        //mCurrentAlbum = null;
        if (mService != null) {
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

    private int mPeriod = 1000;
    private boolean isProgressUpdating = false;
    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCallbacks != null && !mCallbacks.isEmpty()
                     && mSong != null) {
                for (ProgressCallback callback : mProgressCallbacks) {
                    callback.onProgress(mService.getPosition(), mSong.getDuration());
                }
                mHandler.postDelayed(this, mPeriod);
                isProgressUpdating = true;
            } else {
                isProgressUpdating = false;
            }
        }
    };



    public Album getAlbum (int albumId) {
        for (Album album : mTotalAlbumList) {
            if (album.getId() == albumId) {
                return album;
            }
        }
        return null;
    }

    private void startUpdateProgressIfNeed () {
        if (!isProgressUpdating) {
            mHandler.post(mProgressRunnable);
        }
    }




    public void registerProgressCallback (ProgressCallback callback) {
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
        for (Callback callback : mCallbacks) {
            callback.onPlayStateChanged(state,mSong);
        }
    }

    @Override
    public void onShutdown() {

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
//        void onPlayRuleChanged (Rule rule);
    }

    public interface ProgressCallback {
        void onProgress (int progress, int duration);
    }
}
