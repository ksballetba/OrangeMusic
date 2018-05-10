package com.ksblletba.orangemusic;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.NetworkSong;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.fragment.AlbumListFragment;
import com.ksblletba.orangemusic.fragment.MusicListFragment;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.manager.ruler.Rule;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.HttpUtils;
import com.ksblletba.orangemusic.utils.MediaUtils;
import com.ksblletba.orangemusic.utils.NetWorkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity implements PlayManager.Callback {
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
    @BindView(R.id.music_mini_cardview)
    CardView musicMiniCardview;
    private ImageView navHeadImage;
    private TextView navHeadSongName;
    private TextView navHeadArtist;
    private int navMenuIndex = 0;
    private ActionBar actionBar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MusicListFragment musicListFragment = new MusicListFragment();
    private AlbumListFragment albumListFragment = new AlbumListFragment();
    private Song currentSong = null;
    private List<Song> songList;
    private SearchView searchView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navHeadImage = navViewTab.getHeaderView(0).findViewById(R.id.nav_headimage);
        navHeadSongName = navViewTab.getHeaderView(0).findViewById(R.id.nav_headsongname);
        navHeadArtist = navViewTab.getHeaderView(0).findViewById(R.id.nav_headartist);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivityPermissionsDispatcher.initSongInifoWithPermissionCheck(this);
        setSupportActionBar(mainToolBar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        musicMiniOptionPlay.setOnClickListener(viewClistener);
        musicMiniOptionNext.setOnClickListener(viewClistener);
        musicMiniOptionPrevious.setOnClickListener(viewClistener);
        navViewTab.setNavigationItemSelectedListener(navItemSlistener);
        musicMiniPanel.setOnClickListener(viewClistener);
    }

    @Override
    protected void onResume() {
        if (PlayManager.getInstance(this).isService()) {
            if (PlayManager.getInstance(this).getCurrentSong()!=null) {
                currentSong = PlayManager.getInstance(this).getCurrentSong();
            }
        }
        PlayManager.getInstance(this).registerCallback(this);
        MainActivityPermissionsDispatcher.setMusicInfoWithPermissionCheck(this, currentSong);
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());
        super.onResume();
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
        editor.putString("song_name", currentSong.getTitle());
        Log.d("data", "###" + currentSong.getTitle());
        editor.apply();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_btn);
        //通过MenuItem得到SearchView
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("发现好音乐");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent =  new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("search_key",query);
                startActivity(intent);
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        LinearLayout searchViewFrame = (LinearLayout) searchView.findViewById(R.id.search_edit_frame);
        ((LinearLayout.LayoutParams)searchViewFrame.getLayoutParams()).leftMargin=0;
        return super.onCreateOptionsMenu(menu);
    }


    //
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


    private void initViewPager() {
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
                    if (PlayManager.getInstance(v.getContext()).isService()){
                        PlayManager.getInstance(v.getContext()).dispatch();
                        Log.d("data", "onClick: "+ PlayManager.getInstance(v.getContext()).getmState());
                    }

                    else
                        PlayManager.getInstance(v.getContext()).dispatch(currentSong, "fsaf");
                    Log.d("data", "onClick: " + PlayManager.getInstance(v.getContext()).isPlaying());
                    onPlayStateChange(PlayManager.getInstance(v.getContext()).isPlaying());
                    break;
                case R.id.music_mini_option_next:
                    PlayManager.getInstance(v.getContext()).next();
//                    Log.d("data", "###"+);
//                    getNetWorkSongId(currentSong.getDisplayName());
//                    demoPlay();
//                      PlayManager.getInstance(v.getContext()).playNetSong("https://m7.music.126.net/20180508192751/8996a24ba7929796f561783e6d9f4df6/ymusic/fa90/df9c/59f7/95c4a2802e0b9191ae1a048f127e53c5.mp3");
                    break;
                case R.id.music_mini_option_previous:
                    PlayManager.getInstance(v.getContext()).previous();
                    break;
            }
        }
    };

    private void launchPlayActivity() {
        Intent intent = new Intent(this, PlayDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("current_song", currentSong);
        Log.d("data", "launchPlayActivity: " + currentSong);
        intent.putExtras(bundle);
        Pair ShareImage = new Pair<>(musicMiniThump, ViewCompat.getTransitionName(musicMiniThump));
        Pair ShareTextMusic = new Pair<>(mainMiniTitle, ViewCompat.getTransitionName(mainMiniTitle));
        Pair ShareTextArtist = new Pair<>(mainMiniArtistAlbum, ViewCompat.getTransitionName(mainMiniArtistAlbum));
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ShareImage, ShareTextMusic, ShareTextArtist);
        ActivityCompat.startActivity(this, intent, transitionActivityOptions.toBundle());
    }

    public void onPlayStateChange(boolean state) {
        if (state) {
            musicMiniOptionPlay.setBackgroundResource(R.drawable.ic_pause_net_24dp);
        } else {
            musicMiniOptionPlay.setBackgroundResource(R.drawable.ic_play_arrow_net_24dp);
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

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void setMusicInfo(Song song) {
        mainMiniTitle.setText(song.getTitle());
        mainMiniArtistAlbum.setText(song.getArtist());
        navHeadSongName.setText(song.getTitle());
        navHeadArtist.setText(song.getArtist());
        Uri currentSongArt = MediaUtils.getAlbumArtUri(song.getAlbumId());
        Glide.with(this).load(currentSongArt).into(musicMiniThump);
        Glide.with(this).load(currentSongArt).into(navHeadImage);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initSongInifo() {
        initViewPager();
        List<Song> songList = MediaUtils.getAudioList(this);
        PlayManager.getInstance(this).setmCurrentList(songList);
        if (currentSong == null) {
            currentSong = songList.get(0);
        }
        for (Song song : songList) {
            if (song.getTitle().equals(pref.getString("song_name", "")))
                currentSong = song;
        }
        MainActivityPermissionsDispatcher.setMusicInfoWithPermissionCheck(this, currentSong);
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
    public void onPlayRuleChanged(Rule rule) {

    }

    @Override
    public void onPlayStateChanged(int state, Song song) {
        switch (state) {
            case PlayService.STATE_INITIALIZED:
                MainActivityPermissionsDispatcher.setMusicInfoWithPermissionCheck(this, song);
                break;
            case PlayService.STATE_PREPARED_NET:
                Glide.with(this).load(R.drawable.music).into(musicMiniThump);
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
