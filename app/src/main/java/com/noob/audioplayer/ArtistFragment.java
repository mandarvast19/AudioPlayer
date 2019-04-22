package com.noob.audioplayer;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class ArtistFragment extends Fragment {

    ArrayList songArtists;
    ListView artistsList;
    ArrayAdapter ad;
    TextView tname;
    ArrayList<String> artistsId;
    ArrayList<String> artistsName;
    ArrayList<String> artistsCount;
    ArrayList<String> albumId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        Bundle args = getArguments();
        songArtists = new ArrayList<>();
        artistsId = new ArrayList<>();
        artistsName = new ArrayList<>();
        artistsCount = new ArrayList<>();
        albumId = new ArrayList<>();

        artistsList = (ListView) view.findViewById(R.id.artistslist);

        if (args != null && args.containsKey("artists")) {

        //songArtists = (ArrayList) args.getParcelableArrayList("artists");
        songArtists = (ArrayList)args.getStringArrayList("artists");
        }
        /*ArrayList list = new ArrayList();
        for (int i = 0; i <= 12; i++) {
            list.add("Sample Music" + i);
        }*/
        final Context c = getActivity().getApplicationContext();
        /*CustomArtistsAdapter cad = new CustomArtistsAdapter(getActivity(), songArtists);
        artistsList.setAdapter(cad);*/

        ContentResolver contentResolver = getActivity().getContentResolver();
        String[] projection = {MediaStore.Audio.Artists._ID,MediaStore.Audio.Artists.ARTIST,
        };
        Cursor cursor = contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,projection,
                null,null,MediaStore.Audio.Artists.ARTIST);
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                String artistsid = cursor.getString(0);
                String artistname = cursor.getString(1);
                //String albumid = cursor.getString(2);
                if (!artistsId.contains(artistsid)){

                    artistsId.add(artistsid);
                    artistsName.add(artistname);
                    //albumId.add(albumid);
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        CustomArtistsAdapter cad = new CustomArtistsAdapter(getActivity(), artistsName);
        artistsList.setAdapter(cad);
        artistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(c,DisplayArtists.class)
                .putExtra("pos",position)
                .putStringArrayListExtra("names",artistsName).putStringArrayListExtra("id",artistsId));
            }
        });



        return view;
    }


    }


