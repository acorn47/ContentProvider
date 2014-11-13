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

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.rottentomatillos.data.TomatilloDBHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

        // -- YOUR CODE BELOW HERE -- //

        // Create the values for 10 movies and insert them in the database
        String[] titles = new String[] { };
        int[] ratings = new int[] { };

        // Insert the movie and catch the exception if it's already in the database.
        try {

        } catch (SQLiteConstraintException e) {
            Log.i(LOG_TAG,
                    "Trying to insert but it's already in the database.");
            // Do nothing if the movie is already there.
        }

    }

}
