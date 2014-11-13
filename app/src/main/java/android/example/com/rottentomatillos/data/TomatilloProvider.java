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
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
import android.net.Uri;

/**
 * This is a ContentProvider for the movie rating database. This content provider
 * works with {@link TomatilloContract} and {@link TomatilloDBHelper} to provide managed and secure
 * access to the movie database.
 */
public class TomatilloProvider extends ContentProvider {

    /**
     * This helps us create and gain access to the SQLiteDatabase.
     */
    private TomatilloDBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new TomatilloDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        if (uri.equals(Movie.CONTENT_URI)) {
            // Use the database helper to get a readable database.
            final SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    Movie.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, sortOrder);
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
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
