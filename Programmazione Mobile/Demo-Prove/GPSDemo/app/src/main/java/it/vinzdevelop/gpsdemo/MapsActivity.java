package it.vinzdevelop.gpsdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;

import java.util.List;
import java.util.regex.Pattern;

import it.vinzdevelop.gpsdemo.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private List<Location> savedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MyApplication myApp=(MyApplication) getApplicationContext();
        savedLocation=myApp.getMyLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng lastLocation= new LatLng(3,3);
        for(Location location: savedLocation){
            LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Lat: "+location.getLatitude()+" Lon:"+location.getLongitude());
            mMap.addMarker(markerOptions);
            lastLocation=latLng;

        }
     /*   mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation));
        mMap.setMinZoomPreference(11f);//quanto più sei lontano al punto (massimo ti allontani per la città)
        mMap.setMaxZoomPreference(20f);*///quanto più sei vicino al punto (massimo ti avvicini per gli edifici)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation,12.0f));

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(lastLocation)
                .radius(300)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));

        mMap.setOnMarkerClickListener(marker -> {
        //number of time pin has been clicked
            Integer clicks= (Integer) marker.getTag();
            if(clicks==null){
                clicks=0;
            }
            clicks++;
            marker.setTag(clicks);
            Toast.makeText(this,"Marker "+marker.getTitle()+" Was Clicked "+marker.getTag()+" Times",Toast.LENGTH_SHORT).show();
            return false;
        });

/*        1: Mondo
        5: massa continentale/continente
        10: Città
        15: Strade
        20: Edifici*/
    }

}