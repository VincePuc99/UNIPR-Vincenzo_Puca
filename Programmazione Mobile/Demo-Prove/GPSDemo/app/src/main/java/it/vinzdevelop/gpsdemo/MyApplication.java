package it.vinzdevelop.gpsdemo;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication Singleton;
    private List<Location> mMyLocation;

    public MyApplication getInstance(){
        return Singleton;
    }
    public void onCreate(){
        super.onCreate();
        Singleton=this;
        mMyLocation= new ArrayList<>();
    }

    public List<Location> getMyLocation() {
        return mMyLocation;
    }

    public void setMyLocation(List<Location> myLocation) {
        mMyLocation = myLocation;
    }
}
