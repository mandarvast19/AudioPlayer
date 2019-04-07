package com.noob.audioplayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<ArrayList> artistslist = new MutableLiveData<>();

    public void setList(ArrayList artistsList){
        artistslist.setValue(artistsList);
    }

    public LiveData<ArrayList> getList(){
        return artistslist;
    }
}
