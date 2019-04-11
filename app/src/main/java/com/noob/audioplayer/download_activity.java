package com.noob.audioplayer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;


public class download_activity extends Activity {
    Button down;
    private static final int TAG_AUDIO = 140;
    private String ytlink = "https://youtube.com/watch?v=x865r5EqKDo";
    private LinearLayout mainlayout;
    private ProgressBar mainprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_activity);
        down = (Button) findViewById(R.id.download);
        Intent intent = getIntent();
        String link = intent.getData().toString();
        Toast.makeText(this, link, Toast.LENGTH_SHORT).show();

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ytEx.execute(ytlink);
            }
        });

    }

    /*YouTubeUriExtractor ytEx = new YouTubeUriExtractor(getApplication()) {
        @Override
        public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
            if(ytFiles!=null){
                int itag = 22;
                String filename ="download.mp4";
                String stoagePath = Environment.getExternalStorageDirectory().toString();
                File f = new File(filename,stoagePath);
                String downloadUrl = ytFiles.get(itag).getUrl();
                download(downloadUrl,f);
            }
        }

    };*/


    public static void download(String url, File outputFile){
        try{
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentlength = conn.getContentLength();
            DataInputStream ds = new DataInputStream(u.openStream());
            byte[] buffer = new byte[contentlength];
            ds.readFully(buffer);
            ds.close();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile));
            dos.write(buffer);
            dos.flush();
            dos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
