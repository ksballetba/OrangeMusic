package com.ksblletba.orangemusic;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_expand_less_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
