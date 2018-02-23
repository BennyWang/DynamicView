package com.benny.app.dynamicview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.benny.library.dynamicview.DynamicViewEngine;
import com.benny.library.dynamicview.widget.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private LinearLayout vContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Image.setImageLoader(new GlideImageLoader());
        vContainer = findViewById(R.id.container);

        inflateViews();
        inflateAdapterViews();
    }

    private void inflateViews() {
        String xml = "<RBox sn=\"12345678\"><Image src=\"{logo}\" width=\"100\" height=\"100\"/><Text text=\"{title}\" padding=\"30\" gravity=\"center|end\" background=\"red 10\"/></RBox>";
        Map<String, String> map = new HashMap<>();
        map.put("title", "Hello World");
        map.put("logo", "http://avatar.csdn.net/8/B/B/1_sinyu890807.jpg");
        try {
            DynamicViewEngine.getInstance().compile(xml);
            for (int i = 0; i < 5; ++i) {
                View view = DynamicViewEngine.getInstance().inflate(MainActivity.this, vContainer, xml);
                DynamicViewEngine.bindView(view, map);
            }
        }
        catch (Exception ignored) {
        }
    }

    private void inflateAdapterViews() {
        String xml = "<Grid sn=\"123456789\" dataSource=\"{items}\"><RBox><Image src=\"{logo}\" width=\"100\" height=\"100\"/><Text text=\"{title}\" padding=\"30\" gravity=\"center|end\" background=\"red 10\"/></RBox></Grid>";
        try {
            DynamicViewEngine.getInstance().compile(xml);

            Map<String, String> map = new HashMap<>();
            JSONArray items = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("logo", "http://avatar.csdn.net/8/B/B/1_sinyu890807.jpg");
            item.put("title", "Title 0");
            items.put(item);
            item = new JSONObject();
            item.put("logo", "http://avatar.csdn.net/8/B/B/1_sinyu890807.jpg");
            item.put("title", "Title 1");
            items.put(item);
            map.put("items", items.toString());

            for (int i = 0; i < 5; ++i) {
                View view = DynamicViewEngine.getInstance().inflate(MainActivity.this, vContainer, xml);
                DynamicViewEngine.bindView(view, map);
            }
        }
        catch (Exception ignored) {
        }
    }
}
