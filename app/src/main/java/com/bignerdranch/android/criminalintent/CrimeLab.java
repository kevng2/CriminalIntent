package com.bignerdranch.android.criminalintent;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    // s prefix, convention used to indicate the variable is static
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    // returns an instance if it already doesn't exist
    public static CrimeLab get(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    // private constructor
    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();

        // populate the list with 100 boring crimes
        for(int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + 1);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrime() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime crime : mCrimes) {
            if(crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
