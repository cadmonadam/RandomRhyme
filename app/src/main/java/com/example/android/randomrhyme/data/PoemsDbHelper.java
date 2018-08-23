package com.example.android.randomrhyme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PoemsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite_poems.db";

    private static final int DATABASE_VERSION = 1;

    public PoemsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POEMS_TABLE = "CREATE TABLE " + PoemsContract.PoemEntry.TABLE_NAME + " (" +
                PoemsContract.PoemEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PoemsContract.PoemEntry.COLUMN_POEM_BODY_TEXT + " TEXT NOT NULL, " +
                PoemsContract.PoemEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                PoemsContract.PoemEntry.COLUMN_POEM_TITLE + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_POEMS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PoemsContract.PoemEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
