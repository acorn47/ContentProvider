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
import android.database.Cursor;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

/**
 * This is the main activity for the RottenTomatillos App.
 */
public class MainActivity extends ActionBarActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    
    // Identifies a particular Loader being used in this component.
    private static final int CURSOR_LOADER_ID = 0;

    // For the SimpleCursorAdapter to match the TomatilloProvider columns to layout items.
    private static final String[] COLUMNS_TO_BE_BOUND  = new String[] {
            Movie.TITLE,
            Movie.RATING
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[] {
            android.R.id.text1,
            android.R.id.text2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertData();

        // Get the ListView which will be populated with the TomatilloProvider data.
        ListView listView = (ListView) findViewById(R.id.tomatillo_list_view);

        // Get the cursor
        Cursor cursor = getContentResolver().query(
                Movie.CONTENT_URI, null, null, null, null);

        // Set the Adapter to fill the standard two_line_list_item layout with data from the Cursor.
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                cursor,
                COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL,
                0);

        // Attach the adapter to the ListView.
        listView.setAdapter(adapter);
    }

    /**
     * Inserts dummy data into the movie rating database.
     * To keep the code simple for this toy app we are inserting the data here.
     * Normally this should be done on a separate thread, using {@link android.os.AsyncTask}
     * or similar method.
     */
    private void insertData() {
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
        ContentValues[] ratingsArr = new ContentValues[ratings.length];

        // Go through the arrays and make all of the movies, finally insert into the database.
        for (int i = 0; i < titles.length; i++) {
            ratingsArr[i] = new ContentValues();
            ratingsArr[i].put(Movie.TITLE, titles[i]);
            ratingsArr[i].put(Movie.RATING, ratings[i]);
        }

        getContentResolver().bulkInsert(Movie.CONTENT_URI, ratingsArr);
    }
}
