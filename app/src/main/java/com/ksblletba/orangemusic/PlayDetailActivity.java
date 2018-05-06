package com.ksblletba.orangemusic;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.adapter.MusicListItemAdapter;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.MusicListItem;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.manager.ruler.Rule;
import com.ksblletba.orangemusic.manager.ruler.Rulers;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayDetailActivity extends AppCompatActivity implements PlayManager.Callback, PlayManager.ProgressCallback {


    @BindView(R.id.play_detail_image)
    ImageView playDetailImage;
    @BindView(R.id.paly_detail_musicinfo)
    RelativeLayout palyDetailMusicinfo;
    @BindView(R.id.play_detail_rule)
    Button playDetailRule;
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
    @BindView(R.id.play_detail_backbutton)
    Button playDetailBackbutton;
    private Song currentSong;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_detail);
        ButterKnife.bind(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        palyDetailPlay.setOnClickListener(viewOnClickListener);
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());
        palyDetailNext.setOnClickListener(viewOnClickListener);
        palyDetailPrevious.setOnClickListener(viewOnClickListener);
        playDetailProgressbar.setOnSeekBarChangeListener(seekBarChangeListener);
        palyDetailMuscilist.setOnClickListener(viewOnClickListener);
        playDetailBackbutton.setOnClickListener(viewOnClickListener);
        playDetailRule.setOnClickListener(viewOnClickListener);
        if (PlayManager.getInstance(this).isService()) {
            currentSong = PlayManager.getInstance(this).getCurrentSong();
        } else currentSong = (Song) getIntent().getSerializableExtra("current_song");

        setMusicInfo(currentSong);
    }



    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.paly_detail_play:
                    if (PlayManager.getInstance(v.getContext()).isService()) {
                        PlayManager.getInstance(v.getContext()).dispatch();
                    } else
                        PlayManager.getInstance(v.getContext()).dispatch(currentSong, "fasd");
                    break;
                case R.id.paly_detail_next:
                    PlayManager.getInstance(v.getContext()).next();
                    break;
                case R.id.paly_detail_previous:
                    PlayManager.getInstance(v.getContext()).previous();
                    break;
                case R.id.paly_detail_muscilist:
                    showQuickList();
                    break;
                case R.id.play_detail_backbutton:
                    onBackPressed();
                    break;
                case R.id.play_detail_rule:
                    PlayManager pm = PlayManager.getInstance(v.getContext());
                    Rule rule = pm.getRule();
                    if (rule == Rulers.RULER_LIST_LOOP) {
                        pm.setRule(Rulers.RULER_SINGLE_LOOP);
                    } else if (rule == Rulers.RULER_SINGLE_LOOP) {
                        pm.setRule(Rulers.RULER_RANDOM);
                    } else if(rule == Rulers.RULER_RANDOM){
                        pm.setRule(Rulers.RULER_LIST_LOOP);
                    }
            }
        }
    };

    private void setRule(String nowRuleState){
        PlayManager pm = PlayManager.getInstance(this);
       if(nowRuleState.equals("list_loop")){
           pm.setRule(Rulers.RULER_LIST_LOOP);
           playDetailRule.setBackgroundResource(R.drawable.ic_repeat_black_24dp);
       } else if(nowRuleState.equals("single_loop")){
           pm.setRule(Rulers.RULER_SINGLE_LOOP);
           playDetailRule.setBackgroundResource(R.drawable.ic_repeat_one_black_24dp);
       } else if(nowRuleState.equals("random")){
           pm.setRule(Rulers.RULER_RANDOM);
           playDetailRule.setBackgroundResource(R.drawable.ic_shuffle_black_24dp);
       }
    }


    @Override
    protected void onResume() {
        String nowRuleState = pref.getString("rule_state","list_loop");
        Log.d("data", "++"+nowRuleState);
        setRule(nowRuleState);
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

    private void showQuickList () {
        final List<Song> songs = PlayManager.getInstance(this).getmCurrentList();
        if (songs != null && !songs.isEmpty()) {
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            RecyclerView rv = new RecyclerView(this);
            rv.setLayoutManager(new LinearLayoutManager(this));
            List<MusicListItem> musicListItems = new ArrayList<>();
            for (Song song : songs) {
                Uri art = MediaUtils.getAlbumArtUri(song.getAlbumId());
                musicListItems.add(new MusicListItem(song.getTitle(),art,song.getArtist()));
            }
            MusicListItemAdapter adapter = new MusicListItemAdapter(musicListItems);
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new MusicListItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Song song = songs.get(position);
                    PlayManager.getInstance(view.getContext()).dispatch(song,"dd");
                    dialog.dismiss();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            dialog.setContentView(rv);
            dialog.show();
        }
    }

    public void setMusicInfo(Song song) {
        Uri ART = MediaUtils.getAlbumArtUri(song.getAlbumId());
        playDetailMusicTitle.setText(song.getTitle());
        playDetailArtistName.setText(song.getArtist());
        Glide.with(this).load(ART).into(playDetailImage);
//        playDetailImage.setImageResource(R.drawable.music2);
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
    public void onPlayListPrepared(List<Song> songs) {

    }

    @Override
    public void onAlbumListPrepared(List<Album> albums) {

    }

    @Override
    public void onPlayRuleChanged(Rule rule) {
        editor = pref.edit();
        String ruleState = "list_loop";
        if(rule == Rulers.RULER_LIST_LOOP){
            playDetailRule.setBackgroundResource(R.drawable.ic_repeat_black_24dp);
            ruleState = "list_loop";

        } else if(rule == Rulers.RULER_SINGLE_LOOP){
            playDetailRule.setBackgroundResource(R.drawable.ic_repeat_one_black_24dp);
            ruleState = "single_loop";

        } else if(rule == Rulers.RULER_RANDOM){
            playDetailRule.setBackgroundResource(R.drawable.ic_shuffle_black_24dp);
            ruleState = "random";
        }

        editor.putString("rule_state",ruleState);
        editor.apply();
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
