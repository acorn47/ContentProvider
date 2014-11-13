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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
/**
 * This helps organize database versions and gives easy access to a
 * SQLiteDatabase object.
 */
public class TomatilloDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = TomatilloDBHelper.class.getSimpleName();

    /**
     * Stores the current version of the database, starting at one. If you change the database schema,
     * you must increment the database version.
     * */
    private static final int DATABASE_VERSION = 1;
    /**
     * The name of the sqlite database file on the device
     */
    private static final String DATABASE_NAME = "tomatillo_database.db";

    public TomatilloDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creates a database with a single table to store movie ratings.
        Log.i(LOG_TAG, "Bootstrapping database version: " + DATABASE_VERSION);

        sqLiteDatabase.execSQL(
                "CREATE TABLE " +Movie.TABLE_NAME + " (" +
                        Movie._ID + " INTEGER PRIMARY KEY," +
                        Movie.TITLE + " TEXT UNIQUE NOT NULL, " +
                        Movie.RATING + " INTEGER NOT NULL " +
                        " );"
        );
    }

    // This method is used if the schema of the table changes. In this simplified example, we are
    // dropping (which completely deletes) the old data, before remaking the table with the new
    // updated schema.
    // Less destructive implementations could use alter table to save the current version of the
    // table and then create the new, updated table and populate it with the old data.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {

        Log.i(LOG_TAG,
                String.format("Upgrading database from version %d to %d", oldVersion, newVersion));
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Movie.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
