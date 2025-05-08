package it.vinzdevelop.weatherextra;

import static it.vinzdevelop.weatherextra.Data.DataHelper.getmUserDAO;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import it.vinzdevelop.weatherextra.Data.DataHelper;
import it.vinzdevelop.weatherextra.UI.LoginFragment;

public class MasterActivity extends SingleFragmentActivity implements LoginFragment.OnDataPass {
    public static final String ERROR_LOGIN_TOKEN ="ErrorLoginToken";
    public static final String TAGLOGGEDID="LoggedUser";
    private int mLoggedID=0;
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataHelper.buildDBnDAO(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoggedID = getmUserDAO().GetLoggedID();
        if(mLoggedID!=0){ StartHomePage();}
    }

    @Override
    public void onDataPass(String paramUsrNm,String paramPsw,boolean register) {

        if(!register) {
            //check in DB passed data
            if (getmUserDAO().VerifyLogin(paramUsrNm, paramPsw)) {

                LoginFragment.CheckLoginResultToken(true);

                int updateresult = getmUserDAO().AddToken(paramUsrNm, paramPsw);

                if (updateresult == 0) {
                    Log.e(ERROR_LOGIN_TOKEN, "onDataPass: Error inserting token");
                    finish();
                } else {
                    mLoggedID = getmUserDAO().GetLoggedID();
                }

                StartHomePage();

            } else {
                LoginFragment.CheckLoginResultToken(false);
            }
        }else{
            //want register
StartRegister();
        }
    }//on data pass

    private void StartHomePage(){
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra(TAGLOGGEDID,mLoggedID);
        startActivity(intent);
    }

    private void StartRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}