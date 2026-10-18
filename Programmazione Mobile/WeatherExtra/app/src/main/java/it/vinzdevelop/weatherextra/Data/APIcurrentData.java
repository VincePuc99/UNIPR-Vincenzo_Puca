package it.vinzdevelop.weatherextra.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class APIcurrentData {
    private static WeatherCity sWeatherCityOBJ=new WeatherCity();
    private static boolean sInitWeaher =false;
    private static JSONObject sApiData = null;

    public static WeatherCity GetWeather(){
        if(sInitWeaher){
           return sWeatherCityOBJ;
        }
        WeatherCity Sample=new WeatherCity();
        Sample.setCity("");
        Sample.setCountry("");
        Sample.setRefreshDate("");
        Sample.setActualTemp("");
        Sample.setMinTemp("");
        Sample.setMaxTemp("");
        Sample.setMinTemp("");
        Sample.setClouds("");
        Sample.setRain("");
        Sample.setWindDegree("");
        Sample.setWindSpeed("");
        Sample.setWindGuts("");
        Sample.setDescription("");
        Sample.setHumidity("");
        int w =50, h =50;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);
        Sample.setWeatherIcon(bmp);
        return Sample;
    }

    public static CompletableFuture DataFetcher(String city,String country){

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            //Background thread
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+
                        city +","+ country +"&lang=it&appid=APIKEY&units=metric");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";

                while((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                sApiData = new JSONObject(json.toString());

                if(sApiData.getInt("cod") != 200) {
                    Log.e("Error", "Data Cancelled");
                }
            } catch (Exception e) {
                Log.e("Error", "Retrieving Data from API");
            }//data fetched from api

            if(sApiData !=null){
                ////create weather object with maps
                MapWeatherInizializer();
            }
        });//thread background

    return future;

    }//data retriever

    private static void MapWeatherInizializer(){

        Map<String, Object> MainMap,MainMap_weather_List,MainMap_coord,MainMap_main,MainMap_wind,
                 MainMap_rain = null ,MainMap_clouds,MainMap_sys;

        List<Map<String, Object>> MainMap_weather;
        boolean noRain;

        MainMap = jsonToMap(sApiData.toString());

        MainMap_coord = jsonToMap(MainMap.get("coord").toString());

        MainMap_weather = (List<Map<String, Object>>) (MainMap.get("weather"));
        //only response with [] so need two maps
        MainMap_weather_List = MainMap_weather.get(0); //list to use for display //0 means only one list

        MainMap_main = jsonToMap((MainMap.get("main")).toString());

        MainMap_wind = jsonToMap((MainMap.get("wind")).toString());

        try {
            MainMap_rain = jsonToMap((MainMap.get("rain")).toString());
            noRain=false;
        }catch (Exception e){
            noRain=true;
        }

        MainMap_clouds = jsonToMap((MainMap.get("clouds")).toString());

        MainMap_sys = jsonToMap((MainMap.get("sys")).toString());
        //data stored in objects maps

            /*  MainMap.get("name") get data json no { }
                MainMap_weather_List.get("main") get data json {[]}
                MainMap_wind.get("speed") get data (son { } */

        //init weathercity obj
        sWeatherCityOBJ.setCity(MainMap.get("name").toString());
        sWeatherCityOBJ.setCountry(MainMap_sys.get("country").toString());
        sWeatherCityOBJ.setActualTemp(MainMap_main.get("temp").toString());
        sWeatherCityOBJ.setMinTemp(MainMap_main.get("temp_min").toString());
        sWeatherCityOBJ.setMaxTemp(MainMap_main.get("temp_max").toString());
        sWeatherCityOBJ.setClouds(MainMap_clouds.get("all").toString());
        sWeatherCityOBJ.setHumidity(MainMap_main.get("humidity").toString());

        if(noRain){
            sWeatherCityOBJ.setRain("-1");
        }else{
            sWeatherCityOBJ.setRain(MainMap_rain.get("1h").toString());
        }

        Bitmap iconBtmp=null;
        //get provided icon
        String iconURL = "https://openweathermap.org/img/w/" + MainMap_weather_List.get("icon") + ".png";
        try {
            InputStream in = new java.net.URL(iconURL).openStream();
            iconBtmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", "Retrieving Icon from API");
        }
        sWeatherCityOBJ.setWeatherIcon(iconBtmp);

        sWeatherCityOBJ.setWindDegree(MainMap_wind.get("deg").toString());
        sWeatherCityOBJ.setWindSpeed(MainMap_wind.get("speed").toString());
        try {
            sWeatherCityOBJ.setWindGuts(MainMap_wind.get("gust").toString());
        }
        catch (Exception e){
            sWeatherCityOBJ.setWindGuts("0");
        }

        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm", Locale.ITALIAN);
        String formatter = dateTimeFormatter.format(ldt);

        sWeatherCityOBJ.setRefreshDate(formatter);

        sWeatherCityOBJ.setDescription(MainMap_weather_List.get("description").toString());

        sWeatherCityOBJ.setLat(MainMap_coord.get("lat").toString());
        sWeatherCityOBJ.setLong(MainMap_coord.get("lon").toString());

        sInitWeaher =true;
    }
    ///////needed for maps converter
    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new
                TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }
}
