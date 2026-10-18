package com.bignerdranch.android.criminalintent;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mCurrentItem;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private TextView mEmptyList;

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmptyList=view.findViewById(R.id.empty);

        CheckEmpty();

        updateUI();
        return view;
    }

    private void CheckEmpty() {
       /* if(CrimeLab.isEmpty()){
            mEmptyList.setVisibility(View.VISIBLE);
        }else{mEmptyList.setVisibility(View.INVISIBLE);}*/
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyItemChanged(mCurrentItem);
        }
        mAdapter.setCrimes(crimes);

        updateSubtitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        CheckEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_crime:
            Crime crime=new Crime();
            CrimeLab.get(getActivity()).AddCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onCreate(Bundle SavedInstance) { //dire all'activity che il fragment ha il menu
        super.onCreate(SavedInstance);
        setHasOptionsMenu(true);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime mCrime;
        private ImageView mSolvedCrimeIMG= itemView.findViewById(R.id.img_crime_solved);
        private TextView mTitleTextView= itemView.findViewById(R.id.crime_title);
        private TextView mDateTextView = itemView.findViewById(R.id.crime_date);

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCrimeIMG.setVisibility(crime.isSolved() ? View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onCrimeSelected(mCrime);
            mCurrentItem=getAdapterPosition();
        }
    }

    private class CrimeHolderExtended extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Crime mCrime;
        private final ImageView mSolvedCrimeIMG = itemView.findViewById(R.id.img_crime_solved_detailed);
        private final TextView mTitleTextView = itemView.findViewById(R.id.crime_title);
        private final TextView mDateTextView= itemView.findViewById(R.id.crime_date);

        public CrimeHolderExtended(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_detailed, parent, false));
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCrimeIMG.setVisibility(crime.isSolved() ? View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View view) {
          //Intent intent= new Intent(getActivity(),CrimeActivity.class);
         // startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType==1){
                return new CrimeHolder(layoutInflater, parent);//holder extended
            }
            return new CrimeHolder(layoutInflater, parent);
        }
        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);

            final int itemType = getItemViewType(position);
            if(itemType==1){
                ((CrimeHolder)holder).bind(crime);//holder extended
            }else{
                ((CrimeHolder)holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int pos){
            Crime crime=mCrimes.get(pos);
            if(crime.isRequirePolice()){return 1;}
            return 0;
        }
    }
}
