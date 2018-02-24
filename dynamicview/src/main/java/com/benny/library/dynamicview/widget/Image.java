package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.widget.ImageView;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class Image extends ImageView implements ViewType.View {
    private static ImageLoader imageLoader;

    public static void setImageLoader(ImageLoader loader) {
        imageLoader = loader;
    }

    public Image(Context context) {
        super(context);
    }

    public void setSrc(String src) {
        if (imageLoader != null) {
            imageLoader.loadImage(src, this);
        }
    }

    public interface ImageLoader {
        void loadImage(String src, ImageView view);
    }
}
