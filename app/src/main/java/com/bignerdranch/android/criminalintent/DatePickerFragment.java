package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    private static final String TAG = "DatePickerFragment";
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";
    private DatePicker mDatePicker;
    private AlertDialog mAlertDialog;
    private Button mOKButton;

    public static DatePickerFragment newInstance(Date date) {
        Log.d(TAG, "newInstance: running");
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode, Date date) {
        Log.d(TAG, "sendResult: running");
        Intent data = new Intent();
        data.putExtra(EXTRA_DATE, date);
        if(getTargetFragment() == null) {
            getActivity().setResult(Activity.RESULT_OK, data);
        }
        else {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
        }
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date, container, false);

        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mOKButton = v.findViewById(R.id.ok_button);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .create();
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: running");
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year,month,day).getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        });
        return v;
    }
}
