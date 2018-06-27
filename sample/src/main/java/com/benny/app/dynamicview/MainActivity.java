package com.benny.app.dynamicview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.benny.library.dynamicview.DynamicViewEngine;
import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.widget.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView vContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Image.setImageLoader(new GlideImageLoader());
        vContainer = findViewById(R.id.container);

        vContainer.setAdapter(new DynamicViewAdapter());
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

    public class DynamicViewAdapter extends BaseAdapter {
        List<ViewDefinitions.ViewDefinition> viewDefinitions;

        public DynamicViewAdapter() {
            viewDefinitions = ViewDefinitions.getViews();
        }

        @Override
        public int getCount() {
            return viewDefinitions.size();
        }

        @Override
        public ViewDefinitions.ViewDefinition getItem(int position) {
            return viewDefinitions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewDefinitions.ViewDefinition viewDefinition = getItem(position);
            if (convertView == null) {
                convertView = DynamicViewEngine.getInstance().inflate(MainActivity.this, null, viewDefinition.layout);
                DynamicViewEngine.setActionProcessor(convertView, new ActionProcessor() {
                    @Override
                    public void processAction(View view, String tag, JSONObject data) {
                        Toast.makeText(MainActivity.this, "target: " + view + " trigger action " + tag + " with data " + data, Toast.LENGTH_SHORT).show();
                        if (tag.equals("image_click")) {
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    }
                });
            }
            DynamicViewEngine.bindView(convertView, viewDefinition.data);
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 10;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).viewType;
        }
    }
}
