package com.ksblletba.orangemusic.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.ruler.Rule;
import com.ksblletba.orangemusic.service.PlayService;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/5/3.
 */

public class PlayManager implements PlayService.PlayStateChangeListener {

    private static PlayManager sManager = null;
    private Context mContext;
    private Song mSong = null;
    private List<Album> mTotalAlbumList;
    private List<Song> mTotalList;
    private List<Song> mCurrentList;
    private Album mCurrentAlbum;
    private int mState = PlayService.STATE_IDLE;
    private PlayService mService;



    public static synchronized PlayManager getInstance(Context context){
        if (sManager==null) {
            sManager = new PlayManager(context.getApplicationContext());
        }
        return sManager;
    }

    public PlayManager(Context context){
        mContext = context;
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
                dispatch(mSong,"dispatch");
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

    public void dispatch(final Song song, String by) {
        Log.v(TAG, "dispatch BY=" + by);
        Log.v(TAG, "dispatch song=" + song);
        Log.v(TAG, "dispatch getAudioFocus mService=" + mService);
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
            Log.v(TAG, "dispatch mService == null");
            mSong = song;
            bindPlayService();
            startPlayService();
        }

    }

    @Override
    public void onStateChanged(int state) {

    }

    @Override
    public void onShutdown() {

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
//        void onPlayListPrepared (List<Song> songs);
//        void onAlbumListPrepared (List<Album> albums);
        void onPlayStateChanged (@PlayService.State int state, Song song);
//        void onShutdown ();
//        void onPlayRuleChanged (Rule rule);
    }

    public interface ProgressCallback {
        void onProgress (int progress, int duration);
    }
}
