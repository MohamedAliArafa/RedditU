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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class RedditContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.zeowls.redditu";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_REDDIT = "reddit";
    public static final String PATH_COMMENT = "comment";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the location table */
    public static final class RedditEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REDDIT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REDDIT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REDDIT;

        // Table name
        public static final String TABLE_NAME = "reddit";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_CONTEST_MODE = "contest_mode";
        public static final String COLUMN_SUB_REDDIT = "subreddit";
        public static final String COLUMN_SELF_TEXT_HTML = "selftext_html";
        public static final String COLUMN_SELF_TEXT = "selftext";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_SUBREDDIT_NAME_PREFIXED = "subreddit_name_prefixed";
        public static final String COLUMN_NUM_COMMENTS = "num_comments";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_SUBREDDIT_ID = "subreddit_id";
        public static final String COLUMN_PERMALINK = "permalink";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CREATED_UTC = "created_utc";
        public static final String COLUMN_PARENT_ID = "parent_id";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_BODY_HTML = "body_html";

        public static Uri buildRedditUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the weather table */
    public static final class CommentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;

        public static final String TABLE_NAME = "comment";
        public static final String COLUMN_REDIIT_KEY = "reddit_key";
        public static final String COLUMN_REDIIT_ID = "reddit_id";
        public static final String COLUMN_HTML_TEXT = "html_text";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_POSTED_ON = "posted_on";
        public static final String COLUMN_LEVEL = "level";

        public static Uri buildCommentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRedditComment(String redditId) {
            return CONTENT_URI.buildUpon().appendPath(redditId).build();
        }

        public static String getRedditFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
