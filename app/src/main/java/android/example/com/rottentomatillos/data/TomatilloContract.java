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

import android.provider.BaseColumns;

/**
 * This defines the table and columns for the movies table as well as associated
 * URIs, and MIME types.
 */
public class TomatilloContract {
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

    }
}
