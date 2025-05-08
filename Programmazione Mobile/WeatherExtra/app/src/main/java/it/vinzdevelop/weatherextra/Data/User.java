package it.vinzdevelop.weatherextra.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int UsrId;

    @ColumnInfo(name = "Username")
    public String Username;

    @ColumnInfo(name = "Password")
    public String Password;

    @ColumnInfo(name="LoggedToken")
    public boolean Token;
}