/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zeowls.redditu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.zeowls.redditu.data.RedditContract.RedditEntry;
import com.zeowls.redditu.data.RedditContract.CommentEntry;

/**
 * Manages a local database for weather data.
 */
public class RedditDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "redditU.db";

    public RedditDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + RedditEntry.TABLE_NAME + " (" +
                RedditEntry._ID + " INTEGER PRIMARY KEY," +
                RedditEntry.COLUMN_ID + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_CATEGORY + " REAL NOT NULL, " +
                RedditEntry.COLUMN_SUB_REDDIT + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_SUBREDDIT_ID + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_SELF_TEXT_HTML + " TEXT, " +
                RedditEntry.COLUMN_SELF_TEXT + " TEXT, " +
                RedditEntry.COLUMN_SCORE + " INTEGER NOT NULL, " +
                RedditEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_SUBREDDIT_NAME_PREFIXED + " TEXT, " +
                RedditEntry.COLUMN_NUM_COMMENTS + " REAL NOT NULL, " +
                RedditEntry.COLUMN_THUMBNAIL + " TEXT, " +
                RedditEntry.COLUMN_PERMALINK + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_NAME + " TEXT, " +
                RedditEntry.COLUMN_URL + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                RedditEntry.COLUMN_CREATED_UTC + " REAL NOT NULL, " +
                RedditEntry.COLUMN_PARENT_ID + " TEXT, " +
                RedditEntry.COLUMN_BODY + " TEXT, " +
                RedditEntry.COLUMN_BODY_HTML + " TEXT " +
                " );";

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                CommentEntry.COLUMN_REDIIT_KEY + " TEXT, " +
                CommentEntry.COLUMN_REDIIT_ID + " TEXT NOT NULL, " +
                CommentEntry.COLUMN_HTML_TEXT + " TEXT NOT NULL, " +
                CommentEntry.COLUMN_TEXT + " TEXT, " +
                CommentEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                CommentEntry.COLUMN_POINTS + " INTEGER NOT NULL," +

                CommentEntry.COLUMN_POSTED_ON + " INTEGER NO NULL, " +
                CommentEntry.COLUMN_LEVEL + " INTEGER NO NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + CommentEntry.COLUMN_REDIIT_KEY + ") REFERENCES " +
                RedditEntry.TABLE_NAME + " (" + RedditEntry.COLUMN_ID + "));";

//                // To assure the application have just one weather entry per day
//                // per location, it's created a UNIQUE constraint with REPLACE strategy
//                " UNIQUE (" + CommentEntry.COLUMN_REDIIT_KEY + ", " +
//                CommentEntry.COLUMN_REDIIT_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RedditEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
