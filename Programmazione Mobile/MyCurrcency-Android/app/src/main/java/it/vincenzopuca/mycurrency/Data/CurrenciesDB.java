package it.vincenzopuca.mycurrency.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Currencies.class}, version = 1)
public abstract class CurrenciesDB extends RoomDatabase {
    public abstract CurrenciesDAO currencyDAO();
}