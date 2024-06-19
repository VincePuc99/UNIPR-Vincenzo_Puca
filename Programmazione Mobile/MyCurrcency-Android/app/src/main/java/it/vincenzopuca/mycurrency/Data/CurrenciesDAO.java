package it.vincenzopuca.mycurrency.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrenciesDAO {

    @Insert
    void insert(Currencies... currencies);

    @Query("DELETE FROM Currencies")
    void deleteall();

    @Query("SELECT * FROM Currencies")
    List<Currencies> getCurrenciesList();

}