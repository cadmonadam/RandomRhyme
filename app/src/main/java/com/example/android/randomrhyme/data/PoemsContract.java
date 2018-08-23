package com.example.android.randomrhyme.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PoemsContract {
    private static final String AUTHORITY = "com.example.android.randomrhyme";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String PATH_POEMS = "poems";

    public static final class PoemEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POEMS).build();

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String TABLE_NAME = "poems";
        public static final String COLUMN_POEM_BODY_TEXT = "poemBodyText";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_POEM_TITLE = "poemTitle";
    }
}

