package com.bignerdranch.android.criminalintent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    // s prefix, convention used to indicate the variable is static
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "CrimeLab";

    // returns an instance if it already doesn't exist
    public static CrimeLab get(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    // private constructor
    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        for(Crime x : crimes) {
            Log.d(TAG, "getCrimes: " + x.getDate());
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        Log.d(TAG, "getCrime: Big Boi Hours");
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
                );
        try {
            if(cursor.getCount() == 0) {
                Log.d(TAG, "getCrime: It is null");
                return new Crime();
            }
            cursor.moveToFirst();
            Log.d(TAG, "getCrime: After moveToFirst()");
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Log.d(TAG, "queryCrimes: Before cursor");
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        Log.d(TAG, "queryCrimes: After Cursor"); 
        return new CrimeCursorWrapper(cursor);
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(Crime c) {
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?",
                new String[] { c.getId().toString() }) ;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }
}
