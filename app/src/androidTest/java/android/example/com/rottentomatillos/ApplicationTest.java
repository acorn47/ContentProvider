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
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
import android.example.com.rottentomatillos.data.TomatilloProvider;
import android.net.Uri;
import android.test.ApplicationTestCase;

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

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteAllRecords();
    }

    /**
     * Tests {@link TomatilloProvider}'s query method with an empty table.
     */
    public void testQueryEmptyTable(){
        // Table queried should be empty since setup was called and everything was destroyed in
        // the table.
        assertResultCount(Movie.CONTENT_URI, 0);
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
     * Tests {@link TomatilloProvider}'s getType method with
     * both datatypes.
     */
    public void testGetType() {
        ContentResolver r = getContext().getContentResolver();
        assertEquals(
                r.getType(Movie.CONTENT_URI),
                Movie.CONTENT_DIR_TYPE);
        assertEquals(
                r.getType(ContentUris.withAppendedId(Movie.CONTENT_URI, 1)),
                Movie.CONTENT_ITEM_TYPE);
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with one entry.
     */
    public void testInsertOne() {
        ContentValues values = createDummyDataOneMovie("Pulp Fiction", 5);

        // Insert the values.
        Uri uri = mContext.getContentResolver().insert(
                Movie.CONTENT_URI, values);

        // Checks that there is one value in the database
        assertResultCount(Movie.CONTENT_URI, 1);

        // Assert the correct value stored in the database.
        assertCorrectStoredValues(uri, values);
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with a null entry.
     */
    public void testInsertNull() {
        // Insert the values.
        try {
            mContext.getContentResolver().insert(
                    Movie.CONTENT_URI, null);
            fail("Insert with null should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case.
        }
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with an invalid rating.
     */
    public void testInsertInvalidRating() {
        ContentValues values = new ContentValues();
        values.put(Movie.TITLE, "Pulp Fiction");
        values.put(Movie.RATING, 6);

        // Insert the values.
        try {
            mContext.getContentResolver().insert(
                    Movie.CONTENT_URI, values);
            fail("Insert with invalid number should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case.
        }
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with a missing rating.
     */
    public void testInsertNullRating() {
        // Note how we do not store a rating in values.
        ContentValues values = new ContentValues();
        values.put(Movie.TITLE, "Pulp Fiction");

        Uri uri = mContext.getContentResolver().insert(
                Movie.CONTENT_URI, values);

        // A failed insert will return null.
        assertNull("URI is not null, but it should be because no rating was given", uri);
    }

    /**
     * Tests {@link TomatilloProvider}'s insert method with a missing title.
     */
    public void testInsertTitleNull() {
        // Note how we do not store a title in values.
        ContentValues values = new ContentValues();
        values.put(Movie.RATING, 5);

        Uri uri = mContext.getContentResolver().insert(
                Movie.CONTENT_URI, values);

        // A failed insert will return null.
        assertNull("URI is not null, but it should be because no title was given", uri);
    }


    /**
     * Tests {@link TomatilloProvider}'s bulk insert method.
     */
    public void testBulkInsert() {
        ContentValues[] values = createDummyDataArray();

        mContext.getContentResolver().bulkInsert(Movie.CONTENT_URI, values);

        // Create a cursor containing only the ids
        Cursor cursor = mContext.getContentResolver().query(
                Movie.CONTENT_URI, new String[] { Movie._ID }, null, null, null);

        try {
            assertEquals(cursor.getCount(), 2);
            cursor.moveToFirst();
            int i = 1;
            while (cursor.moveToNext()) {
                assertCorrectStoredValues(
                        ContentUris.withAppendedId(Movie.CONTENT_URI, cursor.getLong(0)),
                        values[i]);
                i--;
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Tests {@link TomatilloProvider}'s delete method by
     * deleting the last entry in the table.
     */
    public void testDeleteLastEntry() {
        ContentValues[] values = createDummyDataArray();
        insertDummyData(values);

        Cursor cursor1 = mContext.getContentResolver().query(
                Movie.CONTENT_URI,
                new String[] { Movie._ID, Movie.RATING },
                null, null, null);

        long id = -1;
        try {
            assertEquals(cursor1.getCount(), 2);
            cursor1.moveToLast();
            id = cursor1.getLong(0);
        } finally {
            cursor1.close();
        }

        int rowsDeleted = mContext.getContentResolver().delete(
                ContentUris.withAppendedId(Movie.CONTENT_URI, id),
                null, null);
        assertEquals(rowsDeleted, 1);

        //Make sure that the id was actually set
        assertNotSame(id, -1);
        assertResultCount(ContentUris.withAppendedId(Movie.CONTENT_URI, id),
                0);
    }

    /**
     * Tests {@link TomatilloProvider}'s delete method by deleting all records and the testing that
     * there are no records.
     */
    public void testDeleteAllRecords() {
        deleteAllRecords();
        assertResultCount(Movie.CONTENT_URI, 0);
    }

    /**
     * Tests {@link TomatilloProvider}'s update by changing one value in one row.
     */
    public void testUpdateOneEntry() {
        ContentValues[] values = createDummyDataArray();
        insertDummyData(values);

        Cursor cursor = mContext.getContentResolver().query(
                Movie.CONTENT_URI,
                new String[] { Movie._ID},
                null, null, null);
        long id = -1;
        try {
            assertEquals(cursor.getCount(), 2);
            if (cursor.moveToFirst()) {
                // Based off of the index of the projection, _ID is 0
                id = cursor.getLong(0);
            }
        } finally {
            cursor.close();
        }
        // Make sure the ID was set.
        assertNotSame(id, -1);
        ContentValues valuesUpdated = new ContentValues();
        valuesUpdated.put(Movie.RATING, 1);

        int rowsUpdated = mContext.getContentResolver().update(
                ContentUris.withAppendedId(Movie.CONTENT_URI, id),
                valuesUpdated, null, null);
        assertEquals(rowsUpdated, 1);
        assertCorrectStoredValues(ContentUris.withAppendedId(Movie.CONTENT_URI, id), valuesUpdated);
    }


    /**
     * Tests {@link TomatilloProvider}'s update by changing multiple entries.
     */
    public void testUpdateMultipleEntries() {
        ContentValues[] values = createDummyDataArray();
        insertDummyData(values);
        ContentValues valuesUpdated = new ContentValues();
        int updatedRating = 1;
        valuesUpdated.put(Movie.RATING, updatedRating);

        // Put the values in updatedRating into every rating field of Movies.
        int rowsUpdated = mContext.getContentResolver().update(
                Movie.CONTENT_URI, valuesUpdated, null, null);
        // Make sure the assert worked.
        assertEquals(rowsUpdated, values.length);

        Cursor cursor = mContext.getContentResolver().query(
                Movie.CONTENT_URI,
                new String[] { Movie._ID, Movie.RATING },
                null, null, null);

        try {
            cursor.moveToFirst();
            // Assert that every rating was updated to the value of updatedRating.
            while(cursor.moveToNext()) {
                int newMovies = cursor.getInt(1);
                assertEquals(newMovies, updatedRating);
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Tests {@link TomatilloProvider}'s update by trying to change an entry to an invalid value.
     */
    public void testUpdateInvalid() {
        ContentValues[] values = createDummyDataArray();
        insertDummyData(values);

        ContentValues value = new ContentValues();
        value.put(Movie.RATING, -4);

        // Put the invalid updated rating value in the database
        try {
            mContext.getContentResolver().update(
                    ContentUris.withAppendedId(Movie.CONTENT_URI, 0),
                    value, null, null);
            fail("Update with invalid rating should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The expected case.
        }
    }

    /**
     * Helper Methods are below
     */

    /**
     * Helper method to delete all of the record in the database.
     */
    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                Movie.CONTENT_URI,
                null,
                null);
    }

    /**
     * Helper method to create one row of data in the database to help perform further tests.
     */
    private ContentValues createDummyDataOneMovie(String title, int rating) {
        ContentValues values = new ContentValues();
        values.put(Movie.TITLE, title);
        values.put(Movie.RATING, rating);
        return values;
    }

    /**
     * Helper method to create data for rows in the database to help perform further tests.
     */
    private ContentValues[] createDummyDataArray() {
        ContentValues[] valuesArr = new ContentValues[2];
        valuesArr[0] = createDummyDataOneMovie("Pulp Fiction", 5);
        valuesArr[1] = createDummyDataOneMovie("Forrest Gump", 4);
        return valuesArr;
    }

    /**
     * Helper method to create data for rows in the database and insert to help perform
     * further tests.
     */
    private Uri[] insertDummyData(ContentValues[] values) {
        if (values == null) return null;
        Uri[] uris = new Uri[values.length];
        for(int i = 0; i < values.length; i++) {
            uris[i] = mContext.getContentResolver().insert(
                    Movie.CONTENT_URI, values[i]);
        }
        return uris;
    }

    /**
     * Helper method to test whether the number of objects returned in a query matches the
     * expected amount in the table.
     */
    private void assertResultCount(Uri uri, String[] projection, String selection,
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
                        Movie._ID, Movie.TITLE, Movie.RATING },
                null, null, null);
        try {
            // Assert there is only one entry.
            assertEquals("Row count " + cursor.getCount(), 1, cursor.getCount());

            //Move to the first and only row.
            cursor.moveToFirst();
            //If it contains the key, assert the value is the same.
            if (values.containsKey(Movie._ID)) {
                assertEquals(new Long(cursor.getLong(0)), values.getAsLong(Movie._ID));
            }
            if (values.containsKey(Movie.TITLE)) {
                assertEquals(cursor.getString(1), values.getAsString(Movie.TITLE));
            }
            if (values.containsKey(Movie.RATING)) {
                assertEquals(new Integer(cursor.getInt(2)), values.getAsInteger(Movie.RATING));
            }
        } finally {
            cursor.close();
        }
    }
}