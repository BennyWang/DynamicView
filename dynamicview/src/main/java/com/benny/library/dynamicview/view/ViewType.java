package com.benny.library.dynamicview.view;

import org.json.JSONArray;

public class ViewType {
    public interface View {
    }

    public interface GroupView {
    }

    public interface AdapterView {
        void setDataSource(JSONArray source);
        void setInflater(ViewInflater inflater);
    }
}
