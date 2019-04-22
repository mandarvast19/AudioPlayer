package com.noob.audioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {
    Button previousb,nextb;
    ImageButton pauseb,playb;
    ImageView albumCover;
    TextView songlabel;
    SeekBar songSeek;
    String sname;
    TextView timelabel;
    TextView timelabel2;

    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<String> mySongs;
    ArrayList<String> songPath;
    ArrayList<String> albumArt;
    ArrayList<String> songTimeList;
    Thread updateSeekBar;

    @SuppressLint({"NewApi", "ResourceAsColor"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pauseb=(ImageButton) findViewById(R.id.pause);
        previousb=(Button) findViewById(R.id.previous);
        nextb=(Button) findViewById(R.id.next);
        albumCover=(ImageView)findViewById(R.id.album_art);
        songSeek=(SeekBar) findViewById(R.id.seek);
        songlabel=(TextView) findViewById(R.id.song_text);
        timelabel = (TextView) findViewById(R.id.timelabel);
        timelabel2 = (TextView) findViewById(R.id.timelabel2);

        setTitle("Now Playing");
        /*getSupportActionBar().setTitle("Now Playing");*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        Bundle bundle =  i.getExtras();
        position= bundle.getInt("pos",0);
        //final String filePath = i.getStringExtra("pathsingle");

        mySongs = new ArrayList<>();
        songTimeList = new ArrayList<>();
        songPath = (ArrayList) bundle.getParcelableArrayList("path");
        albumArt = (ArrayList) bundle.getParcelableArrayList("cover");
        mySongs = (ArrayList) bundle.getParcelableArrayList("songname");
        songTimeList = (ArrayList) bundle.getParcelableArrayList("time");

        String filePath = songPath.get(position).toString();

        final Context myContext = getApplicationContext();
        String albumId = albumArt.get(position);
        getAlbumArt(myContext,albumId);


        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            //myMediaPlayer = null;
            myMediaPlayer.release();
        }

        myMediaPlayer = new MediaPlayer();
        try {
            myMediaPlayer.setDataSource(filePath);
            myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            myMediaPlayer.prepare();
            myMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        sname = mySongs.get(position).toString();
        songlabel.setText(sname);
        songlabel.setSelected(true);
        songSeek.setMax(myMediaPlayer.getDuration());
        startSeekAsync();
        //updateSeekBar.start();

        songSeek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        songSeek.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);




        songSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pauseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songSeek.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying()){
                    //pauseb.setForeground(getResources().getDrawable(R.drawable.ic_play_arrow));
                    pauseb.setImageResource(R.drawable.ic_play_arrow);
                    myMediaPlayer.pause();
                }
                else{
                    //pauseb.setForeground(getResources().getDrawable(R.drawable.ic_pause));
                    pauseb.setImageResource(R.drawable.ic_pause);
                    myMediaPlayer.start();
                }
            }
        });

        nextb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.reset();
                //myMediaPlayer.release();
                position = ((position+1)%mySongs.size());
                String newPath = songPath.get(position);
                String albumPath = albumArt.get(position);
                getAlbumArt(myContext,albumPath);
                myMediaPlayer = new MediaPlayer();
                try {
                    myMediaPlayer.setDataSource(newPath);
                    myMediaPlayer.prepare();
                    myMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startSeekAsync();
                sname = mySongs.get(position).toString();
                songlabel.setText(sname);
                /*String coverpathnext = albumArt.get(position).toString();
                Bitmap bitmapnext = BitmapFactory.decodeFile(coverpathnext);
                albumCover.setImageBitmap(bitmapnext);*/

            }
        });



        previousb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = (((position-1)<0) ?(mySongs.size()): (position-1));
                String newPath = songPath.get(position);
                String albumPath = albumArt.get(position);
                getAlbumArt(myContext,albumPath);
                myMediaPlayer = new MediaPlayer();
                try {
                    myMediaPlayer.setDataSource(newPath);
                    myMediaPlayer.prepare();
                    myMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startSeekAsync();
                sname = mySongs.get(position).toString();
                songlabel.setText(sname);
            }
        });



    }


    public void startSeekAsync(){
        SeekRunnable seekRunnable = new SeekRunnable();
        new Thread(seekRunnable).start();
    }

    public void getAlbumArt(Context context,String albumPath){
        Uri artUri = Uri.parse("content://media/external/audio/albumart");
        Uri imageUrl = Uri.withAppendedPath(artUri,String.valueOf(albumPath));
        if(imageUrl!=null){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_music_note_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(albumCover);
        }
    }


    /*public class SeekBarASync extends AsyncTask<Integer, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(Integer... integers) {
            int totalDuration = myMediaPlayer.getDuration();
            String duration = songTimeList.get(position).toString();
            timelabel2.setText(duration);

            int currentposition = 0;
            while(currentposition<totalDuration){
                try {
                    Thread.sleep(1000);
                    currentposition = myMediaPlayer.getCurrentPosition();
                    songSeek.setProgress(currentposition);
                    String songPosition = String.valueOf(currentposition);
                    /*int hrs = (int) (currentposition / 3600000);
                    int mns = (int)(currentposition - (hrs * 3600000)) / 60000;
                    int scs = (int)(currentposition - (hrs * 3600000) - (mns * 60000));
                    String minutes = String.valueOf(mns);
                    String seconds = String.valueOf(scs);
                    if (seconds.length() < 2) {
                        seconds = "00";
                    } else {
                        seconds = seconds.substring(0, 2);
                    }
                    String songPosition = null;
                    if (hrs > 0) {
                        songPosition = hrs + ":" + minutes + ":" + seconds;
                    } else {
                        songPosition= minutes + ":" + seconds;
                    }

                    publishProgress(songPosition);
                    //cancel(true);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
                cancel(true);
            }
            //cancel(true);
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            timelabel.setText(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //timelabel.setText(s);

        }

    }*/

    public class SeekRunnable implements Runnable{
        String songPosition;
        String hms;

        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            int totalDuration = myMediaPlayer.getDuration();
            String duration = songTimeList.get(position).toString();
            //String songPosition = null;
            timelabel2.setText(duration);
            int currentposition = 0;

            while(currentposition<totalDuration){
                try {
                    Thread.sleep(1000);
                    currentposition = myMediaPlayer.getCurrentPosition();
                    songSeek.setProgress(currentposition);
                    long millisecs = Long.valueOf(currentposition);
                    long hrs = TimeUnit.MILLISECONDS.toHours(millisecs);
                    long mins = TimeUnit.MILLISECONDS.toMinutes(millisecs);
                    long secs = TimeUnit.MILLISECONDS.toSeconds(millisecs);
                    if (hrs>0) {
                         hms = String.format("%02d:%02d:%02d",
                                hrs,
                                mins-hrs,
                                secs-mins);
                    }
                    else{

                         hms = String.format("%02d:%02d",
                                mins-hrs,
                                secs-mins);
                    }
                    timelabel.post(new Runnable() {
                        @Override
                        public void run() {
                            timelabel.setText(hms);
                        }
                    });
                    //cancel(true);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            }
        }
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

}
