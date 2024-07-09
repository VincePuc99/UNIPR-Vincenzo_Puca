package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeListFragment.Callbacks {
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mLast;
    private Button mFirst;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent =  new Intent (packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId=(UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mLast=findViewById(R.id.last_button);
        mFirst=findViewById(R.id.first_button);

        mFirst.setOnClickListener(view -> {
            mViewPager.setCurrentItem(0);
            mFirst.setEnabled(false);
        });

        mLast.setOnClickListener(view -> {
            mViewPager.setCurrentItem(mCrimes.size()-1);
            mLast.setEnabled(false);

        });

        mViewPager=findViewById(R.id.crime_view_pager);
        mCrimes=CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager= getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                mFirst.setEnabled(true);
                mLast.setEnabled(true);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for(int i=0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onCrimeSelected(Crime crime) {

    }

}
