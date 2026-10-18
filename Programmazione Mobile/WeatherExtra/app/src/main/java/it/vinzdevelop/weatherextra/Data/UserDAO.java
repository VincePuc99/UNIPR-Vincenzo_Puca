package it.vinzdevelop.weatherextra.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDAO {

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM User")
    void deleteall();

    @Query("UPDATE User SET LoggedToken = 1 WHERE Username IS :GivenUsername AND " +
            "Password IS :GivenPassword ")
    int AddToken(String GivenUsername, String GivenPassword);

    @Query("SELECT LoggedToken FROM User WHERE LoggedToken = 1")
    int GetLoggedID();

    @Query("SELECT * FROM User WHERE Username IS :GivenUsername AND " +
    "Password IS :GivenPassword LIMIT 1")
    boolean VerifyLogin(String GivenUsername, String GivenPassword);

    @Query("UPDATE User SET LoggedToken = 0")
    void RemoveAllLoggedUser();
}