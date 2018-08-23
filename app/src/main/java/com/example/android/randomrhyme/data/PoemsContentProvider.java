package com.example.android.randomrhyme.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.randomrhyme.data.PoemsContract.PoemEntry.TABLE_NAME;

public class PoemsContentProvider extends ContentProvider {

    private PoemsDbHelper poemsDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        poemsDbHelper = new PoemsDbHelper(context);
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = poemsDbHelper.getReadableDatabase();

        Cursor cursor;
        cursor = db.query(TABLE_NAME,
                strings,
                s,
                strings1,
                null,
                null,
                s1);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = poemsDbHelper.getWritableDatabase();
        Uri returnUri;

        long id = db.insert(TABLE_NAME, null, contentValues);
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(PoemsContract.PoemEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = poemsDbHelper.getWritableDatabase();
        int poemsDeleted;

        String id = uri.getPathSegments().get(1);

        poemsDeleted = db.delete(TABLE_NAME, PoemsContract.PoemEntry.COLUMN_ID + "=?", new String[]{id});

        if (poemsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return poemsDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not implemented");
    }
}