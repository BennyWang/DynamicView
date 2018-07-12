package com.benny.library.dynamicview;

import com.benny.library.dynamicview.api.HttpCacheProxy;

public class HttpCacheProxyManager {
    private static HttpCacheProxy cacheProxy;

    public static void setProxy(HttpCacheProxy proxy) {
        cacheProxy = proxy;
    }

    public static String getProxyUrl(String url) {
        if (cacheProxy != null) {
            return cacheProxy.getProxyUrl(url);
        }
        return url;
    }
}
