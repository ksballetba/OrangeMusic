package com.ksblletba.orangemusic;

import android.Manifest;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.AlbumListItem;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.fragment.AlbumListFragment;
import com.ksblletba.orangemusic.fragment.MusicListFragment;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity implements PlayManager.Callback{
    @BindView(R.id.main_tool_bar)
    Toolbar mainToolBar;
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_app_bar_layout)
    AppBarLayout mainAppBarLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mainCoordinatorLayout;
    @BindView(R.id.music_mini_thump)
    ImageView musicMiniThump;
    @BindView(R.id.main_mini_title)
    TextView mainMiniTitle;
    @BindView(R.id.main_mini_artist_album)
    TextView mainMiniArtistAlbum;
    @BindView(R.id.music_mini_info)
    LinearLayout musicMiniInfo;
    @BindView(R.id.music_mini_option_previous)
    Button musicMiniOptionPrevious;



    @BindView(R.id.music_mini_option_play)
    Button musicMiniOptionPlay;
    @BindView(R.id.music_mini_option_next)
    Button musicMiniOptionNext;
    @BindView(R.id.muxic_mini_option)
    LinearLayout muxicMiniOption;
    @BindView(R.id.music_mini_panel)
    RelativeLayout musicMiniPanel;
    @BindView(R.id.nav_view_tab)
    NavigationView navViewTab;
    @BindView(R.id.draw_layout)
    DrawerLayout drawLayout;
    private int navMenuIndex = 0;
    private ActionBar actionBar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MusicListFragment musicListFragment = new MusicListFragment();
    private AlbumListFragment albumListFragment = new AlbumListFragment();
    private Song currentSong=null;
    private List<Song> songList;
    Uri currentSongArt;
    private boolean mState;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivityPermissionsDispatcher.initSongInifoWithPermissionCheck(this);
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());
        setSupportActionBar(mainToolBar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());
        musicMiniOptionPlay.setOnClickListener(viewClistener);
        musicMiniOptionNext.setOnClickListener(viewClistener);
        musicMiniOptionPrevious.setOnClickListener(viewClistener);
        navViewTab.setNavigationItemSelectedListener(navItemSlistener);
        musicMiniPanel.setOnClickListener(viewClistener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(PlayManager.getInstance(this).isService()){
            currentSong = PlayManager.getInstance(this).getCurrentSong();
        }
        PlayManager.getInstance(this).registerCallback(this);
        setMusicInfo(currentSong);
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());
    }

    @Override
    public void onBackPressed() {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayManager.getInstance(this).unregisterCallback(this);
    }


    @Override
    protected void onDestroy() {
        editor = pref.edit();
        editor.clear();
        editor.putString("song_name",currentSong.getTitle());
        Log.d("data","###"+currentSong.getTitle());
        editor.apply();
        super.onDestroy();
    }

    private NavigationView.OnNavigationItemSelectedListener navItemSlistener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_music:
                    navMenuIndex = 0;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_music_list:
                    navMenuIndex = 1;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_folder:
                    navMenuIndex = 2;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_favorite:
                    navMenuIndex = 3;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_setting:
                    navMenuIndex = 4;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
            }
            drawLayout.closeDrawers();
            return true;
        }
    };


    private void initViewPager(){
        fragmentList.add(musicListFragment);
        fragmentList.add(albumListFragment);
        mainViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainTabLayout.getTabAt(0).setText("歌曲");
        mainTabLayout.getTabAt(1).setText("专辑");
    }

    private View.OnClickListener viewClistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.music_mini_panel:
                    launchPlayActivity();
                    break;
                case R.id.music_mini_option_play:
                    if(PlayManager.getInstance(v.getContext()).isService())
                        PlayManager.getInstance(v.getContext()).dispatch();
                    else
                        PlayManager.getInstance(v.getContext()).dispatch(currentSong,"fsaf");
                    Log.d("data", "onClick: "+PlayManager.getInstance(v.getContext()).isPlaying());
                    onPlayStateChange(PlayManager.getInstance(v.getContext()).isPlaying());
                    break;
                case R.id.music_mini_option_next:
                    PlayManager.getInstance(v.getContext()).next();

                    break;
                case R.id.music_mini_option_previous:
                    PlayManager.getInstance(v.getContext()).previous();
                    break;
            }
        }
    };

    private void launchPlayActivity() {
        Intent intent = new Intent(this, PlayDetailActivity.class);
        intent.putExtra("image_art",currentSong.getAlbumId());
        intent.putExtra("music_title",currentSong.getTitle());
        intent.putExtra("artist_name",currentSong.getArtist());
        Bundle bundle=new Bundle();
        bundle.putSerializable("current_song",currentSong);
        intent.putExtras(bundle);
        Pair ShareImage = new Pair<>(musicMiniThump, ViewCompat.getTransitionName(musicMiniThump));
        Pair ShareTextMusic = new Pair<>(mainMiniTitle, ViewCompat.getTransitionName(mainMiniTitle));
        Pair ShareTextArtist = new Pair<>(mainMiniArtistAlbum, ViewCompat.getTransitionName(mainMiniArtistAlbum));
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,ShareImage,ShareTextMusic,ShareTextArtist);
        ActivityCompat.startActivity(this, intent, transitionActivityOptions.toBundle());
    }

    public void onPlayStateChange(boolean state){
        if (state) {
            musicMiniOptionPlay.setBackgroundResource(R.drawable.ic_pause_pink_500_24dp);
        } else {
            musicMiniOptionPlay.setBackgroundResource(R.drawable.ic_play_arrow_pink_500_24dp);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMusicInfo(Song song){
        mainMiniTitle.setText(song.getTitle());
        mainMiniArtistAlbum.setText(song.getArtist());
        Uri currentSongArt = MediaUtils.getAlbumArtUri(song.getAlbumId());
        Glide.with(this).load(currentSongArt).into(musicMiniThump);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initSongInifo() {
        initViewPager();
        List<Song> songList = MediaUtils.getAudioList(this);
        if(currentSong==null){
            currentSong = songList.get(0);
        }

        for (Song song : songList) {
            if(song.getTitle().equals(pref.getString("song_name","")))
                currentSong = song;
        }
        setMusicInfo(currentSong);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onPlayListPrepared(List<Song> songs) {

    }

    @Override
    public void onAlbumListPrepared(List<Album> albums) {

    }

    @Override
    public void onPlayStateChanged(int state, Song song) {
        switch (state){
            case PlayService.STATE_INITIALIZED:
                setMusicInfo(song);
                break;
            case PlayService.STATE_STARTED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_PAUSED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_STOPPED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_COMPLETED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_RELEASED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_ERROR:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
        }
    }
}
