package com.benny.library.dynamicview.property;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewIdGenerator {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private Map<String, String> idMap = new HashMap<>();

    public String getId(String name) {
        if (idMap.containsKey(name)) {
            return idMap.get(name);
        }

        String id = String.valueOf(generateViewId());
        idMap.put(name, id);
        return id;
    }

    public boolean contains(String name) {
        return idMap.containsKey(name);
    }

    private static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
