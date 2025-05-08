package it.vinzdevelop.weatherextra;

import static it.vinzdevelop.weatherextra.Data.DataHelper.buildDBnDAO;
import static it.vinzdevelop.weatherextra.Data.DataHelper.getmUserDAO;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import it.vinzdevelop.weatherextra.Data.DataHelper;
import it.vinzdevelop.weatherextra.Data.User;
import it.vinzdevelop.weatherextra.UI.RegisterFragment;

public class RegisterActivity extends SingleFragmentActivity implements RegisterFragment.OnDataPass {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildDBnDAO(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        //show dialog wanna exit
    }

    @Override
    public void onDataPass(String paramUsrNm, String paramPsw) {
        User usr= new User();
        usr.Username=paramUsrNm;
        usr.Password=paramPsw;
        usr.UsrId= ThreadLocalRandom.current().nextInt(0, 100 + 1);
        usr.Token=false;
       getmUserDAO().insertAll(usr);
       finish();
    }
}
