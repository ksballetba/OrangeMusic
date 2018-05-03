package com.ksblletba.orangemusic.manager.ruler;

import com.ksblletba.orangemusic.bean.Song;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class Rulers {

    public static final Rule RULER_SINGLE_LOOP = new SingleLoopRuler(),
            RULER_LIST_LOOP = new ListLoopRuler(), RULER_RANDOM = new RandomRuler();


    public static class SingleLoopRuler implements Rule{
        @Override
        public Song previous(Song song, List<Song> songList, boolean isUserAction) {
            if (isUserAction) {
                return RULER_LIST_LOOP.previous(song,songList,isUserAction);
            }
            return song;
        }

        @Override
        public Song next(Song song, List<Song> songList, boolean isUserAction) {
            if (isUserAction) {
                return RULER_LIST_LOOP.next(song,songList,isUserAction);
            }
            return song;
        }

        @Override
        public void clear() {

        }
    }

    public static class ListLoopRuler implements Rule{
        @Override
        public Song previous(Song song, List<Song> songList, boolean isUserAction) {
            if (songList != null && !songList.isEmpty()) {
                if (song == null) {
                    return songList.get(0);
                }
                int index = songList.indexOf(song);
                if (index < 0) {
                    return songList.get(0);
                } else if (index == 0) {
                    index = songList.size();
                }
                return songList.get(index - 1);
            }
            return song;
        }

        @Override
        public Song next(Song song, List<Song> songList, boolean isUserAction) {
            if (songList != null && !songList.isEmpty()) {
                if (song == null) {
                    return songList.get(0);
                }
                int index = songList.indexOf(song);
                if (index < 0) {
                    return songList.get(0);
                }
                return songList.get((index + 1) % songList.size());
            }
            return song;
        }

        @Override
        public void clear() {

        }
    }

    public static class RandomRuler implements Rule{
        @Override
        public Song previous(Song song, List<Song> songList, boolean isUserAction) {
            return null;
        }

        @Override
        public Song next(Song song, List<Song> songList, boolean isUserAction) {
            return null;
        }

        @Override
        public void clear() {

        }
    }
}
