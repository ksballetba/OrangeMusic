package com.ksblletba.orangemusic;

import android.app.ActivityOptions;
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
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(mainToolBar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        mainTabLayout.addTab(mainTabLayout.newTab().setText("歌曲"));
        mainTabLayout.addTab(mainTabLayout.newTab().setText("歌手"));
        mainTabLayout.addTab(mainTabLayout.newTab().setText("专辑"));
        navViewTab.setNavigationItemSelectedListener(navItemSlistener);
        musicMiniPanel.setOnClickListener(viewClistener);


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

    private View.OnClickListener viewClistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.music_mini_panel:
                    launchPlayActivity();
                    break;
            }
        }
    };

    private void launchPlayActivity() {
        Intent intent = new Intent(this, PlayDetailActivity.class);
        Pair ShareImage = new Pair<>(musicMiniThump, ViewCompat.getTransitionName(musicMiniThump));
        Pair ShareTextMusic = new Pair<>(mainMiniTitle, ViewCompat.getTransitionName(mainMiniTitle));
        Pair ShareTextArtist = new Pair<>(mainMiniTitle, ViewCompat.getTransitionName(mainMiniTitle));
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,ShareImage,ShareTextMusic,ShareTextArtist);
        ActivityCompat.startActivity(this,
                intent, transitionActivityOptions.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}
