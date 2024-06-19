package it.vincenzopuca.mycurrency.Data;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class DataHelper extends AppCompatActivity {
    private static CurrenciesDB mDB;
    private static CurrenciesDAO mCurrencyDAO;
    public static void buildDBnDAO(Context context){
        if(mDB==null){
            mDB= Room.databaseBuilder(context,
                            CurrenciesDB.class, "FavouritesCurrencies").allowMainThreadQueries()//for small db this is good
                    .build(); //however is not recommended locking main thread for bigger db
            mCurrencyDAO = mDB.currencyDAO();
        }
    }
    public static CurrenciesDAO getmCurrenciesDAO() {
        return mCurrencyDAO;
    }
}
