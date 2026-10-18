package it.vinzdevelop.weatherextra;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment() throws JSONException;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.base_fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            try {
                fragment = createFragment();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
