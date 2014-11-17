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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.example.com.rottentomatillos.data.TomatilloContract.Movie;
import android.example.com.rottentomatillos.data.TomatilloProvider;

/**
 * This is a custom adapter for creating a list view from a cursor. The list view
 * contains clickable {@link RatingBar}s that uses a user can change by
 * selecting a new number of stars. This works in conjuncture with a {@link TomatilloProvider}
 * to display movie ratings.
 */
public class RatingAdapter extends CursorAdapter {
    private Context mContext;

    /**
     * An implementation of the view holder pattern, which caches views so that one does not need
     * to transverse the entire view tree to find a view.
     */
    public static class ViewHolder {
        public final RatingBar ratingBar;
        public final TextView titleView;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            titleView = (TextView) view.findViewById(R.id.movie_name);
        }
    }

    private static final String LOG_TAG = RatingAdapter.class.getSimpleName();
    private static int sLoaderID;


    public RatingAdapter(Context context, Cursor c, int flags, int loaderID) {
        super(context, c, flags);
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_view_item_rating;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        // Hack to get orange stars.
        Context c = viewHolder.ratingBar.getContext();

        LayerDrawable stars = (LayerDrawable) viewHolder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(c.getResources().getColor(R.color.rt_orange),
                PorterDuff.Mode.SRC_ATOP);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read title from cursor
        int titleIndex = cursor.getColumnIndex(Movie.TITLE);
        final String movieTitle = cursor.getString(titleIndex);
        viewHolder.titleView.setText(movieTitle);

        int ratingIndex = cursor.getColumnIndex(Movie.RATING);
        int rating = cursor.getInt(ratingIndex);
        float ratingDisplay = (float)(Math.max(1, Math.min(rating, 5)));
        // Show a number of stars equal to what was returned from the cursor for the movie.
        viewHolder.ratingBar.setRating(ratingDisplay);

        int idIndex = cursor.getColumnIndex(Movie._ID);
        long id = cursor.getLong(idIndex);

        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingClickListener(id));
    }

    protected class RatingClickListener implements RatingBar.OnRatingBarChangeListener {
        private long mID;
        public RatingClickListener(long id) {
            super();
            mID = id;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            // If the user changed the rating, update the rating in the ContentProvider.
            if (fromUser) {
                long roundedRating = Math.max(Math.min(Math.round(rating),5),0);
                ratingBar.setRating(roundedRating);

                ContentValues values = new ContentValues();
                values.put(Movie.RATING, roundedRating);

                mContext.getContentResolver().update(
                        ContentUris.withAppendedId(Movie.CONTENT_URI,
                                mID),
                        values,null,null);
                // Tell the loader to reload the data in the cursor adapter.
                ((MainActivity)mContext).getSupportLoaderManager().restartLoader(
                        sLoaderID, null, (MainActivity)mContext);
            }
        }
    }
}
