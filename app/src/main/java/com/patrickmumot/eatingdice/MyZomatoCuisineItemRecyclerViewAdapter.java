package com.patrickmumot.eatingdice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.ZomatoCuisineItemFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Cuisine} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyZomatoCuisineItemRecyclerViewAdapter extends RecyclerView.Adapter<MyZomatoCuisineItemRecyclerViewAdapter.ViewHolder> {

    private final List<Cuisine> mValues;
    private boolean anySelected = false;
    private final OnListFragmentInteractionListener mListener;
    private Context thisContext;
    public MyZomatoCuisineItemRecyclerViewAdapter(List<Cuisine> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        thisContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_zomatocuisineitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(thisContext).load(holder.mItem.getCuisineResId()).into(holder.mImageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        if(holder.mItem.getLookFor()){
            holder.mFrameLayout.setForeground(ResourcesCompat.getDrawable(thisContext.getResources(),R.drawable.gradient_main, null));
            anySelected = true;
        } else {
            holder.mFrameLayout.setForeground(ResourcesCompat.getDrawable(thisContext.getResources(),R.drawable.gradient_bg_opposite, null));
        }

        holder.mContentView.setText(holder.mItem.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public final FrameLayout mFrameLayout;
        public final ProgressBar mProgressBar;
       // public final ImageView mIcon;
        public Cuisine mItem;
        Typeface custom_font = Typeface.createFromAsset(thisContext.getAssets(),  "fonts/SourceSansPro-SemiBold.ttf");
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.cuisineImage);
            mContentView = (TextView) view.findViewById(R.id.content);
            mContentView.setTypeface(custom_font);
            mFrameLayout = (FrameLayout) view.findViewById(R.id.displayFrame);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarCuisine);
           // mIcon = (ImageView) view.findViewById(R.id.foodIcon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
