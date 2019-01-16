package com.noob.audioplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    Button playb,pauseb,previousb,nextb;
    TextView songlabel;
    SeekBar songSeek;
    String sname;

    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;





    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pauseb=(Button) findViewById(R.id.pause);
        previousb=(Button) findViewById(R.id.previous);
        nextb=(Button) findViewById(R.id.next);

        songSeek=(SeekBar) findViewById(R.id.seek);
        songlabel=(TextView) findViewById(R.id.song_text);


        /*getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        updateSeekBar=new Thread(){
            @Override
            public void run() {
                int totalDuration = myMediaPlayer.getDuration();
                int currentposition=0;
                while(currentposition<totalDuration) {
                    try {
                        Thread.sleep(500);
                        currentposition = myMediaPlayer.getCurrentPosition();
                        songSeek.setProgress(currentposition);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname=mySongs.get(position).getName().toString();

        String songname=i.getStringExtra("songname");
        songlabel.setText(songname);
        songlabel.setSelected(true);

        position= bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();
        songSeek.setMax(myMediaPlayer.getDuration());

        updateSeekBar.start();
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
                    pauseb.setBackgroundResource(R.drawable.ic_play_arrow);
                    myMediaPlayer.pause();
                }
                else{
                    pauseb.setBackgroundResource(R.drawable.ic_pause);
                    myMediaPlayer.start();
                }
            }
        });

        nextb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songlabel.setText(sname);

                myMediaPlayer.start();

            }
        });



        previousb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = (((position-1)<0) ?(mySongs.size()): (position-1));
                Uri u= Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songlabel.setText(sname);

                myMediaPlayer.start();


            }
        });



    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }*/

}
