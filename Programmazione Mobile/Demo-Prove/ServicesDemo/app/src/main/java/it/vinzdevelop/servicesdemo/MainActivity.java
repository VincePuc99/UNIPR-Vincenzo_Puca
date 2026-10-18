package it.vinzdevelop.servicesdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.tv.TableRequest;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

 TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent service= new Intent(this,BackgroundService.class);
        tx=findViewById(R.id.hellotxtx);

        Button start= findViewById(R.id.startbtn);
        Button stop = findViewById(R.id.stopbtn);

        stop.setOnClickListener(view -> {
            ServiceCommunication(service,false);
        });

        start.setOnClickListener(view -> {
            ServiceCommunication(service,true);
            tx.setText("Loading");
        });
    }

    private void ServiceCommunication(Intent i, boolean startstop){
        if(startstop){
            Context c=getApplicationContext();
            startService(i);
        }else{ stopService(i);}
    }
    public  void ServiceSendFuture(CompletableFuture<Void> fut){
       fut.thenRun(()->{
        tx.setText("Done");
       });
    }

    private BroadcastReceiver gpsBRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.getSerializableExtra()t
            //Implement UI change code here once notification is received
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(gpsBRec);
        } catch (IllegalArgumentException e) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(gpsBRec, new IntentFilter(
                RetrieveLastTrackDBService.BROADCAST_MESSAGE_NAME));
    }

}