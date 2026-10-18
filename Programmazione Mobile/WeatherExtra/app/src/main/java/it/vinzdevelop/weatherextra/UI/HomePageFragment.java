package it.vinzdevelop.weatherextra.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import it.vinzdevelop.weatherextra.Data.APIcurrentData;
import it.vinzdevelop.weatherextra.Data.WeatherCity;
import it.vinzdevelop.weatherextra.R;

public class HomePageFragment extends Fragment {

    ///// interface for passing data between fragment and activty
    private HomePageFragment.OnDataPass mVarForPassData;
    public interface OnDataPass {
        void onDataPass();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mVarForPassData = (HomePageFragment.OnDataPass) context;
    }

    public void passData() {
        mVarForPassData.onDataPass();
    }
    //data pass to activity

    Button mBtnLogout,mBtnAddCity;
    ImageButton mImgBtnLocation;
    TextInputEditText mTietCity,mTietCountry;

    ///recycle
    protected RecyclerView mRecyclerView;
    ///working vars
    private List<WeatherCity> mWeatherList= new ArrayList<WeatherCity>(); //da far diventare activity
    WeatherAdapter adapter = new WeatherAdapter(mWeatherList);
    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        Network nw = cm.getActiveNetwork();
        if (nw == null) passData();

       NetworkCapabilities actNw = cm.getNetworkCapabilities(nw);


 /*      actNw.getLinkDownstreamBandwidthKbps(); !=null is connected*/

    /*       actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View HomePageView = inflater.inflate(R.layout.homepage_layout_fragment, container, false);

        mBtnLogout=HomePageView.findViewById(R.id.btn_logout);

        mImgBtnLocation=HomePageView.findViewById(R.id.imbbtn_location);
        mTietCity=HomePageView.findViewById(R.id.tiet_city);
        mTietCountry=HomePageView.findViewById(R.id.tiet_country);

        mRecyclerView = HomePageView.findViewById(R.id.rv_homepage);
        mBtnAddCity=HomePageView.findViewById(R.id.btn_addcity);

        mBtnLogout.setOnClickListener(view -> passData());

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mBtnAddCity.setOnClickListener(view ->{
            AddDataFromAPI(mTietCity.getText().toString(),mTietCountry.getText().toString(),false);
            adapter.SetLocalWeatherCities(mWeatherList,mWeatherList.size()-1);
        });
        mImgBtnLocation.setOnClickListener(view -> {
            GPS();
            adapter.SetLocalWeatherCities(mWeatherList,mWeatherList.size()-1);
        });

        return HomePageView;
    }
    private void AddDataFromAPI(String sentCity, String sentCountry,boolean needrefresh){
        CompletableFuture future = APIcurrentData.DataFetcher(sentCity,sentCountry);

        future.thenRun( () -> {

        });

        if(!needrefresh){
            mWeatherList.add(APIcurrentData.GetWeather());
        }
    }
    private void CommitRefreshedItemWeatherList(List<WeatherCity> RefreshedList){
        mWeatherList=RefreshedList;
    }


    ////GPS section
    private static final int PERMISSION_FINE_LOCATION = 99;
    //client gps
    private FusedLocationProviderClient mFusLocProvClie;
    private LocationRequest mLocReq; //file di impostazioni per mFusLoc
    private LocationCallback mLocCallBck; //usato per ottenere subito una location

private void GPS(){
    mLocReq= new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 30000) //quanto spesso la default location accade
            .setWaitForAccurateLocation(false) //sopra, priority high massima gps,balanced torri wifi e 3g 4g
            .setMinUpdateIntervalMillis(50000) //massimo refresh della location quando settato su massimo
            .build();

    //inizializare location
    mLocCallBck = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
           locationResult.getLastLocation();//usare location ottenuta
        }
    };

    //get permission
    //get current location from fused
    //update UI
    mFusLocProvClie = LocationServices.getFusedLocationProviderClient(getContext());

    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        mFusLocProvClie.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            //permesso ottenuto location
            if (location != null) {

                try {
                    Geocoder geocoder= new Geocoder(getContext());
                   /* Geocoder.GeocodeListener GL = list -> mTvShowInd.setText(list.get(0).getThoroughfare());*/

                    Geocoder.GeocodeListener GL = list -> {
                        AddDataFromAPI(list.get(0).getLocality(),list.get(0).getCountryCode(),false);
                    };
                    geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1, GL);

                }catch (Exception e){
                   //cant translate lat lon to adress
                }
            }
        });

    }
    else{ //permesso non ancora garantito richiedo al dispositivo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },PERMISSION_FINE_LOCATION);
        }
    }

    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
    }

    mFusLocProvClie.requestLocationUpdates(mLocReq, mLocCallBck, null);
}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //richiedi if updategps
                }else{
                    Toast.makeText(getContext(),"Localizzazione non diponibile",Toast.LENGTH_SHORT).show();
                    passData();
                }
                break;
        }
    }

    //////////////
    //recycler view
    public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {
        private List<WeatherCity> LocalWeatherCities;

        public WeatherAdapter(List<WeatherCity> dataSet) {
            LocalWeatherCities = dataSet;
        }
        public void SetLocalWeatherCities(List<WeatherCity> dataset,int pos){
            LocalWeatherCities=dataset;
            notifyItemInserted(pos);
        }

        @NonNull
        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_item_recycle_homepage, viewGroup, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherHolder viewHolder,int position) {
            viewHolder.onBind(LocalWeatherCities.get(position));
        }

        @Override
        public int getItemCount() {
            return LocalWeatherCities.size();
        }

        public class WeatherHolder extends RecyclerView.ViewHolder {
            private final ImageView
                    mIV_CurrentWeather=itemView.findViewById(R.id.ic_currentweathericon),
                    mIVRainLogo=itemView.findViewById(R.id.iv_rainlogo);
            private final TextView
                    mTV_SelectedCity=itemView.findViewById(R.id.tv_selectedcity),
                    mTV_SelCityDescr=itemView.findViewById(R.id.tv_description),
                    mTV_SelCityDegrees=itemView.findViewById(R.id.tv_degrees),
                    mTVMinMaxCurrent=itemView.findViewById(R.id.tv_currentminmax),
                    mTXCurrentCloud=itemView.findViewById(R.id.tx_currentcloud),
                    mTXCurrentWind=itemView.findViewById(R.id.tx_currentwind),
                    mTxCurrentHumidity=itemView.findViewById(R.id.tx_currenthumdity),
                    mTxtLastUpdated=itemView.findViewById(R.id.txt_lastupdated),
                    mTxtRainMM=itemView.findViewById(R.id.txt_rainmm);
            private final ImageButton
                    mImgBtnUpdate=itemView.findViewById(R.id.imgbtn_update),
                    mImgBtnEdit=itemView.findViewById(R.id.imgbtn_editcity),
                    mImgBtnDelete=itemView.findViewById(R.id.imgbtn_delete),
                    mImgBtnShowonMap=itemView.findViewById(R.id.imgbtn_showonmap);
            public WeatherHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
              mImgBtnDelete.setOnClickListener(v -> {
                  LocalWeatherCities.remove(getAdapterPosition());
                  notifyItemRemoved(getAdapterPosition());
                  notifyItemRangeChanged(getAdapterPosition(), LocalWeatherCities.size());
                  CommitRefreshedItemWeatherList(LocalWeatherCities);
              });
              mImgBtnUpdate.setOnClickListener(v -> {
                  onBind(RequestRefreshedItem(LocalWeatherCities.get(getAdapterPosition()).getCity(),
                          LocalWeatherCities.get(getAdapterPosition()).getCountry()));
              });
              mImgBtnEdit.setOnClickListener(v -> {
                    ShowEditDialog();
              });
              mImgBtnShowonMap.setOnClickListener(v -> {

              });
            }

            public WeatherCity RequestRefreshedItem(String city,String country){
                AddDataFromAPI(city,country,true);
                WeatherCity refreshedItem=APIcurrentData.GetWeather();
                LocalWeatherCities.set(getAdapterPosition(),refreshedItem);
                CommitRefreshedItemWeatherList(LocalWeatherCities);
                return refreshedItem;
            }

            public void ShowEditDialog(){
                View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_city,null);

                AlertDialog.Builder alertdialog =new AlertDialog.Builder(getContext(),R.style.MyDialogTheme)
                        .setView(v)
                        .setTitle("Modifica Città")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            TextInputEditText tiet_newcity=v.findViewById(R.id.tiet_newcity),
                                    tiet_newcountry=v.findViewById((R.id.tiet_newcountry));

                            onBind(RequestRefreshedItem(tiet_newcity.getText().toString(),
                                    tiet_newcountry.getText().toString()));

                        }).setCancelable(false);
                alertdialog.show();
            }

            public void onBind(WeatherCity weatherCity){

                mIV_CurrentWeather.setImageBitmap(weatherCity.getWeatherIcon());
                if(!weatherCity.getRain().equals("-1")) {
                    mIVRainLogo.setVisibility(View.VISIBLE);
                    mTxtRainMM.setVisibility(View.VISIBLE);
                    mTxtRainMM.setText(weatherCity.getRain());
                }else{
                    mIVRainLogo.setVisibility(View.INVISIBLE);
                    mTxtRainMM.setVisibility(View.INVISIBLE);
                    mTxtRainMM.setText("");
                }
                mTV_SelectedCity.setText(weatherCity.getCity());
                mTV_SelCityDescr.setText(weatherCity.getDescription());
                mTV_SelCityDegrees.setText(weatherCity.getActualTemp());
                mTVMinMaxCurrent.setText(getString(R.string.minmaxdegree_single_recycle,weatherCity.getMinTemp(),
                        weatherCity.getMaxTemp()));
                mTXCurrentCloud.setText(getString(R.string.current_clouds,weatherCity.getClouds()));
                mTxCurrentHumidity.setText(getString(R.string.currenthumdity,weatherCity.getHumidity()));
                mTxtLastUpdated.setText(weatherCity.getRefreshDate());
                mTXCurrentWind.setText(getString(R.string.currentwind,weatherCity.getWindSpeed(),weatherCity.getWindDegree(),
                        weatherCity.getWindGuts()));

            }
        }
    }
}/////homepage fragment class