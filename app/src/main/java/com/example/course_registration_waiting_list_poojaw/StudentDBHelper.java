package com.example.course_registration_waiting_list_poojaw;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StudentDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;

//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + StudentContract.StudentEntry.TABLE_NAME + " (" +
//                    StudentContract.StudentEntry._ID + " INTEGER PRIMARY KEY," +
//                    StudentContract.StudentEntry.COLUMN_NAME + " TEXT," +
//                    StudentContract.StudentEntry.COLUMN_PRIORITY + " INTEGER)";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StudentContract.StudentEntry.TABLE_NAME + " (" +
                    StudentContract.StudentEntry._ID + " INTEGER PRIMARY KEY," +
                    StudentContract.StudentEntry.COLUMN_NAME + " TEXT," +
                    StudentContract.StudentEntry.COLUMN_PRIORITY + " TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StudentContract.StudentEntry.TABLE_NAME;

    public StudentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(StudentContract.StudentEntry.TABLE_NAME,
                StudentContract.StudentEntry._ID + "=?",
                new String[]{String.valueOf(studentId)});
        db.close();
    }

    public void updateStudent(int studentId, String name, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        values.put(StudentContract.StudentEntry.COLUMN_PRIORITY, priority);

        String selection = StudentContract.StudentEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};

        int count = db.update(
                StudentContract.StudentEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        Log.d(TAG, "Updated " + count + " rows");

        db.close();
    }

}


