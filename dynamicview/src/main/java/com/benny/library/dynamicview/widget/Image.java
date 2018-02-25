package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.widget.ImageView;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;

import java.util.HashMap;
import java.util.Map;

@DynamicView
public class Image extends ImageView implements ViewType.View {
    private static Map<String, ScaleType> SCALE_TYPES = new HashMap<>();
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

    public void setScale(String scale) {
        if (SCALE_TYPES.containsKey(scale)) {
            setScaleType(SCALE_TYPES.get(scale));
        }
    }

    public interface ImageLoader {
        void loadImage(String src, ImageView view);
    }

    static {
        SCALE_TYPES.put("stretch", ScaleType.FIT_XY);
        SCALE_TYPES.put("fitStart", ScaleType.FIT_START);
        SCALE_TYPES.put("fitEnd", ScaleType.FIT_END);
        SCALE_TYPES.put("center", ScaleType.CENTER);
        SCALE_TYPES.put("centerCrop", ScaleType.CENTER_CROP);
        SCALE_TYPES.put("centerInside", ScaleType.CENTER_INSIDE);
    }
}
