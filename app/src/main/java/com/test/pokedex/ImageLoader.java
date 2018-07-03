package com.test.pokedex;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageLoader {

    public static final int GLIDE = 1;
    public static final int PICASSO = 2;
    public static final int IMAGE_LOADER = GLIDE;


    @Inject
    Picasso picasso;

    @Inject
    Context context;

    @Inject
    public ImageLoader(Picasso picasso) {
        this.picasso = picasso;
    }

    public void loadImage(@NonNull String imageUrl, @NonNull ImageView imageView, final LottieAnimationView imageLoadingAnimationView) {
        switch (IMAGE_LOADER) {
            case GLIDE:
                loadImageUsingGlide(imageUrl, imageView, imageLoadingAnimationView);
                break;
            case PICASSO:
                loadImageUsingPicasso(imageUrl, imageView, imageLoadingAnimationView);
                break;
        }

    }

    public void loadImageUsingPicasso(@NonNull String imageUrl, @NonNull ImageView imageView, final LottieAnimationView imageLoadingAnimationView) {

        if (imageLoadingAnimationView != null) {
            com.squareup.picasso.Callback callback = new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    imageLoadingAnimationView.cancelAnimation();
                    imageLoadingAnimationView.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            };
            picasso.load(imageUrl)
                    .into(imageView, callback);
        }
        else {
            picasso.load(imageUrl)
                    .into(imageView);
        }

    }

    public void loadImageUsingGlide(@NonNull String imageUrl, @NonNull ImageView imageView, final LottieAnimationView imageLoadingAnimationView) {
        if (imageLoadingAnimationView != null) {
            RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imageLoadingAnimationView.cancelAnimation();
                    imageLoadingAnimationView.setVisibility(View.GONE);
                    return false;
                }
            };

            Glide.with(context)
                    .load(imageUrl)
                    .listener(requestListener)
                    .transition(withCrossFade())
                    .into(imageView);
        }
        else {
            Glide.with(context)
                    .load(imageUrl)
                    .transition(withCrossFade())
                    .into(imageView);
        }

    }
}
