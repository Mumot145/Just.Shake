package com.patrickmumot.eatingdice.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.telecom.Call;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.patrickmumot.eatingdice.API.GooglePlaces;
import com.patrickmumot.eatingdice.Elements.SwipeImageView;
import com.patrickmumot.eatingdice.Enum.detailType;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;
import com.patrickmumot.eatingdice.Interface.IPhotoInteraction;
import com.patrickmumot.eatingdice.Interface.IScrollListener;
import com.patrickmumot.eatingdice.Listener.ScrollGestureListener;
import com.patrickmumot.eatingdice.MainActivityRemake;
import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullViewPagerAdapter extends PagerAdapter {
    private IScrollListener scrollListener;
    private IPhotoInteraction photoInteraction;
    private List<GooglePhoto> IMAGES;
    private Cuisine cuisine;
    private LayoutInflater inflater;
    private Context context;
    private GestureDetector mGestureDetector;
    private boolean fullPage = false;



    public FullViewPagerAdapter(Context context) {
        this.context = context;

        if (context instanceof IScrollListener) {
            scrollListener = (IScrollListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IScrollListener");
        }

        inflater = LayoutInflater.from(context);
    }

    public void setImages(List<GooglePhoto> IMAGES){
        this.IMAGES=IMAGES;
    }


    public void setCuisine(Cuisine Cuisine){
        this.cuisine = Cuisine;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if(IMAGES != null)
            return IMAGES.size();

        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.full_photo_layout, view, false);

        assert imageLayout != null;
        final SwipeImageView imageView = (SwipeImageView) imageLayout
                .findViewById(R.id.image);
        final ProgressBar photoProgress = (ProgressBar) imageLayout
                .findViewById(R.id.photoProgress);
        final ConstraintLayout background = (ConstraintLayout) imageLayout
                .findViewById(R.id.background);

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });

        if(IMAGES.size() > 0){

            final ScrollGestureListener listener = new ScrollGestureListener();
            listener.setInfo(scrollListener, null, null, position);
            final GestureDetector scrollGestureDetector = new GestureDetector(context,listener);
            imageView.setmGestureDetector(scrollGestureDetector);
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.tappedBg();
                }
            });
            loadImage(builder, imageView, IMAGES.get(position), photoProgress);
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    private void loadImage(Picasso.Builder builder, ImageView imageView, GooglePhoto photo, final ProgressBar photoProgress){
        Callback cb = new Callback() {
            @Override
            public void onSuccess() {
                photoProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        };

        builder.build()
            .load(GooglePlaces.CreatePhotoUrl(photo)).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .into(imageView, cb);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}