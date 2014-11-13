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
package android.example.com.rottentomatillos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
import android.example.com.rottentomatillos.data.TomatilloDBHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * This is the main activity for the RottenTomatillos App.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a DBHelper
        TomatilloDBHelper dbHelper = new TomatilloDBHelper(this);
        // Get a WritableDatabase, this is when the database is actually created if it does not
        // already exist.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create the values for one movie
        ContentValues values = new ContentValues();
        values.put(Movie.TITLE, "Eternal Sunshine of the Spotless Mind");
        values.put(Movie.RATING, 5);

        // Insert the Movie
        db.insert(Movie.TABLE_NAME, null, values);
    }
}
