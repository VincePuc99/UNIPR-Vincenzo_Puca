package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mTimePicker;
    private Button mSuspectCall;
    private static final int REQUEST_PHOTO= 2;
    private ImageButton mPhotoButton;
    private ImageButton mPhotoView;
    private File mPhotoFile;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_Time= "DialogTime";
    private static final int REQUEST_DATE=0;
    private Button mReportButton;
    private static final int REQUEST_CONTACT = 1;
    private Button mSuspectButton;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args=new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeID);

        CrimeFragment crimeFragment=new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID=(UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField=v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDateButton=v.findViewById(R.id.crime_date);

        mDateButton.setOnClickListener( view -> {
            FragmentManager fm=getFragmentManager();
            DatePickerFragment dpf=DatePickerFragment.newInstance(mCrime.getDate());
            dpf.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
            dpf.show(fm,DIALOG_DATE);
        });

        mTimePicker=v.findViewById(R.id.crime_time_picker);
        mTimePicker.setOnClickListener(view -> {
            FragmentManager fm=getParentFragmentManager();
            TimePickerFragment tpf=new TimePickerFragment();

            tpf.show(fm,DIALOG_Time);

            getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {

                Date result =(Date) bundle.getSerializable("bundleKey");
                Date timeonly = mCrime.getDate();
                timeonly.setHours(result.getHours());
                timeonly.setMinutes(result.getMinutes());
                mCrime.setDate(timeonly);
                updateDate(false);
            });
        });

        mSolvedCheckbox=v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {mCrime.setSolved(isChecked);updateCrime();});
        updateDate(true);
        updateDate(false);

        mReportButton =v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener( view ->{
            String mimeType = "text/plain";
            Intent i =  ShareCompat.IntentBuilder.from(getActivity())
                    .setType(mimeType)
                    .setText(getCrimeReport())
                    .setSubject(getString(R.string.crime_report_subject))//titolo
                    .setChooserTitle(getString(R.string.send_report))
                    .getIntent();
            i = Intent.createChooser(i, getString(R.string.send_report));
            startActivity(i);
        });

        Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton=v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(view -> {
            startActivityForResult(pickContact, REQUEST_CONTACT);
        });

        mSuspectCall=v.findViewById(R.id.call_suspect);
        mSuspectCall.setOnClickListener(view -> {
            Uri number = Uri.parse("tel:"+mSuspectCall.getText());
            Intent Call = new Intent(Intent.ACTION_DIAL, number);
            startActivity(Call);
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        CheckPickedContact();

        mPhotoButton = v.findViewById(R.id.crime_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener( v1 -> {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.criminalintent.fileprovider",
                    mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            List<ResolveInfo> cameraActivities = getActivity()
                    .getPackageManager().queryIntentActivities(captureImage,
                            PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo activity : cameraActivities) {
                getActivity().grantUriPermission(activity.activityInfo.packageName,
                        uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            mPhotoView.setEnabled(true);
            startActivityForResult(captureImage, REQUEST_PHOTO);
        });

        mPhotoView = v.findViewById(R.id.crime_photo);
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setEnabled(false);
        }else mPhotoView.setEnabled(true);
        mPhotoView.setOnClickListener(View->{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            ZoomInPhotoFragment.SendScaledBitmap(bitmap);
            new ZoomInPhotoFragment().show(getParentFragmentManager(),ZoomInPhotoFragment.DIALOG_TAG);
        });


        v.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            updatePhotoView();
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_crime:
                CrimeLab.DeleteCrime(mCrime.getId());
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getCrimeReport() {
        String solvedString;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap( mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(PictureUtils.RotateBitmap(bitmap,90));
        }
    }
    private void CheckPickedContact() {
        if (mCrime.getSuspect() == null) {
            mSuspectCall.setEnabled(false);
        }else
            mSuspectCall.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date =(Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate(true);

        }else if (requestCode == REQUEST_CONTACT && data != null) {
            mSuspectCall.setText("");
            Uri contactUri = data.getData();

            String WHERE_CONDITION = ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
            String[] SELECT = { ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DATA1 };
            String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME;

            Cursor cur =  getActivity().getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    SELECT,
                    WHERE_CONDITION,
                    null,
                    SORT_ORDER);

            // Specify which fields you want your query to return values for
            String[] queryFields = new String[]{ ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };

            // Perform your query - the contactUri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(
                    contactUri,
                    queryFields,
                    null,
                    null,
                   null);
            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name
                c.moveToFirst();

                String SuspectID=c.getString(0);
                String suspect = c.getString(1);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
                CheckPickedContact();
                updateCrime();

                cur.moveToFirst();

                for(int i=0;i<3;i++){
                    String checkDataID=cur.getString(0);
                    if(checkDataID.equals(SuspectID)){
                        mSuspectCall.setText(cur.getString(1));
                        break;
                    }
                    cur.moveToNext();
                }

            } finally {
                cur.close();
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {

        Uri uri = FileProvider.getUriForFile(getActivity(),
                "com.bignerdranch.android.criminalintent.fileprovider",
                mPhotoFile);
        getActivity().revokeUriPermission(uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateCrime();
        updatePhotoView();
    }

    }
    private void updateDate(Boolean both) {
        if(both==false){
        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");
        String time = TimeFormat.format(mCrime.getDate());
        mTimePicker.setText(time);}else{
        SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String date = DateFormat.format(mCrime.getDate());
        mDateButton.setText(date);}
    }
}