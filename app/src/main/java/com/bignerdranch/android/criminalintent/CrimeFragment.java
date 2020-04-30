package com.bignerdranch.android.criminalintent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    public static final String ARG_CRIME_ID = "crime_id";
    public static final String DIALOG_DATE = "DialogDate";
    public static final String DIALOG_TIME = "DialogTime";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mTimeButton;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private static final String TAG = "CrimeFragment";

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle idArgs = new Bundle();
        idArgs.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(idArgs);
        return fragment;
    }

    // this method is public because it will need to be accessed by whatever activity is hosting
    // the fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    // this method inflates the layout for the fragment's view and returns the inflated View to the
    // hosting activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        // wires up the EditText widget
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // wires up Date button
        mDateButton = v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: running");
                Intent intent = new Intent(getActivity(), DatePickerActivity.class);
                intent.putExtra(DIALOG_DATE, mCrime.getDate());
                startActivityForResult(intent, REQUEST_DATE);
                /*
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
                */
            }
        });

        mTimeButton = v.findViewById(R.id.crime_time);
        mTimeButton.setText("Time");
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        // checkbox
        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        else if(requestCode == REQUEST_DATE) {
            if(data == null) {
                return;
            }
            mCrime.setDate((Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE));
            updateDate();
        }
        else if(requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0);
            String time = hour + ":" + minute;
            mTimeButton.setText(time);
        }
    }

    public void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
