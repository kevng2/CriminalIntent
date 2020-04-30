package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";
    public static final String EXTRA_HOUR = "com.bignerdranch.android.criminalintent.hour";
    public static final String EXTRA_MINUTE = "com.bignerdranch.android.criminalintent.minute";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        args.putInt(ARG_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        args.putInt(ARG_MINUTE, calendar.get(Calendar.MINUTE));

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode, int hour, int minute) {
        Intent data = new Intent();
        data.putExtra(EXTRA_HOUR, hour);
        data.putExtra(EXTRA_MINUTE, minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        int hour = getArguments().getInt(ARG_HOUR);
        int minute = getArguments().getInt(ARG_MINUTE);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mTimePicker.setHour(hour);
        else
            mTimePicker.setCurrentHour(hour);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mTimePicker.setMinute(minute);
        else
            mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Time of crime:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = mTimePicker.getHour();
                        }
                        int minute = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            minute = mTimePicker.getMinute();
                        }
                        sendResult(Activity.RESULT_OK, hour, minute);
                    }
                })
                .create();
    }
}
