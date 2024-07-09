package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    private TimePicker mTimePicker;

    public void SendResult(Date datetime){
        Bundle args = new Bundle();
        args.putSerializable("bundleKey",datetime);
        getParentFragmentManager().setFragmentResult("requestKey", args);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        mTimePicker=v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_string)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY,mTimePicker.getHour());
                    cal.set(Calendar.MINUTE,mTimePicker.getMinute());
                    Date d = cal.getTime();
                    SendResult(d);

                        })
                .create();
    }
}
