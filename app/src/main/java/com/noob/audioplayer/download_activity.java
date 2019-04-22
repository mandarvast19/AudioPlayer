package com.noob.audioplayer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Random;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
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
                getDownload();
            }
        });
        Context c = getApplicationContext();
    }

    @SuppressLint("StaticFieldLeak")
    private void getDownload() {
        new YouTubeExtractor(getApplicationContext()){
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if(ytFiles!=null){
                    int tag = 140;
                    String downloadurl = "";
                    try{
                        downloadurl = ytFiles.get(tag).getUrl();
                    }
                    catch(Exception e){
                        tag = 171;
                        try {
                            downloadurl = ytFiles.get(tag).getUrl();
                        }
                        catch(Exception ex){
                            Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!downloadurl.toString().isEmpty()){
                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        String exten = ".mp3";
                        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(downloadurl));
                        downloadRequest.allowScanningByMediaScanner();
                        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,videoMeta.getTitle() +exten);
                        downloadManager.enqueue(downloadRequest);
                        Toast.makeText(getApplicationContext(), "Audio has been downloaded.Check Downloads Folder for file", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }.extract(ytlink,true,true);
    }


    /*public void download(String url, String outputFile){
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

    }*/
}
