package it.vincenzopuca.mycurrency.Data;

import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIRequester {
    private static String sApiData=null;
    private static String[] sApiDataWeekly=new String[7];
    private static final String sAPIKey ="sbFjyHFBe2lQChNcEhjGVDGJxSARhsjE";

    //https://apilayer.com/marketplace/exchangerates_data-api?e=Sign+In&l=Success

    //previous 7 days
    private static String[] initCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return days;
    }

    public static CompletableFuture<List<CurrencyChangeDay>> WeeklyFetcher(String StartCountry,String EndCountry){
        CompletableFuture<List<CurrencyChangeDay>> AsyncApiWork = CompletableFuture.supplyAsync(() -> {
            //"date": "2013-12-24",
            //symbols=EUR,USD,GBP end
            //base=EUR start
            sApiDataWeekly=new String[7];
            String[] LastWeekDates=initCalendar();
            for(int i=0;i< LastWeekDates.length ;i++) {

                try {
                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    Request request = new Request.Builder()
                            .url("https://api.apilayer.com/exchangerates_data/" + LastWeekDates[i] + "?symbols=" + EndCountry + "&base=" + StartCountry)
                            .addHeader("apikey", sAPIKey)
                            .method("GET", null)
                            .build();

                    Response response = client.newCall(request).execute();
                    sApiDataWeekly[i] = response.body().string();

                } catch (Exception e) {
                    Log.e("Error Thread Weekly", "WeeklyDataAPI Fetcher Error");
                    sApiDataWeekly=null;
                }
            }

            if(sApiDataWeekly!=null){
                return MapInitWeeklyRate(sApiDataWeekly,EndCountry);
            }else{
                return null;
            }

        });//thread background
        return AsyncApiWork;
    }

    private static List<CurrencyChangeDay> MapInitWeeklyRate(String[] DataApiWeekly,String EndCountry){
        List<CurrencyChangeDay> CurrencyWeeklyList = new ArrayList<>();
            try {
                for (String s : DataApiWeekly) {
                    CurrencyChangeDay ccd = new CurrencyChangeDay("", "", "", "", "");
                    JSONObject mainObject = new JSONObject(s);

                    ccd.setmSuccess(mainObject.getString("success"));
                    ccd.setmStartCurrency(mainObject.getString("base"));
                    ccd.setmDate(mainObject.getString("date"));

                    JSONObject rates = mainObject.getJSONObject("rates");
                    ccd.setmRate(rates.getString(EndCountry));
                    ccd.setmEndCurrenct(EndCountry);

                    CurrencyWeeklyList.add(0,ccd);
                }
            } catch (Exception e) {
                Log.e("Error", "Fetching Data weekly from JSON");
                CurrencyWeeklyList=null;
            }

        return CurrencyWeeklyList;
    }

    public static CompletableFuture<ConvertedResponse> DataFetcher(String StartCurrency, String EndCurrency, String value){
        sApiData="";
        CompletableFuture<ConvertedResponse> AsyncApiWork = CompletableFuture.supplyAsync(() -> {
            //Background thread
            try {

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                            .url("https://api.apilayer.com/exchangerates_data/convert?to="+EndCurrency+"&from="+
                                    StartCurrency+"&amount="+value)
                            .addHeader("apikey", sAPIKey)
                            .method("GET", null)
                            .build();

            Response response = client.newCall(request).execute();
            sApiData=response.body().string();

            } catch (Exception e) {
                Log.e("Error", "Retrieving Data from API");
                sApiData=null;
            }//data fetched from api

            if(sApiData !=null){
                return MapInit(sApiData);
            }else{
                ConvertedResponse EmptyResponse=new ConvertedResponse("","","","",
                        "","", "","");
                EmptyResponse.setmSuccess("false");
                return EmptyResponse;
            }
        });//thread background

        return AsyncApiWork;
    }//data retriever

    private static ConvertedResponse MapInit(String RetrievedData){
        ConvertedResponse cr=new ConvertedResponse("","","","",
                "","", "","");
        try {
        JSONObject mainObject = new JSONObject(RetrievedData);

        cr.setmDate(mainObject.getString("date"));

        JSONObject info = mainObject.getJSONObject("info");
        cr.setmRate(info.getString("rate"));
        cr.setmTimeStamp(info.getString("timestamp"));

        JSONObject query = mainObject.getJSONObject("query");
        cr.setmAmount(query.getString("amount"));
        cr.setmFrom(query.getString("from"));
        cr.setmTo(query.getString("to"));

        cr.setmResult(mainObject.getString("result"));
        cr.setmSuccess(mainObject.getString("success"));

        }catch (Exception e){
            Log.e("Error", "Fetching Data from JSON");
        }
        return cr;
    }//mapping

}
