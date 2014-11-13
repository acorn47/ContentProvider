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
import android.database.sqlite.SQLiteConstraintException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.example.com.rottentomatillos.data.TomatilloDBHelper;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;

/**
 * This is the main activity for the RottenTomatillos App.
 */
public class MainActivity extends ActionBarActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertData();
    }

    /**
     * Inserts dummy data into the movie rating database.
     * To keep the code simple for this toy app we are inserting the data here.
     * Normally this should be done on a separate thread, using {@link android.os.AsyncTask}
     * or similar method.
     */
    private void insertData() {
        // Create a DBHelper
        TomatilloDBHelper dbHelper = new TomatilloDBHelper(this);
        // Get a WritableDatabase, this is when the database is actually created if it does not
        // already exist.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create the values for 10 movies and insert them in the database
        String[] titles = new String[] {
                "Eternal Sunshine of the Spotless Mind",
                "Oldboy",
                "Ponyo",
                "Frozen",
                "Let the Right One In",
                "Amelie",
                "Pan's Labyrinth",
                "City of God",
                "Akira",
                "Some Like It Hot"};
        int[] ratings = new int[]{5,5,1,2,3,5,5,4,3,4};

        // Go through the arrays and make all of the movies, finally insert into the database.
        for (int i = 0; i < titles.length; i++) {
            ContentValues values = new ContentValues();
            values.put(Movie.TITLE, titles[i]);
            values.put(Movie.RATING, ratings[i]);
            // Insert the movie and catch the exception if it's already in the database.
            try {
                db.insertOrThrow(Movie.TABLE_NAME, null, values);
            } catch (SQLiteConstraintException e) {
                Log.i(LOG_TAG,
                        "Trying to insert " + values.getAsString(Movie.TITLE) +
                        " but it's already in the database.");
                // Do nothing if the movie is already there.
            }
        }

        TextView textView = (TextView)findViewById(R.id.tomatillo_text_view);
        textView.setText("");

        // Read from the database only the Title and Rating columns
        Cursor cursor = db.query(
                Movie.TABLE_NAME,
                new String[]{Movie.TITLE, Movie.RATING},
                null, null, null, null, null);
        // Try block so that we can have a "finally" block to close the cursor.
        try {
            // Note that the Title column is mapped to index 0
            // Note that the Rating column is mapped to index 1
            while (cursor.moveToNext()) {
                textView.append(cursor.getString(0) + " : ");
                // Prints the correct number of stars
                for (int i = 0; i < cursor.getInt(1); i++) textView.append("*");
                textView.append("\n");
            }
        } finally {
            // Make sure to close your cursor!
            cursor.close();
        }
    }
}
