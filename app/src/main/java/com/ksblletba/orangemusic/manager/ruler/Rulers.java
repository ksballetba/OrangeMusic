package com.ksblletba.orangemusic.manager.ruler;

import com.ksblletba.orangemusic.bean.Song;

import java.util.List;
import java.util.Random;
import java.util.Stack;

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

        private Random random;
        private Stack<Song> stack;

        private RandomRuler () {
            random = new Random();
            stack = new Stack<>();
        }

        @Override
        public Song previous(Song song, List<Song> songList, boolean isUserAction) {
            if(songList==null||songList.isEmpty()){
                return song;
            }
            if (!stack.empty()) {
                return stack.pop();
            }
            int index = random.nextInt(songList.size());
            return songList.get(index);

        }

        @Override
        public Song next(Song song, List<Song> songList, boolean isUserAction) {
            if(songList!=null||songList.size()>1){
                Song forwardSong;
                if(!stack.isEmpty()){
                    Song lastSong = stack.get(stack.size()-1);
                    do{
                        int index = random.nextInt(songList.size());
                        forwardSong = songList.get(index);
                    } while (forwardSong==lastSong);
                } else {
                    int index = random.nextInt(songList.size());
                    forwardSong = songList.get(index);
                }
                stack.push(forwardSong);
                return forwardSong;
            }
            return song;
        }

        @Override
        public void clear() {
            stack.clear();
        }
    }
}
