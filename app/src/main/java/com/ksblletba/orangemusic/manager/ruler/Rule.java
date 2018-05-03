package com.ksblletba.orangemusic.manager.ruler;

import com.ksblletba.orangemusic.bean.Song;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public interface Rule {
    Song previous (Song song, List<Song> songList, boolean isUserAction);
    Song next(Song song, List<Song> songList, boolean isUserAction);
    void clear ();
}
