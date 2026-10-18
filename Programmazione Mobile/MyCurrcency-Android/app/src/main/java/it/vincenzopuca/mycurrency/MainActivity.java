package it.vincenzopuca.mycurrency;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import it.vincenzopuca.mycurrency.Data.Currencies;
import it.vincenzopuca.mycurrency.Data.DataHelper;
import it.vincenzopuca.mycurrency.UI.HomePageFragment;

public class MainActivity extends SingleFragment implements HomePageFragment.OnDataPass {
    HomePageFragment hpf=new HomePageFragment();
    @Override
    protected Fragment createFragment() {
        return hpf;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataHelper.buildDBnDAO(getApplicationContext());
    }

    @Override //communication fragment activity
    public void onDataPass(boolean ReadOrWrite,String inSpin,String outSpin,int DeleteAll) {
        if(DeleteAll==99){
            DataHelper.getmCurrenciesDAO().deleteall();
        }else if(ReadOrWrite){ //true write
        Currencies cur=new Currencies();
        cur.CurrencyStart=inSpin;
        cur.CurrencyEnd=outSpin;
        DataHelper.getmCurrenciesDAO().insert(cur);
        }
        else{ //false read
            hpf.PulseFavourites(DataHelper.getmCurrenciesDAO().getCurrenciesList());
        }
    }
}
