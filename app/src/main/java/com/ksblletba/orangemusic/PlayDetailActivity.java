package com.ksblletba.orangemusic;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.ksblletba.orangemusic.utils.MediaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayDetailActivity extends AppCompatActivity {

    @BindView(R.id.play_detail_toolbar)
    Toolbar playDetailToolbar;
    @BindView(R.id.play_detail_image)
    ImageView playDetailImage;
    @BindView(R.id.paly_detail_albumimage)
    CoordinatorLayout palyDetailAlbumimage;
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
    private Song currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_detail);
        ButterKnife.bind(this);
        setSupportActionBar(playDetailToolbar);
        setMusicInfo(getIntent().getIntExtra("image_art",0),getIntent().getStringExtra("music_title"),getIntent().getStringExtra("artist_name"));
        ActionBar actionBar = getSupportActionBar();
        palyDetailPlay.setOnClickListener(viewOnClickListener);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_expand_less_white_24dp);
        }
        currentSong = (Song)getIntent().getSerializableExtra("current_song");
    }

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.paly_detail_play:
                    PlayManager.getInstance(v.getContext()).dispatch(currentSong,"hehehe");
                    onPlayStateChange(PlayManager.getInstance(v.getContext()).isPlaying());
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPlayStateChange(boolean state){
        if (state) {
            palyDetailPlay.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            palyDetailPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    public void setMusicInfo(int imageArt, String musicTitle, String artistName) {
        Uri currentSongArt = MediaUtils.getAlbumArtUri(imageArt);
        playDetailMusicTitle.setText(musicTitle);
        playDetailArtistName.setText(artistName);
        Glide.with(this).load(currentSongArt).into(playDetailImage);
    }
}
