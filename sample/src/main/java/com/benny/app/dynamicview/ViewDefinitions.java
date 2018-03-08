package com.benny.app.dynamicview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ViewDefinitions {

    public static List<ViewDefinition> getViews() {
        List<ViewDefinition> viewDefs = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append("<RBox sn='000001'>");
        builder.append("<VBox background='#80E0E0E0 20 20 0 0' padding='18 18 18 10' margin='14'>");
        builder.append("<Text text='{title}' onClick='(TITLE_CLICK)' fontSize='20' color='black'/>");
        builder.append("<Text text='金额' margin='0 10 0 0'/>");
        builder.append("<RBox><Text name='money' text='{money}' onClick='({money})'fontSize='28' color='black'/><Text rightOf='@money' alignBaseline='@money' text='元'/></RBox>");
        builder.append("<Grid spanCount='2' dataSource='{items}'>");
        builder.append("<VBox margin='0 10 0 0'><Text text='{name}'/><Text text='{value}' color='black'/></VBox>");
        builder.append("</Grid>");
        builder.append("</VBox>");
        builder.append("</RBox>");
        viewDefs.add(new ViewDefinition(builder.toString(), getDealReminderData("2000.98", "20465.36元"), 0));
        viewDefs.add(new ViewDefinition(builder.toString(), getDealReminderData("3000.98", "30465.36元"), 0));
        viewDefs.add(new ViewDefinition(builder.toString(), getDealReminderData("4000.98", "40465.36元"), 0));

        builder = new StringBuilder();
        builder.append("<RBox sn='000002'>");
        builder.append("<VBox background='#80E0E0E0 20 20 0 0' padding='18 18 18 10' margin='14' size='match wrap'>");
        builder.append("<Text text='{title}' onClick='({title})' color='black'/>");
        builder.append("<Text text='还款金额' margin='0 10 0 0'/>");
        builder.append("<HBox><Text text='{money}' fontSize='28' color='black'/><Text text='人民币'/></HBox>");
        builder.append("<Grid spanCount='2' dataSource='{items}'>");
        builder.append("<VBox margin='0 10 0 0'><Text text='{name}'/><Text text='{value}' color='black'/></VBox>");
        builder.append("</Grid>");
        builder.append("</VBox>");
        builder.append("</RBox>");
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));

        builder = new StringBuilder();
        builder.append("<Image sn='000003' size='100 100' src='res://ic_launcher' scale='fitCenter'/>");
        viewDefs.add(new ViewDefinition(builder.toString(), new JSONObject(), 2));
        return viewDefs;
    }

    private static JSONObject getDealReminderData(String money, String remaining) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "交易提醒");
            data.put("money", money);

            JSONArray items = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("name", "账号");
            item.put("value", "*9921");
            items.put(item);
            item = new JSONObject();
            item.put("name", "类型");
            item.put("value", "支付平台网上支付");
            items.put(item);
            item = new JSONObject();
            item.put("name", "余额");
            item.put("value", remaining);
            items.put(item);
            item = new JSONObject();
            item.put("name", "时间");
            item.put("value", new Date().toString());
            items.put(item);
            data.put("items", items);
            return data;
        }
        catch (JSONException e) {
            return null;
        }
    }

    private static JSONObject getRepaymentData(String money, String remaining) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "信用卡还款");
            data.put("money", money);

            JSONArray items = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("name", "账号");
            item.put("value", "*9921");
            items.put(item);
            item = new JSONObject();
            item.put("name", "交易类型");
            item.put("value", "还款");
            items.put(item);
            item = new JSONObject();
            item.put("name", "可用额度");
            item.put("value", remaining);
            item.put("colspan", 2);
            items.put(item);
            item = new JSONObject();
            item.put("name", "提示");
            item.put("value", "已还清本期账单");
            item.put("colspan", 2);
            items.put(item);
            data.put("items", items);
            return data;
        }
        catch (JSONException e) {
            return null;
        }
    }

    public static class ViewDefinition {
        public String layout;
        public JSONObject data;
        public int viewType;

        public ViewDefinition(String layout, JSONObject data, int viewType) {
            this.layout = layout;
            this.data = data;
            this.viewType = viewType;
        }
    }
}
