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

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.rottentomatillos.data.TomatilloDBHelper;
import android.net.Uri;
import android.test.ApplicationTestCase;
import android.example.com.rottentomatillos.data.TomatilloProvider;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;

/**
 * This is a collection of tests for the associated Content Provider. See
 * {@link TomatilloProvider}
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     *     Setup is called before each test
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    // TODO Add your code here for tearDown.

    /**
     * Tests {@link TomatilloProvider}'s query method with an empty table.
     */
    public void testQueryEmptyTable(){
        // Table queried should be empty since setup was called and everything was destroyed in
        // the table.

        // TODO Add your code here.

    }

    /**
     * Tests {@link TomatilloProvider}'s query method with an invalid ID.
     */
    // Runs because it has the word test in front.
    public void testQueryBadID(){
        Cursor cursor = null;
        try {
            // Appending the bad ID -1. This should cause an UnsupportedOperationException.
            cursor = mContext.getContentResolver().query(
                    ContentUris.withAppendedId(Movie.CONTENT_URI, -1),
                    null, null, null, null);
            // If it did not throw an UnsupportedOperationException, FAIL!
            fail("Query given -1, should throw UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // This is the expected case.
        } finally {
            // Always close your cursor.
            if (cursor != null) cursor.close();
        }
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with one entry.
     */
    public void testInsertOne() {
        ContentValues values = new ContentValues();
        values.put(Movie.TITLE, "Pulp Fiction");
        values.put(Movie.RATING, 5);

        // Insert the values.
        Uri uri = mContext.getContentResolver().insert(
                Movie.CONTENT_URI, values);

        // Checks that there is one value in the database
        assertResultCount(Movie.CONTENT_URI, 1);

        // Get the value in the database.
        assertCorrectStoredValues(uri, values);
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with a null entry.
     */
    public void testInsertNull() {
        // TODO Add your code here.
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with an invalid rating.
     */
    public void testInsertInvalidRating() {
        // TODO Add your code here.
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with a missing rating.
     */
    public void testInsertNullRating() {
        ContentValues values = new ContentValues();
        values.put(Movie.TITLE, "Pulp Fiction");

        // Note how we do not store a rating in values.
        Uri uri = mContext.getContentResolver().insert(
                Movie.CONTENT_URI, values);

        // A failed insert will return null.
        assertNull("URI is not null even though no rating was given", uri);
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with a missing title.
     */
    public void testInsertTitleNull() {
        // TODO Add your code here.
    }

    /**
     * Helper Methods are below
     */

    /**
     * Helper method to delete all of the record in the database.
     */
    public void deleteAllRecords() {
        SQLiteDatabase db = new TomatilloDBHelper(mContext).getWritableDatabase();
        // Because we haven't finished the content provide delete, we'll just do the database's
        // method. This will need to be changed.
        db.delete(
                Movie.TABLE_NAME,
                null,
                null
        );
    }

    /**
     * Helper method to test whether the number of objects returned in a query matches the
     * expected amount in the table.
     */
    public void assertResultCount(Uri uri, String[] projection, String selection,
                                  String[] selectionArgs, int expectedCount) {
        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection,
                selectionArgs, null);
        try {
            assertEquals("Row count " + cursor.getCount(), expectedCount, cursor.getCount());
        } finally {
            cursor.close();
        }
    }

    /**
     * Shortened form of the method assertResultCount if no additional where clause or projection
     * is needed.
     */
    private void assertResultCount(Uri uri, int expectedCount) {
        assertResultCount(uri, null, null, null, expectedCount);
    }

    /**
     * Helper method to test whether the object stored at the URI has the same values as the
     * ContentValues passed as a parameter.
     */
    private void assertCorrectStoredValues(Uri uri, ContentValues values) {
        Cursor cursor = mContext.getContentResolver().query(uri,
                new String[] {
                        Movie._ID,
                        Movie.TITLE,
                        Movie.RATING },
                null, null, null);
        try {
            // Assert there is only one entry.
            assertEquals("Row count " + cursor.getCount(), 1, cursor.getCount());

            //Move to the first and only row.
            cursor.moveToFirst();
            //If it contains the key, assert the value is the same.
            if (values.containsKey(Movie._ID)) {
                assertEquals(
                        new Long(cursor.getLong(0)),
                        values.getAsLong(Movie._ID)
                );
            }
            if (values.containsKey(Movie.TITLE)) {
                assertEquals(
                        cursor.getString(1),
                        values.getAsString(Movie.TITLE)
                );
            }
            if (values.containsKey(Movie.RATING)) {
                assertEquals(
                        new Integer(cursor.getInt(2)),
                        values.getAsInteger(Movie.RATING)
                );
            }
        } finally {
            cursor.close();
        }
    }
}