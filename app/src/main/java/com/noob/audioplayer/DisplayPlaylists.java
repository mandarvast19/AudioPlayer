package com.noob.audioplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayPlaylists extends AppCompatActivity {
    ArrayList<Long> playlist_lists;
    ArrayList<String> playlistNames;
    int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_playlists);
        Intent intent = new Intent();
        playlist_lists = new ArrayList<>();
        playlistNames = new ArrayList<>();
        playlist_lists = (ArrayList) intent.getSerializableExtra("playlistId");
        playlistNames = (ArrayList) intent.getStringArrayListExtra("playlistNames");
        position = intent.getIntExtra("position",0);

    }
}
