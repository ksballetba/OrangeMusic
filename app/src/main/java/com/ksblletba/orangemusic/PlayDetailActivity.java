package com.ksblletba.orangemusic;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.MediaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayDetailActivity extends AppCompatActivity implements PlayManager.Callback, PlayManager.ProgressCallback {


    @BindView(R.id.play_detail_image)
    ImageView playDetailImage;
    @BindView(R.id.paly_detail_musicinfo)
    RelativeLayout palyDetailMusicinfo;
    @BindView(R.id.play_detail_fav)
    Button playDetailFav;
    @BindView(R.id.paly_detail_previous)
    Button palyDetailPrevious;
    @BindView(R.id.paly_detail_next)
    Button palyDetailNext;
    @BindView(R.id.paly_detail_muscilist)
    Button palyDetailMuscilist;
    @BindView(R.id.play_detail_progressbar)
    SeekBar playDetailProgressbar;
    @BindView(R.id.paly_detail_play)
    FloatingActionButton palyDetailPlay;
    @BindView(R.id.play_detail_music_title)
    TextView playDetailMusicTitle;
    @BindView(R.id.play_detail_artist_name)
    TextView playDetailArtistName;
    @BindView(R.id.current_playtime)
    TextView currentPlaytime;
    @BindView(R.id.sum_playtime)
    TextView sumPlaytime;
    private Song currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_detail);
        ButterKnife.bind(this);

        palyDetailPlay.setOnClickListener(viewOnClickListener);
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());

        playDetailProgressbar.setOnSeekBarChangeListener(seekBarChangeListener);
        currentSong = (Song) getIntent().getSerializableExtra("current_song");
        setMusicInfo(currentSong);
    }

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.paly_detail_play:
                    PlayManager.getInstance(v.getContext()).dispatch(currentSong,"dfadf");
//                    onPlayStateChange(PlayManager.getInstance(v.getContext()).isPlaying());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        PlayManager.getInstance(this).registerCallback(this);
        PlayManager.getInstance(this).registerProgressCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayManager.getInstance(this).unregisterCallback(this);
        PlayManager.getInstance(this).unregisterProgressCallback(this);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private boolean isSeeking = false;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             currentPlaytime.setText(MediaUtils.formatTime(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeeking = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeeking = false;
            PlayManager.getInstance(seekBar.getContext()).seekTo(seekBar.getProgress());
        }
    };

    public void onPlayStateChange(boolean state) {
        if (state) {
            palyDetailPlay.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            palyDetailPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    public void setMusicInfo(Song song) {
        Uri ART = MediaUtils.getAlbumArtUri(song.getAlbumId());
        playDetailMusicTitle.setText(song.getTitle());
        playDetailArtistName.setText(song.getArtist());
        Glide.with(this).load(ART).into(playDetailImage);
    }

    @Override
    public void onPlayStateChanged(int state, Song song) {
        switch (state) {
            case PlayService.STATE_INITIALIZED:
                closeContextMenu();
                setMusicInfo(song);
                break;
            case PlayService.STATE_STARTED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_PAUSED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_COMPLETED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_STOPPED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_RELEASED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                playDetailProgressbar.setProgress(0);
                break;
            case PlayService.STATE_ERROR:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                playDetailProgressbar.setProgress(0);
                break;
        }
    }




    @Override
    public void onProgress(int progress, int duration) {
        if (isSeeking) {
            return;
        }
        if (playDetailProgressbar.getMax() != duration) {
            playDetailProgressbar.setMax(duration);
            sumPlaytime.setText(MediaUtils.formatTime(duration));
        }
        playDetailProgressbar.setProgress(progress);
        currentPlaytime.setText(MediaUtils.formatTime(progress));
    }
}
