package it.vincenzopuca.mycurrency.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Currencies {
    @PrimaryKey(autoGenerate = true)
    public int CurrenciesID;

    @ColumnInfo(name = "CurrencyStart")
    public String CurrencyStart;

    @ColumnInfo(name = "CurrencyEnd")
    public String CurrencyEnd;

}
