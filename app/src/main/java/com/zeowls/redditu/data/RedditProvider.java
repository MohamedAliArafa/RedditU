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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class RedditProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RedditDbHelper mOpenHelper;

    static final int COMMENT = 100;
    static final int COMMENT_WITH_REDDIT = 101;
    //    static final int Reddit_WITH_LOCATION_AND_DATE = 102;
    static final int REDDIT = 300;

    private static final SQLiteQueryBuilder sRedditByLocationSettingQueryBuilder;

    static {
        sRedditByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //COMMENT INNER JOIN location ON COMMENT.location_id = location._id
        sRedditByLocationSettingQueryBuilder.setTables(
                RedditContract.RedditEntry.TABLE_NAME + " INNER JOIN " +
                        RedditContract.CommentEntry.TABLE_NAME +
                        " ON " + RedditContract.CommentEntry.TABLE_NAME +
                        "." + RedditContract.CommentEntry.COLUMN_REDIIT_KEY +
                        " = " + RedditContract.RedditEntry.TABLE_NAME +
                        "." + RedditContract.RedditEntry._ID);
    }

    //location.location_setting = ?
    private static final String sLocationSettingSelection =
            RedditContract.RedditEntry.TABLE_NAME +
                    "." + RedditContract.RedditEntry.COLUMN_ID + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sLocationSettingWithStartDateSelection =
            RedditContract.RedditEntry.TABLE_NAME +
                    "." + RedditContract.RedditEntry.COLUMN_ID + " = ? AND " +
                    RedditContract.CommentEntry.COLUMN_POSTED_ON + " >= ? ";

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelection =
            RedditContract.RedditEntry.TABLE_NAME +
                    "." + RedditContract.RedditEntry.COLUMN_ID + " = ? ";

    private Cursor getCommentsByReddit(Uri uri, String[] projection, String sortOrder) {
        String Reddit = RedditContract.CommentEntry.getRedditFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sLocationSettingSelection;
        selectionArgs = new String[]{Reddit};

        return sRedditByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the COMMENT, COMMENT_WITH_REDDIT, Reddit_WITH_LOCATION_AND_DATE,
        and REDDIT integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RedditContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, RedditContract.PATH_COMMENT, COMMENT);
        matcher.addURI(authority, RedditContract.PATH_COMMENT + "/*", COMMENT_WITH_REDDIT);

        matcher.addURI(authority, RedditContract.PATH_REDDIT, REDDIT);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new RedditDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new RedditDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases

            case COMMENT_WITH_REDDIT:
                return RedditContract.CommentEntry.CONTENT_TYPE;
            case COMMENT:
                return RedditContract.CommentEntry.CONTENT_TYPE;
            case REDDIT:
                return RedditContract.RedditEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "COMMENT/*"
            case COMMENT_WITH_REDDIT: {
                retCursor = getCommentsByReddit(uri, projection, sortOrder);
                break;
            }
            // "COMMENT"
            case COMMENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RedditContract.CommentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case REDDIT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RedditContract.RedditEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case COMMENT: {
                normalizeDate(values);
                long _id = db.insert(RedditContract.CommentEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = RedditContract.CommentEntry.buildCommentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REDDIT: {
                long _id = db.insert(RedditContract.RedditEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = RedditContract.RedditEntry.buildRedditUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case COMMENT:
                rowsDeleted = db.delete(
                        RedditContract.CommentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REDDIT:
                rowsDeleted = db.delete(
                        RedditContract.RedditEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(RedditContract.RedditEntry.COLUMN_CREATED_UTC)) {
            long dateValue = values.getAsLong(RedditContract.RedditEntry.COLUMN_CREATED_UTC);
            values.put(RedditContract.RedditEntry.COLUMN_CREATED_UTC, RedditContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case COMMENT:
                normalizeDate(values);
                rowsUpdated = db.update(RedditContract.CommentEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REDDIT:
                rowsUpdated = db.update(RedditContract.RedditEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COMMENT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(RedditContract.CommentEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REDDIT:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(RedditContract.RedditEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}