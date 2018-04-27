package com.ksblletba.orangemusic.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/25.
 */

public class AlbumListItem implements Serializable {
    private String musicName;
    private String musicAlbumImageId;
    private String artistName;

    public AlbumListItem(String musicName, String musicAlbumImageId, String artistName) {
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

    public String getMusicAlbumImageId() {
        return musicAlbumImageId;
    }

    public void setMusicAlbumImageId(String musicAlbumImageId) {
        this.musicAlbumImageId = musicAlbumImageId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
