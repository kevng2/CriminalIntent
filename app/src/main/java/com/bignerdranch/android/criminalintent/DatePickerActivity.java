package com.bignerdranch.android.criminalintent;
import android.util.Log;
import androidx.fragment.app.Fragment;
import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {
    private static final String TAG = "DatePickerActivity";

    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "createFragment: running");
        Date date = (Date) getIntent().getSerializableExtra(CrimeFragment.DIALOG_DATE);
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(date);
        return datePickerFragment;
    }
}
