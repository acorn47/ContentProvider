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

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This defines the table and columns for the movies table as well as associated
 * URIs, and MIME types.
 */
public class TomatilloContract {
    /**
     * The String storing the content authority for the URI and database
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.rottentomatillos.provider";

    /**
     * The base URI to build off of which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Movie implements BaseColumns{
        /**
         * Name of the Movie table.
         */
        public static final String TABLE_NAME = "movie";

        /**
         * Title of the move.
         * <P>Type: TEXT</P>
         */
        public static final String TITLE = "title";

        /**
         * A rating between 0 and 5 for the Movie.
         * <P>Type: INTEGER</P>
         */
        public static final String RATING = "rating";

        /**
         * Base Uri for the Movie table.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        /**
         * The MIME type for a list of movie ratings.
         */
        public static final String CONTENT_DIR_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        /**
         * The MIME type for a single movie rating.
         */
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
}
