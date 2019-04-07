package com.noob.audioplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;



public class SongsFragment extends Fragment{
    ListView l1;
    String[] items;
    Thread songListUpdate;
    RecyclerView recyclerView;
    ArrayList<String> songsList;
    ArrayList<String> songsNameList;
    ArrayList<String> songFileList;
    ArrayList<String> songCover;
    ArrayList<String> songArtists;
    ArrayList<String> songAlbums;
    ArrayList<String> songTimeList;
    ArrayList<String> albumId;

    ArrayAdapter<String> songsAdapter;

    private SharedViewModel viewModel;
    BottomNavigationView b2;
    private MainActivity myContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_songs,container,false);
        l1=(ListView) view.findViewById(R.id.listsongs);


        b2 = (BottomNavigationView) getActivity().findViewById(R.id.botttom_nav);
        l1.setFastScrollEnabled(true);
        runtimePermission();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (MainActivity) activity;
        super.onAttach(activity);
    }

    public void runtimePermission(){
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
}


    void display() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE ;
        ContentResolver contentResolver = getActivity().getContentResolver();

        //Songs
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        //Album Art
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        songsList = new ArrayList<>();
        songsNameList = new ArrayList<>();
        songFileList = new ArrayList<>();
        songCover = new ArrayList<>();
        songArtists = new ArrayList<>();
        songAlbums = new ArrayList<>();
        songTimeList = new ArrayList<>();
        albumId = new ArrayList<>();



        if (cursor != null && cursor.moveToFirst()) {
            int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            int albumid = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);




            do {
                String title = cursor.getString(songTitle);
                String artist = cursor.getString(songArtist);
                String duration = cursor.getString(songDuration);
                String sdata = cursor.getString(songData);
                int duration1 = cursor.getInt(songDuration);
                String aid = cursor.getString(albumid);

                //int albumArt = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                Cursor albumCursor = contentResolver.query(albumUri,null,MediaStore.Audio.Albums._ID +"=?"
                        ,new String[] {aid}, null);

                albumCursor.moveToFirst();
                String albumArt = albumCursor.getString(albumCursor.getColumnIndex(BaseColumns._ID));
                songCover.add(albumArt);
                albumCursor.close();


                long hrs = (duration1 / 3600000);
                long mns = (duration1 - (hrs * 3600000)) / 60000;
                long scs = (duration1 - (hrs * 3600000) - (mns * 60000));
                String minutes = String.valueOf(mns);
                String seconds = String.valueOf(scs);
                if (seconds.length() < 2) {
                    seconds = "00";
                } else {
                    seconds = seconds.substring(0, 2);
                }
                String songTime = null;
                if (hrs > 0) {
                    songTime = hrs + ":" + minutes + ":" + seconds;
                } else {
                    songTime = minutes + ":" + seconds;
                }
                if (mns > 1) {
                    songsNameList.add(title);
                    songFileList.add(sdata);
                    songTimeList.add(songTime);
                    albumId.add(aid);
                    songsList.add(title + "\n" + artist + "\t" + songTime);
                    songArtists.add(artist);
                }
            } while (cursor.moveToNext());
            }
        cursor.close();
        //songsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, songsList);
        //l1.setAdapter(songsAdapter);
        CustomAdapter customAdapter = new CustomAdapter(getActivity(),songsNameList,songArtists,songTimeList,songCover);
        l1.setAdapter(customAdapter);

        b2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()){
                    case R.id.songs:
                        selectedFragment=new SongsFragment();
                        break;
                    case R.id.albums:
                        selectedFragment=new AlbumFragment();
                        break;
                    case R.id.artists:
                        selectedFragment=new ArtistFragment();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("artists",songArtists);
                        selectedFragment.setArguments(bundle);
                        break;
                    case R.id.playlists:
                        selectedFragment=new PlaylistsFragment();
                        break;
                }
                FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main,selectedFragment).commit();
                return true;
            }
        });





        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilePath = songFileList.get(position);


                startActivity(new Intent(getActivity(),Main2Activity.class)
                .putExtra("pos",position).putExtra("pathsingle",selectedFilePath)
                        .putStringArrayListExtra("songname",songsNameList).putStringArrayListExtra("songslist",songsList)
                        .putStringArrayListExtra("path",songFileList).putStringArrayListExtra("time",songTimeList)
                        .putStringArrayListExtra("cover",songCover)
                );
            }
        });



    }

    private Uri getAlbumUri(Context myContext, String albumArt) {
        if(myContext!=null){
            Uri artUri = Uri.parse("content;//media/external/audio/albumart");
            Uri imageUri = Uri.withAppendedPath(artUri,String.valueOf(albumArt));
            return imageUri;
        }
        return null;
    }


    /*private void displayArtists() {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("artists",songArtists);
        ArtistFragment artistFragment = new ArtistFragment();
        artistFragment.setArguments(bundle);
        //b2.setSelectedItemId(R.id.artists);
    }*/


}

