package com.benny.library.dynamicview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

public class LifeCycleCallbacksManager {
    private static boolean initialized;
    private static LinkedList<WeakReference<LifecycleCallbacks> > callbacks = new LinkedList<>();

    public static void initialize(Context context) {
        if (!initialized) {
            initialized = true;
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new LifecycleCallbacksWrapper());
            callbacks.offer(new WeakReference<LifecycleCallbacks>(LifecycleCacheManager.getInstance()));
        }
    }

    public static void registerCallbacks(LifecycleCallbacks callback) {
        callbacks.offerFirst(new WeakReference<>(callback));
    }

    private static class LifecycleCallbacksWrapper extends LifecycleCallbacks {
        @Override
        public void onActivityPaused(final Activity activity) {
            forEach(new LifecycleCallbacksWrapper.Function<LifecycleCallbacks, Void>() {
                @Override
                public Void call(LifecycleCallbacks lifecycleCallbacks) {
                    lifecycleCallbacks.onActivityPaused(activity);
                    return null;
                }
            });
        }

        @Override
        public void onActivityResumed(final Activity activity) {
            forEach(new LifecycleCallbacksWrapper.Function<LifecycleCallbacks, Void>() {
                @Override
                public Void call(LifecycleCallbacks lifecycleCallbacks) {
                    lifecycleCallbacks.onActivityResumed(activity);
                    return null;
                }
            });
        }

        @Override
        public void onActivityStarted(final Activity activity) {
            forEach(new LifecycleCallbacksWrapper.Function<LifecycleCallbacks, Void>() {
                @Override
                public Void call(LifecycleCallbacks lifecycleCallbacks) {
                    lifecycleCallbacks.onActivityStarted(activity);
                    return null;
                }
            });
        }

        @Override
        public void onActivityStopped(final Activity activity) {
            forEach(new LifecycleCallbacksWrapper.Function<LifecycleCallbacks, Void>() {
                @Override
                public Void call(LifecycleCallbacks lifecycleCallbacks) {
                    lifecycleCallbacks.onActivityStopped(activity);
                    return null;
                }
            });
        }

        @Override
        public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
            forEach(new LifecycleCallbacksWrapper.Function<LifecycleCallbacks, Void>() {
                @Override
                public Void call(LifecycleCallbacks lifecycleCallbacks) {
                    lifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
                    return null;
                }
            });
        }

        @Override
        public void onActivityDestroyed(final Activity activity) {
            forEach(new LifecycleCallbacksWrapper.Function<LifecycleCallbacks, Void>() {
                @Override
                public Void call(LifecycleCallbacks lifecycleCallbacks) {
                    lifecycleCallbacks.onActivityDestroyed(activity);
                    return null;
                }
            });
        }

        private void forEach(Function<LifecycleCallbacks, Void> action) {
            Iterator<WeakReference<LifecycleCallbacks>> iterator = callbacks.iterator();
            while (iterator.hasNext()) {
                WeakReference<LifecycleCallbacks> reference = iterator.next();
                LifecycleCallbacks callback = reference.get();
                if (callback != null) {
                    action.call(callback);
                }
                else {
                    iterator.remove();
                }
            }
        }

        interface Function<T, R> {
            R call(T t);
        }
    }
}
