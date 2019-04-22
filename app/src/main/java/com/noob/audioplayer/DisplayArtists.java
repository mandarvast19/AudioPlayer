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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DisplayArtists extends AppCompatActivity {
    ArrayList<String> artistsName;
    ArrayList<String> artistsId;
    ArrayList<String> audioName;
    ArrayList<String> audioDuration;
    ArrayList<String> audioPath;
    ArrayList<String> albumId;
    ListView l1;
    ImageView i1;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_artists);

        artistsName = new ArrayList<>();
        artistsId = new ArrayList<>();
        audioName = new ArrayList<>();
        audioDuration = new ArrayList<>();
        audioPath = new ArrayList<>();
        albumId = new ArrayList<>();
        l1 = findViewById(R.id.artistslist);
        i1 = findViewById(R.id.artistArt);

        Intent intent = getIntent();
        artistsName = intent.getStringArrayListExtra("names");
        artistsId = intent.getStringArrayListExtra("id");
        final Context c = getApplicationContext();
        ContentResolver contentResolver = getContentResolver();
        int position = intent.getIntExtra("pos",0);
        String artistidstring = artistsId.get(position);
        String artistname = artistsName.get(position);
        setTitle(artistname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] projection ={MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST_ID
                ,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM_ID} ;
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,
                MediaStore.Audio.Media.ARTIST_ID + "=?",new String[]{artistidstring},null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                String audioname = cursor.getString(0);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String albumid = cursor.getString(4);
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
                audioName.add(audioname);
                audioPath.add(path);
                albumId.add(albumid);
            }while (cursor.moveToNext());
        }cursor.close();
        String albumfirst = albumId.get(0);
        getAlbumArt(c,albumfirst,i1);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,audioName);
        l1.setAdapter(adapter);
        l1.setDividerHeight(0);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(c,Main2Activity.class)
                .putExtra("pos",position)
                .putStringArrayListExtra("songname",audioName).putStringArrayListExtra("path",audioPath)
                .putStringArrayListExtra("time",audioDuration).putStringArrayListExtra("cover",albumId));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
    public void getAlbumArt(Context context, String albumPath, ImageView simageView){
        Uri artUri = Uri.parse("content://media/external/audio/albumart");
        Uri imageUrl = Uri.withAppendedPath(artUri,String.valueOf(albumPath));
        if(imageUrl!=null){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_music_note_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(simageView);
        }
    }
}
