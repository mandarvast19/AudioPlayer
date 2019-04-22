package com.noob.audioplayer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DisplayPlaylists extends AppCompatActivity {
    ArrayList<Long> playlist_lists;
    ArrayList<String> playlistNames;
    ArrayList<String> audioNames;
    ArrayList<String> audioArtists;
    ArrayList<String> audioDuration;
    ArrayList<String> audioData;
    ArrayList<String> audioAlbum;
    int position;
    final static String TAG = "DisplayPlaylists";

    RecyclerView playRecyclerView;
    DisplayPlaylistdapter dpd;
    RecyclerView.LayoutManager playLayoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_playlists);
        playRecyclerView = (RecyclerView) findViewById(R.id.displayplaylist_list);
        playRecyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        playlist_lists = new ArrayList<>();
        playlistNames = new ArrayList<>();

        audioNames = new ArrayList<>();
        audioArtists = new ArrayList<>();
        audioDuration = new ArrayList<>();
        audioData = new ArrayList<>();
        audioAlbum = new ArrayList<>();
        playlist_lists = (ArrayList) intent.getStringArrayListExtra("playlistId");
        playlistNames = (ArrayList) intent.getStringArrayListExtra("playlistNames");
        position = intent.getIntExtra("pos",0);

        long playList_id = playlist_lists.get(position);
        String playlistName = playlistNames.get(position);
        setTitle(playlistName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Uri newUri = MediaStore.Audio.Playlists.Members.getContentUri("external",playList_id);
        Uri myUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver resolver = getContentResolver();
        String[] projection = {MediaStore.Audio.Playlists.Members.TITLE, MediaStore.Audio.Playlists.Members.ARTIST
                ,MediaStore.Audio.Playlists.Members.AUDIO_ID,MediaStore.Audio.Playlists.Members.DATA};
        String[] audioProjection = {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DURATION
        ,MediaStore.Audio.Media.ALBUM_ID};
        Cursor cursor = resolver.query(newUri,projection,null,null,null);
        if (cursor !=null & cursor.moveToFirst()){

            do {
                String audioName = cursor.getString(0);
                String artist = cursor.getString(1);
                String aid = cursor.getString(2);
                String data = cursor.getString(3);
                Cursor audioCursor = resolver.query(myUri,audioProjection,MediaStore.Audio.Media._ID+ "=?",new String[] {aid}
                ,null);
                if(audioCursor!=null && audioCursor.moveToFirst()) {
                    do {
                        String duration = audioCursor.getString(2);
                        String albumart = audioCursor.getString(3);
                        audioNames.add(audioName);
                        audioArtists.add(artist);
                        long millisecs = Long.valueOf(duration);
                        long hrs = TimeUnit.MILLISECONDS.toHours(millisecs);
                        long mins = TimeUnit.MILLISECONDS.toMinutes(millisecs);
                        long secs = TimeUnit.MILLISECONDS.toSeconds(millisecs);
                        String secs1 = String.valueOf(secs);
                        if(secs1.length() >=2 ) {
                            secs1 = secs1.substring(0, 2);
                            secs = Long.valueOf(secs1);

                        }
                        if (mins>1) {
                            if (hrs > 0) {
                                @SuppressLint("DefaultLocale")
                                String hms = String.format("%02d:%02d:%02d",
                                        hrs,
                                        mins - hrs,
                                        secs - mins);
                                audioDuration.add(hms);
                            } else {
                                @SuppressLint("DefaultLocale")
                                String hms = String.format("%02d:%02d",
                                        mins - hrs,
                                        secs - mins);
                                audioDuration.add(hms);
                            }
                        }
                        audioData.add(data);
                        audioAlbum.add(albumart);
                    }while(audioCursor.moveToNext());
                }
                audioCursor.close();
            }
            while (cursor.moveToNext());
        }cursor.close();

        final Context context = getApplicationContext();
        playLayoutManager = new LinearLayoutManager(context);
        playRecyclerView.setLayoutManager(playLayoutManager);

        dpd = new DisplayPlaylistdapter(context,audioNames,audioArtists,audioAlbum,audioDuration);
        playRecyclerView.setAdapter(dpd);

        dpd.setOnItemCLickListener(new DisplayPlaylistdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playAudio(audioNames,audioData,audioDuration,audioAlbum);
            }
        });
    }

    private void playAudio(ArrayList<String> audioNames, ArrayList<String> audioData,
                           ArrayList<String> audioDuration, ArrayList<String> audioAlbum) {

        startActivity(new Intent(getApplicationContext(),Main2Activity.class)
                .putExtra("pos",position)
                .putStringArrayListExtra("songname",audioNames)
                .putStringArrayListExtra("path",audioData).putStringArrayListExtra("time",audioDuration)
                .putStringArrayListExtra("cover",audioAlbum));

    }


    public Cursor getPlayList(Uri myUri){
        ContentResolver resolver = getContentResolver();
        final String id = MediaStore.Audio.Playlists._ID;
        final String name = MediaStore.Audio.Playlists.NAME;
        final String[] columns = {id,name};
        final String criteria = null;
        return resolver.query(myUri,columns,criteria,null,name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
