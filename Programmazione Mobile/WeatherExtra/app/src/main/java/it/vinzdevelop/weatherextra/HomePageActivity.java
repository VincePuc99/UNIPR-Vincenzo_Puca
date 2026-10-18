package it.vinzdevelop.weatherextra;

import static it.vinzdevelop.weatherextra.Data.DataHelper.getmUserDAO;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import it.vinzdevelop.weatherextra.UI.HomePageFragment;

public class HomePageActivity extends SingleFragmentActivity implements HomePageFragment.OnDataPass{
    @Override
    protected Fragment createFragment() {
        return new HomePageFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        LogOut();
    }

    @Override
    public void onDataPass() {
        LogOut();
    }

    private void LogOut() {
        getmUserDAO().RemoveAllLoggedUser();
        finish();
    }


}
