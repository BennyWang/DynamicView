package com.benny.library.dynamicview.view;

import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.parser.property.NodeProperties;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewBinder {
    private List<Animation> animations = new ArrayList<>();
    private List<Pair<DynamicViewBuilder, NodeProperties>> pairs = new ArrayList<>();
    private ActionProcessorWrapper actionProcessorWrapper = new ActionProcessorWrapper();

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public void add(DynamicViewBuilder builder, NodeProperties properties) {
        pairs.add(Pair.create(builder, properties));
    }

    public void bind(JSONObject data) {
        for (Pair<DynamicViewBuilder, NodeProperties> pair : pairs) {
            pair.second.set(pair.first, actionProcessorWrapper, data);
        }

        for (Animation animation : animations) {
            animation.start();
        }
    }

    public void setActionProcessor(ActionProcessor processor) {
        actionProcessorWrapper.setProcessor(processor);
    }

    public ActionProcessor getActionProcessor() {
        return actionProcessorWrapper;
    }

    private static class ActionProcessorWrapper implements ActionProcessor {
        private ActionProcessor processor;

        public void setProcessor(ActionProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void processAction(View view, String tag, JSONObject data) {
            if (processor != null) {
                processor.processAction(view, tag, data);
            }
        }
    }
}
