package it.vinzdevelop.weatherextra.Data;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class DataHelper extends AppCompatActivity {
    private static UserDB mDB;
    private static UserDAO mUserDAO;
    public static void buildDBnDAO(Context context){
        if(mDB==null){
        mDB= Room.databaseBuilder(context,
                        UserDB.class, "WheaterExtraDataBase").allowMainThreadQueries()//for small db this is good
                .build(); //however is not recommended locking main thread for bigger db
        mUserDAO = mDB.userDao();
        }
    }
    public static UserDAO getmUserDAO() {
        return mUserDAO;
    }

}
