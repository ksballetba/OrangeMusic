package com.ksblletba.orangemusic.bean;

import android.net.Uri;

/**
 * Created by Administrator on 2018/4/25.
 */

public class MusicListItem {
    private String musicName;
    private Uri musicAlbumImageId;
    private String artistName;

    public MusicListItem(String musicName, Uri musicAlbumImageId, String artistName) {
        this.musicName = musicName;
        this.musicAlbumImageId = musicAlbumImageId;
        this.artistName = artistName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public Uri getMusicAlbumImageId() {
        return musicAlbumImageId;
    }

    public void setMusicAlbumImageId(Uri musicAlbumImageId) {
        this.musicAlbumImageId = musicAlbumImageId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
