package com.ksblletba.orangemusic.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Log;

import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.Song;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class MediaUtils {


    private static final String TAG = MediaUtils.class.getSimpleName();

    public static final DecimalFormat FORMAT = new DecimalFormat("00");

    public static final String[] AUDIO_KEYS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE_KEY,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM_KEY,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_PODCAST,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA,
    };

    public static final String[] ALBUM_COLUMNS = new String[] {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_KEY,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR,
    };

    public static final String[] ARTIST_COLUMNS = new String[] {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
    };

    /*public static List<Song> getArtistList (Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                ARTIST_COLUMNS,
                null,
                null,
                null);
        return getAudioList(cursor);
    }*/

    public static List<Song> getAlbumSongList (Context context, int album_id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                AUDIO_KEYS,
                MediaStore.Audio.Media.ALBUM_ID + " = " + album_id,
                null,
                null);
        return getAudioList(cursor);
    }

    public static List<Album> getAlbumList (Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                ALBUM_COLUMNS,
                null,
                null,
                null);
        int count = cursor.getCount();
        List<Album> albumList = null;
        if (count > 0) {
            albumList = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id, minYear, maxYear, numSongs;
                String album, albumKey, artist, albumArt;
                id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                minYear = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR));
                maxYear = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
                numSongs = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                albumKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
                artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                Album albumObj = new Album(id, minYear, maxYear, numSongs,
                        album, albumKey, artist, albumArt);
                albumList.add(albumObj);
            }

        }
        cursor.close();
        return albumList;
    }

    public static List<Song> getAudioList(Context context) {


        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                AUDIO_KEYS,
                MediaStore.Audio.Media.IS_MUSIC + "=" + 1,
                null,
                null);
        return getAudioList(cursor);
    }

    private static List<Song> getAudioList (Cursor cursor) {
        List<Song> audioList = null;
        if (cursor.getCount() > 0) {
            audioList = new ArrayList<Song>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Bundle bundle = new Bundle ();
                for (int i = 0; i < AUDIO_KEYS.length; i++) {
                    final String key = AUDIO_KEYS[i];
                    final int columnIndex = cursor.getColumnIndex(key);
                    final int type = cursor.getType(columnIndex);
                    switch (type) {
                        case Cursor.FIELD_TYPE_BLOB:
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            float floatValue = cursor.getFloat(columnIndex);
                            bundle.putFloat(key, floatValue);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            int intValue = cursor.getInt(columnIndex);
                            bundle.putInt(key, intValue);
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            String strValue = cursor.getString(columnIndex);
                            bundle.putString(key, strValue);
                            break;
                    }
                }
                Song audio = new Song(bundle);
                audioList.add(audio);
            }
        }

        cursor.close();
        return audioList;
    }

    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

    public static String formatTime (int durationInMilliseconds) {
        int seconds = durationInMilliseconds /  1000;
        int minutes = seconds / 60;
        int secondsRemain = seconds % 60;
        return FORMAT.format(minutes) + ":" + FORMAT.format(secondsRemain);
    }

    public static Bitmap rsBlur(Context context,Bitmap source,int radius){

        Bitmap inputBmp = source;
        //(1)
        RenderScript renderScript =  RenderScript.create(context);

        Log.i(TAG,"scale size:"+inputBmp.getWidth()+"*"+inputBmp.getHeight());

        // Allocate memory for Renderscript to work with
        //(2)
        final Allocation input = Allocation.createFromBitmap(renderScript,inputBmp);
        final Allocation output = Allocation.createTyped(renderScript,input.getType());
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);
        //(8)
        renderScript.destroy();

        return inputBmp;
    }

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

}
