package com.patrickmumot.eatingdice.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.patrickmumot.eatingdice.Models.Location;
import com.patrickmumot.eatingdice.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private IScrollListener scrollListener;
    private IPhotoInteraction photoInteraction;
    private List<GooglePhoto> IMAGES;
    private Cuisine cuisine;
    private LayoutInflater inflater;
    private Context context;
    private GestureDetector mGestureDetector;



    public ViewPagerAdapter(Context context) {
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
        View imageLayout = inflater.inflate(R.layout.photo_layout, view, false);

        assert imageLayout != null;
        final SwipeImageView imageView = (SwipeImageView) imageLayout
                .findViewById(R.id.image);
        final ProgressBar photoProgress = (ProgressBar) imageLayout
                .findViewById(R.id.photoProgress);

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

            Log.i("slider", position+"");

            ScrollGestureListener listener = new ScrollGestureListener();

            listener.setInfo(scrollListener, IMAGES.get(position), detailType.Review, position);
            final GestureDetector scrollGestureDetector = new GestureDetector(context,listener);
            imageView.setmGestureDetector(scrollGestureDetector);

            builder.build()
                    .load(GooglePlaces.CreatePhotoUrl(IMAGES.get(position))).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).resize(MainActivityRemake.screenWidth, MainActivityRemake.restaurantImageHeight)
                    .centerCrop().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    photoProgress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        }

        view.addView(imageLayout, 0);

        return imageLayout;
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