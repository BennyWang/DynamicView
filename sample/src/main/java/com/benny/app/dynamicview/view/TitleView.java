package com.benny.app.dynamicview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benny.app.dynamicview.R;
import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;
import com.bumptech.glide.Glide;

@DynamicView
public class TitleView extends RelativeLayout implements ViewType.View {
    private ImageView vLogo;
    private TextView vTitle;

    public TitleView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.widget_title_view, this);

        vLogo = findViewById(R.id.logo);
        vTitle = findViewById(R.id.title);
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }

    public void setLogo(String logo) {
        Glide.with(getContext()).load(logo).into(vLogo);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
