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
package android.example.com.rottentomatillos.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
import android.net.Uri;
import android.util.Log;

/**
 * This is a ContentProvider for the movie rating database. This content provider
 * works with {@link TomatilloContract} and {@link TomatilloDBHelper} to provide managed and secure
 * access to the movie database.
 */
public class TomatilloProvider extends ContentProvider {

    public static final String LOG_TAG = TomatilloProvider.class.getSimpleName();

    /**
     * This helps us create and gain access to the SQLiteDatabase.
     */
    private TomatilloDBHelper mDBHelper;

    // URI Matcher Codes
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Builds a UriMatcher object for the movie database URIs.
     */
    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // For each type of URI you want to add, create a corresponding code. Note that UriMatchers
        // Need your content authority.
        matcher.addURI(TomatilloContract.CONTENT_AUTHORITY, Movie.TABLE_NAME, MOVIE);
        matcher.addURI(TomatilloContract.CONTENT_AUTHORITY, Movie.TABLE_NAME + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new TomatilloDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        // Get the constant integer representing the uri type and use in the switch statement.
        switch (sUriMatcher.match(uri)) {
            // Case where all movie ratings are selected
            case MOVIE: {
                Cursor cursor = db.query(
                        Movie.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            }
            // Case with only one movie rating selected, by ID
            case MOVIE_WITH_ID: {
                Cursor cursor = db.query(
                        Movie.TABLE_NAME,
                        projection,
                        Movie._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},

                        null,
                        null,
                        sortOrder
                );
                return cursor;
            }
            default: {
                // In the default case, the uri must have been bad
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                long id = -1;
                // Insert the movie and catch the exception if it's already in the database.
                try {
                    id = mDBHelper.getWritableDatabase().insertOrThrow(
                            Movie.TABLE_NAME, null, contentValues);
                } catch (SQLiteConstraintException e) {
                    Log.i(LOG_TAG,
                            "Trying to insert " + contentValues.getAsString(Movie.TITLE) +
                                    " but it's already in the database."
                    );
                    // Do nothing if the movie is already there.
                }
                if (id == -1) return null; // it failed!
                return ContentUris.withAppendedId(Movie.CONTENT_URI, id);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }
}
