package com.noob.audioplayer;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class ArtistFragment extends Fragment {

    ArrayList songArtists;
    ListView artistsList;
    ArrayAdapter ad;
    TextView tname;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        Bundle args = getArguments();
        songArtists = new ArrayList<>();

        artistsList = (ListView) view.findViewById(R.id.artistslist);

        if (args != null && args.containsKey("artists")) {

        //songArtists = (ArrayList) args.getParcelableArrayList("artists");
        songArtists = (ArrayList)args.getStringArrayList("artists");
        }
        /*ArrayList list = new ArrayList();
        for (int i = 0; i <= 12; i++) {
            list.add("Sample Music" + i);
        }*/
        Context c = getActivity().getApplicationContext();
        CustomArtistsAdapter cad = new CustomArtistsAdapter(getActivity(), songArtists);
        artistsList.setAdapter(cad);

        


        return view;
    }


    }


