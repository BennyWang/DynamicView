package com.benny.app.dynamicview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.benny.library.dynamicview.DynamicViewEngine;

import java.util.HashMap;
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
        vContainer = findViewById(R.id.container);
        vContainer.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {
        private String layoutXml = "<TitleView sn=\"12345678\" title=\"{title}\" logo=\"{logo}\" >";
        private Map<String, String> props = new HashMap<>();

        public MyAdapter() {
            props.put("logo", "http://avatar.csdn.net/8/B/B/1_sinyu890807.jpg");
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Map<String, String> getItem(int position) {
            props.put("title", "Title " + position);
            return props;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = DynamicViewEngine.getInstance().createView(MainActivity.this, layoutXml);
            }
            DynamicViewEngine.getInstance().bindView(convertView, getItem(position));
            return convertView;
        }
    }
}
