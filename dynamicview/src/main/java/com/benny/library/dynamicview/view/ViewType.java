package com.benny.library.dynamicview.view;

public class ViewType {
    public interface View {
    }

    public interface GroupView {
        void addView(android.view.View view);
    }

    public interface AdapterView {
        void setDataSource(String source);
        void setViewCreator(ViewCreator viewCreator);
    }
}
