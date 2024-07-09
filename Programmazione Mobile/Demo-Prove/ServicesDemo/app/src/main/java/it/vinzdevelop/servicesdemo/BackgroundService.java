package it.vinzdevelop.servicesdemo;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class BackgroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        CompletableFuture<Void> future= CompletableFuture.runAsync(()-> {
            JSONObject sApiData = null;
            try {
                String city = "Roma", country = "IT";

                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" +
                        city + "," + country + "&lang=it&appid=39215f00b1739eefc69b0812eca7696b&units=metric");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";

                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                sApiData = new JSONObject(json.toString());

                if (sApiData.getInt("cod") != 200) {
                    Log.e("Error", "Data Cancelled");
                }
            } catch (Exception e) {
                Log.e("Error", "Retrieving Data from API");
            }//data fetched from api

            if (sApiData != null) {
                ////create weather object with maps
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).thenRun(()->{
            Bundle extras;
            extras.putSerializable("www",sApiData);
            sendBroadcastNotification(extras);
        });

        return super.onStartCommand(intent, flags, startId);
    }

    public void sendBroadcastNotification(Bundle extras) {
        Intent intentBroadcast = new Intent(BROADCAST_MESSAGE_NAME);
        intentBroadcast.putExtra(CoreConstants.EXTRA_INTENT_MSG_ID,
                mIntentMsgId);
        sendBroadcast(intentBroadcast);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
