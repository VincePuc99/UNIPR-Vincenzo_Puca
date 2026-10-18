package it.vincenzopuca.mycurrency.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import it.vincenzopuca.mycurrency.Data.APIRequester;
import it.vincenzopuca.mycurrency.Data.ConvertedResponse;
import it.vincenzopuca.mycurrency.Data.Currencies;
import it.vincenzopuca.mycurrency.Data.CurrencyChangeDay;
import it.vincenzopuca.mycurrency.R;

public class HomePageFragment extends Fragment {
    ///// interface for passing data between fragment and activity
    private OnDataPass mVarForPassData;
    public interface OnDataPass {
        void onDataPass(boolean ReadOrWrite,String inSpin,String outSpin,int DeleteAll);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mVarForPassData = (HomePageFragment.OnDataPass) context;
    }

    public void passData(boolean ReadOrWrite,String inSpin,String outSpin,int delete) {
        mVarForPassData.onDataPass(ReadOrWrite,inSpin,outSpin,delete);
    }
    //data pass to activity

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = cm.getActiveNetwork();
        if (nw == null){
            AlertDialog.Builder alertdialog =new AlertDialog.Builder(getContext())
                    .setView(getView())
                    .setTitle(R.string.conn_error_title)
                    .setMessage(R.string.conn_error_body)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        //ok do
                    }).setCancelable(false);
            alertdialog.show();}
    }

    private TextInputEditText mTIET_Output,mTIET_Input;
    private Spinner mSPNin,mSPNout;
    private Button mBTNConvert,mBTNSaveToList,mBTNViewList,mBTNDeleteAll,mBTNWeeklyChange;
    private ArrayAdapter<CharSequence> adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View HomePageView = inflater.inflate(R.layout.homepage_layout_fragment, container, false);
        FindID(HomePageView);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSPNin.setAdapter(adapter);
        mSPNout.setAdapter(adapter);

        mBTNConvert.setOnClickListener(v->{
            Toast.makeText(getContext(),R.string.conversion_requested, Toast.LENGTH_SHORT).show();
            mTIET_Input.clearFocus();
            CompletableFuture<ConvertedResponse> thread = APIRequester.DataFetcher(
                    mSPNin.getSelectedItem().toString(),
                    mSPNout.getSelectedItem().toString(),
                    mTIET_Input.getText().toString());
            thread.thenAcceptAsync((cr -> {
                runOnUiThread(() -> {
                if(cr.getmSuccess().equals("true")){
                    mTIET_Output.setText(cr.getmResult());
                    Toast.makeText(getContext(),R.string.conversion_done, Toast.LENGTH_SHORT).show();
                }else{
                    mTIET_Output.setText(R.string.errorAPIMessage);
                    Toast.makeText(getContext(),R.string.errorAPIMessage, Toast.LENGTH_SHORT).show();
                }
                });//ui thread operation
            }));//convert thread
        });//listener

        mBTNSaveToList.setOnClickListener(v->{
            passData(true,mSPNin.getSelectedItem().toString(),mSPNout.getSelectedItem().toString(),0);
            Toast.makeText(getContext(),R.string.favourite_added, Toast.LENGTH_SHORT).show();
        });

        mBTNViewList.setOnClickListener(v->{
            passData(false,"","",0);
            //pulse dialog
        });

        mBTNDeleteAll.setOnClickListener(v->{
            passData(false,"","",99);
            Toast.makeText(getContext(),R.string.favourite_removed, Toast.LENGTH_SHORT).show();
        });

        mBTNWeeklyChange.setOnClickListener(v->{
            Toast.makeText(getContext(),R.string.data_request_weekly,Toast.LENGTH_SHORT).show();
            ShowWeeklyFromAPI();
        });
        return HomePageView;
    }

    public void ShowWeeklyFromAPI(){
        CompletableFuture<List<CurrencyChangeDay>> ThreadForWeekly = APIRequester.WeeklyFetcher(
                mSPNin.getSelectedItem().toString(),
                mSPNout.getSelectedItem().toString());

        //then accept return void then apply return something
        //async change thread-pool/fix by giving it an executor (otherwise same thread if default)
        //no async -> main thread
        ThreadForWeekly.thenAcceptAsync((WeeklyList -> {

            runOnUiThread(() -> {

                    if(WeeklyList != null) {
                        String[] DisplayArray = new String[WeeklyList.size()];

                        for (int i = 0; i < WeeklyList.size(); i++) {
                            DisplayArray[i] = WeeklyList.get(i).getmDate() + " -> " +
                                    WeeklyList.get(i).getmRate();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                .setItems(DisplayArray, null)
                                .setTitle(R.string.weekly_title_dialog)
                                .setPositiveButton(android.R.string.ok, null)
                                .setCancelable(false);
                        builder.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                .setTitle(R.string.weekly_title_dialog)
                                .setMessage(R.string.errorAPIMessage)
                                .setPositiveButton(android.R.string.ok, null)
                                .setCancelable(false);
                        builder.show();
                        Toast.makeText(getContext(),R.string.errorAPIMessage, Toast.LENGTH_SHORT).show();
                    }

            }); //end ui thread runner
        }));//end weekly thread run

    }
    //needed for showing up dialog and other UI related works
    public static void runOnUiThread(Runnable runnable){
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler.post(runnable);
    }

    public void PulseFavourites(List<Currencies> currenciesList){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.favourites_dialog_title);

        if(currenciesList.size()==0){
            builder.setMessage(R.string.no_favourites);
        }

        String[] DisplayArray=new String[currenciesList.size()];

        for(int i=0;i<currenciesList.size();i++){
            DisplayArray[i]=currenciesList.get(i).CurrencyStart+" <--> " +
                    currenciesList.get(i).CurrencyEnd;
        }
        builder.setItems(DisplayArray, (dialog, which) -> {
            int position = adapter.getPosition(currenciesList.get(which).CurrencyStart);
            mSPNin.setSelection(position);

            position = adapter.getPosition(currenciesList.get(which).CurrencyEnd);
            mSPNout.setSelection(position);

            Toast.makeText(getContext(),R.string.favourites_loaded,Toast.LENGTH_SHORT).show();
            }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void FindID(View HomePageView){
        mTIET_Input=HomePageView.findViewById(R.id.txt_inputfirstvalue);
        mTIET_Output=HomePageView.findViewById(R.id.txt_outputsecondvalue);

        mBTNConvert = HomePageView.findViewById(R.id.btn_convert);
        mBTNSaveToList=HomePageView.findViewById(R.id.btn_savevalues);
        mBTNViewList=HomePageView.findViewById(R.id.btn_selectfromsaved);
        mBTNDeleteAll=HomePageView.findViewById(R.id.btn_removeAll);
        mBTNWeeklyChange=HomePageView.findViewById(R.id.btn_weeklychange);

        mSPNin = HomePageView.findViewById(R.id.spn_input);
        mSPNout = HomePageView.findViewById(R.id.spn_output);
    }
}
