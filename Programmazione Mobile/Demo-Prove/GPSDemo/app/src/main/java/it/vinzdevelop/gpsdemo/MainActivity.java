package it.vinzdevelop.gpsdemo;

import static com.google.android.gms.location.LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "channel-id";
    private static final int notificationId = 1;
    private static final int PERMISSION_FINE_LOCATION = 99;
    private TextView mTvShowLat, mTvShowLong, mTvShowAlt,
            mTvShowAcc, mTvShowVel, mTvShowInd,
            mTvShowSensAtt, mTvShowAggiorn, mTvShowContSegn;
    private Switch mSwAggiornaPos, mSwSalvaBatt;
    private Button mBtnMostraSegna, mBtnAggiungiSegna, mBtnVediMappa;
    private MyApplication myAppContext;

    //client gps
    private FusedLocationProviderClient mFusLocProvClie;
    private LocationRequest mLocReq; //file di impostazioni per mFusLoc
    private LocationCallback mLocCallBck; //usato per ottenere subito una location
    private Location mCurrentLocation;
    private List<Location> mSavedLocation;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();

        mSwSalvaBatt.setOnClickListener(view -> {
            if (mSwSalvaBatt.isChecked()) {
                BuildLocReq(Priority.PRIORITY_HIGH_ACCURACY);
                mTvShowSensAtt.setText("GPS");
            } else {
                BuildLocReq(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                mTvShowSensAtt.setText("Wi-Fi / GSM");
            }
        });
        mSwSalvaBatt.performClick();
        UpdateLocCallBck();
        mSwAggiornaPos.setOnClickListener(view -> {
            if (mSwAggiornaPos.isChecked()) {
                startLocationUpdate();
            } else {
                stopLocationUpdate();
            }
        });

        mBtnMostraSegna.setOnClickListener(view -> {
            Intent i = new Intent(this, ShowSavedLocationList.class);
            startActivity(i);
        });
        createNotificationChannel();

        /*  // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);*/

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        mBtnAggiungiSegna.setOnClickListener(view -> {
            //get gps location
            //add new location to list
            mSavedLocation=myAppContext.getMyLocation();
            mSavedLocation.add(mCurrentLocation);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Location Aggiunta!")
                    .setContentText("La location " + mCurrentLocation + " è stata agggiunta")
/*                .setStyle(new NotificationCompat.BigTextStyle() notifica multilinea
                        .bigText("Much longer text that cannot fit one line..."))
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)*/
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify(notificationId, builder.build());
        });

        mBtnVediMappa.setOnClickListener(view -> {
            Intent i=new Intent(this,MapsActivity.class);
            startActivity(i);
        });

        updateGPS();
    }//end oncreate

    private void UpdateLocCallBck() {
        //Da richiedere sempre in quanto la last location potrebbe essere vuota
        //così inizializzi la location
        mLocCallBck = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUIValues(locationResult.getLastLocation());
            }
        };
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startLocationUpdate() {
        mTvShowAggiorn.setText("Posizione tracciata");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusLocProvClie.requestLocationUpdates(mLocReq, mLocCallBck, null);
    }
    private void stopLocationUpdate() {
        mFusLocProvClie.removeLocationUpdates(mLocCallBck);
        clearUIValue();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(this,"Localizzazione non diponibile",Toast.LENGTH_SHORT).show();
                    finish();
                }
            break;
        }
    }

    private void updateGPS(){
        //get permission
        //get current location from fused
        //update UI
        mFusLocProvClie = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        mFusLocProvClie.getLastLocation().addOnSuccessListener(this, location -> {
            //permesso ottenuto location
            if (location != null) {
            updateUIValues(location);
            mCurrentLocation=location;
            }
        });

        }
        else{ //permesso non ancora garantito richiedo al dispositivo
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },PERMISSION_FINE_LOCATION);
        }
     }
    }

    private void BuildLocReq(int priority){
        mLocReq= new LocationRequest.Builder(priority, 30000) //quanto spesso la default location accade
                .setWaitForAccurateLocation(false) //sopra, priority high massima gps,balanced torri wifi e 3g 4g
                .setMinUpdateIntervalMillis(50000) //massimo refresh della location quando settato su massimo
                .build();
    }

    private void findID(){
        myAppContext=(MyApplication) getApplicationContext();
        mTvShowLat=findViewById(R.id.tvshow_lat);
        mTvShowLong=findViewById(R.id.tvshow_long);
        mTvShowAlt=findViewById(R.id.tvshow_alt);
        mTvShowAcc=findViewById(R.id.tvshow_acc);
        mTvShowVel=findViewById(R.id.tvshow_vel);
        mTvShowInd=findViewById(R.id.tvshow_ind);
        mTvShowSensAtt=findViewById(R.id.tvshow_sensoreattivo);
        mTvShowAggiorn=findViewById(R.id.tvshow_aggiornato);
        mSwAggiornaPos=findViewById(R.id.sw_aggiornapos);
        mSwSalvaBatt=findViewById(R.id.sw_salvabatt);
        mBtnAggiungiSegna=findViewById(R.id.btn_addsegn);
        mBtnMostraSegna=findViewById(R.id.btn_mostralistasegn);
        mTvShowContSegn=findViewById(R.id.tvshow_contsegnalini);
        mBtnVediMappa=findViewById(R.id.btn_vedimappa);

    }

    private void updateUIValues(Location location) {
    mTvShowLat.setText(String.valueOf(location.getLatitude()));
    mTvShowLong.setText(String.valueOf(location.getLongitude()));
    if(location.hasAltitude()){mTvShowAlt.setText(String.valueOf(location.getAltitude()));}else{mTvShowAlt.setText("Non Disponibile");}
    mTvShowAcc.setText(String.valueOf(location.getAccuracy()));
    mTvShowVel.setText(String.valueOf(location.getSpeed()));


        try {
            Geocoder geocoder= new Geocoder(this);
            Geocoder.GeocodeListener GL = list -> mTvShowInd.setText(list.get(0).getThoroughfare());

            if (Build.VERSION.SDK_INT >= 33) {
                // declare here the geocodeListener, as it requires Android API 33
                geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1, GL);
            } else {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                mTvShowInd.setText(addresses.get(0).getAddressLine(0));
            }

        }catch (Exception e){
            mTvShowInd.setText("Impossibile prendere l'indirizzo");
        }
        mSavedLocation=myAppContext.getMyLocation();
        mTvShowContSegn.setText(Integer.toString(mSavedLocation.size()));
    }

    private void clearUIValue() {
        mTvShowAggiorn.setText("Posizione non tracciata");
        mTvShowLat.setText("Latitudine non disponibile");
        mTvShowLong.setText("Longitudine non disponibile");
        mTvShowAlt.setText("Altitudine non disponibile");
        mTvShowAcc.setText("Accuratezza non disponibile");
        mTvShowVel.setText("Velocità non disponibile");
        mTvShowInd.setText("Indirizzo non disponibile");
    }

}

