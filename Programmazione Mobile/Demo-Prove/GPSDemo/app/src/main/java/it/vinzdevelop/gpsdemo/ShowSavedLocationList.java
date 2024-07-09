package it.vinzdevelop.gpsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ShowSavedLocationList extends AppCompatActivity {
    private ListView mListaSegnalini;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location_list);
        mListaSegnalini=findViewById(R.id.lv_listaSegnalini);
        MyApplication myApp = (MyApplication) getApplicationContext();
        List<Location> savedLocation= myApp.getMyLocation();

        mListaSegnalini.setAdapter(new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1,savedLocation));
    }
}