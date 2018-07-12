package com.benny.app.dynamicview;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.benny.library.dynamicview.DynamicViewEngineImpl;
import com.benny.library.dynamicview.api.ActionProcessor;
import com.benny.library.dynamicview.api.HttpCacheProxy;
import com.danikula.videocache.HttpProxyCacheServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private ListView vContainer;
    private DynamicViewEngineImpl dynamicViewEngine = new DynamicViewEngineImpl();
    private HttpProxyCacheServer proxyCacheServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityPermissionsDispatcher.initViewWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initView() {
        //dynamicViewEngine.compileAll(new File(Environment.getExternalStorageDirectory(), "dynamicview"));
        proxyCacheServer = new HttpProxyCacheServer(this);
        dynamicViewEngine.setImageLoader(new GlideImageLoader());
        dynamicViewEngine.setHttpCacheProxy(new HttpCacheProxy() {
            @Override
            public String getProxyUrl(String url) {
                return proxyCacheServer.getProxyUrl(url);
            }
        });

        vContainer = findViewById(R.id.container);
        vContainer.setAdapter(new DynamicViewAdapter());
    }

    private void inflateViews() {
        String xml = "<RBox sn=\"12345678\"><Image src=\"{logo}\" width=\"100\" height=\"100\"/><Text text=\"{title}\" padding=\"30\" gravity=\"center|end\" background=\"red 10\"/></RBox>";
        Map<String, String> map = new HashMap<>();
        map.put("title", "Hello World");
        map.put("logo", "http://avatar.csdn.net/8/B/B/1_sinyu890807.jpg");
        try {
            dynamicViewEngine.compile(xml);
            for (int i = 0; i < 5; ++i) {
                View view = dynamicViewEngine.inflate(MainActivity.this, vContainer, xml);
                dynamicViewEngine.bindView(view, new JSONObject(map));
            }
        }
        catch (Exception ignored) {
        }
    }

    private void inflateAdapterViews() {
        String xml = "<Grid sn=\"123456789\" dataSource=\"{items}\"><RBox><Image src=\"{logo}\" width=\"100\" height=\"100\"/><Text text=\"{title}\" padding=\"30\" gravity=\"center|end\" background=\"red 10\"/></RBox></Grid>";
        try {
            dynamicViewEngine.compile(xml);

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
                View view = dynamicViewEngine.inflate(MainActivity.this, vContainer, xml);
                dynamicViewEngine.bindView(view, new JSONObject(map));
            }
        }
        catch (Exception ignored) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public class DynamicViewAdapter extends BaseAdapter {
        List<ViewDefinitions.ViewDefinition> viewDefinitions;

        public DynamicViewAdapter() {
            viewDefinitions = ViewDefinitions.getViews(MainActivity.this);
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
                convertView = dynamicViewEngine.inflate(MainActivity.this, null, viewDefinition.layout);
                dynamicViewEngine.setActionProcessor(convertView, new ActionProcessor() {
                    @Override
                    public void processAction(View view, String tag, JSONObject data) {
                        Toast.makeText(MainActivity.this, "target: " + view + " trigger action " + tag + " with data " + data, Toast.LENGTH_SHORT).show();
                        //if (tag.equals("image_click")) {
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        //}
                    }
                });
            }
            dynamicViewEngine.bindView(convertView, viewDefinition.data);
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
