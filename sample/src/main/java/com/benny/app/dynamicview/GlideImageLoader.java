package com.benny.app.dynamicview;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.benny.library.dynamicview.widget.Image;
import com.bumptech.glide.Glide;

import java.io.File;

public class GlideImageLoader implements Image.ImageLoader {
    private static String basePath;

    public static void setBasePath(String path) {
        basePath = path;
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
    }


    @Override
    public void loadImage(String src, ImageView view) {
        if (basePath == null) {
            basePath = view.getContext().getDir("images", Context.MODE_PRIVATE).toString();
        }

        Uri uri = parseSource(src);
        try {
            Glide.with(view.getContext()).load(uri).into(view);
        }
        catch (Error ignored) {
        }
    }

    private Uri parseSource(String src) {
        if (src.startsWith("http")) {
            return Uri.parse(src);
        }

        if (!src.startsWith("/")) {
            src = basePath + src;
        }

        return Uri.fromFile(new File(src));
    }
}
