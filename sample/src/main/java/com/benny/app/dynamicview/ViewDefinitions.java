package com.benny.app.dynamicview;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewDefinitions {

    public static List<ViewDefinition> getViews(Context context) {
        List<ViewDefinition> viewDefs = new ArrayList<>();

        String layout00001 = readAssetString(context, "00001.xml");
        viewDefs.add(new ViewDefinition(layout00001, getLayout00001Data(), 2));
        viewDefs.add(new ViewDefinition(layout00001, getLayout00001Data(), 2));
        viewDefs.add(new ViewDefinition(layout00001, getLayout00001Data(), 2));
        viewDefs.add(new ViewDefinition(layout00001, getLayout00001Data(), 2));
        viewDefs.add(new ViewDefinition(layout00001, getLayout00001Data(), 2));

        StringBuilder builder = new StringBuilder();
        builder.append("<Relative sn='000001' size='match'>");
        builder.append("<Linear orientation='vertical'  background='#80E0E0E0 20 20 0 0' padding='18 18 18 10' margin='14'>");
        builder.append("<Label onClick='(TITLE_CLICK)' fontSize='20' color='black' text='{$title.title}'>");
        builder.append("</Label>");
        builder.append("<Label margin='0 10 0 0' text='金额'/>");
        builder.append("<Relative><Label name='money' onClick='({money})' fontSize='28' color='{@ddd:red}' text='{$money}'/>");
        builder.append("<Label rightOf='@money' alignBaseline='@money' text='元' /></Relative>");
        builder.append("<List size='match wrap' orientation='horizontal' dividerHeight='1' dividerColor='red' dividerMargin='20 5 20 5' range='1' dataSource='{$items}'>");
        builder.append("<Linear size='0 wrap' weight='1' gravity='center' orientation='vertical'><Label size='match wrap' align='center' text='{$name}'/><Label color='black' background='red' align='center' text='{$value}'/></Linear>");
        builder.append("</List>");
        builder.append("<List size='match wrap' orientation='horizontal' dividerHeight='1' dividerColor='red' dividerMargin='20 5 20 5' range='1' dataSource='{$items}'>");
        builder.append("<Label size='0 wrap' weight='1' gravity='center' align='center' text='{$name}'/>");
        builder.append("</List>");
        builder.append("</Linear>");
        builder.append("</Relative>");
        viewDefs.add(new ViewDefinition(builder.toString(), getDealReminderData("2000.98", "20465.36元"), 0));
        viewDefs.add(new ViewDefinition(builder.toString(), getDealReminderData("3000.98", "30465.36元"), 0));
        viewDefs.add(new ViewDefinition(builder.toString(), getDealReminderData("4000.98", "40465.36元"), 0));

        builder = new StringBuilder();
        builder.append("<Relative sn='000002'>");
        builder.append("<Linear orientation='vertical'  background='#80E0E0E0 20 20 0 0' padding='18 18 18 10' margin='14' size='match wrap'>");
        builder.append("<Label onClick='(title_click)' color='black' text='{$title}' />");
        builder.append("<Label margin='0 10 0 0' text='还款金额' />");
        builder.append("<Linear orientation='horizontal' ><Label fontSize='28' color='black' text='{$money}' /><Label text='人民币'/></Linear>");
        builder.append("<Grid spanCount='2' range='1' dataSource='{$items}'>");
        builder.append("<Linear orientation='vertical' margin='0 10 0 0' onClick='(ITEM_CLICK)'><Label text='{$name}'/><Label color='black' text='{$value}'/></Linear>");
        builder.append("</Grid>");
        builder.append("</Linear>");
        builder.append("</Relative>");
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));
        viewDefs.add(new ViewDefinition(builder.toString(), getRepaymentData("2000.98", "20465.36元"), 1));

        String layout00002 = readAssetString(context, "00002.xml");
        viewDefs.add(new ViewDefinition(layout00002, getLayout00002Data(), 3));
        return viewDefs;
    }

    private static JSONObject getDealReminderData(String money, String remaining) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "交易提醒");
            data.put("money", money);

            JSONObject data2 = new JSONObject();
            data2.put("title", "交易提醒");
            data.put("title", data2);

            JSONArray items = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("name", "账号");
            item.put("value", "*9921");
            items.put(item);
            item = new JSONObject();
            item.put("name", "时间");
            item.put("value", new Date().toString());
            items.put(item);
            item = new JSONObject();
            item.put("name", "余额");
            item.put("value", remaining);
            items.put(item);
            item = new JSONObject();
            item.put("name", "类型");
            item.put("value", "支付平台网上支付");
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

    public static JSONObject getLayout00001Data() {
        try {
            JSONObject data = new JSONObject();
            JSONArray array = new JSONArray();

            JSONObject article = new JSONObject();
            article.put("original_link", "http://gz.feixin.10086.cn/Public/Uploads/user/7/1/00/0/1151587100/imgs/5aafa09fe6c04.png");
            article.put("title", "你与00后的距离只有年龄？其实还有…");
            article.put("main_text", "其实我们知道自己终将被Hello world Hello world但没有想到这一天会来得这么快。架空世界空案件四大发了加快立法不能玩了看好你的ladnfons");
            array.put(article);
            data.put("article", array);

            array = new JSONArray();
            JSONObject action = new JSONObject();
            action.put("title", "按钮1");
            array.put(action);
            action = new JSONObject();
            action.put("title", "按钮2");
            array.put(action);
            data.put("action", array);
            return data;
        }
        catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject getLayout00002Data() {
        try {
            JSONObject data = new JSONObject();
            JSONArray array = new JSONArray();

            JSONObject article = new JSONObject();
            article.put("original_link", "http://gz.feixin.10086.cn/Public/Uploads/user/7/1/00/0/1151587100/imgs/5aafa09fe6c04.png");
            article.put("title", "你与00后的距离只有年龄？其实还有…");
            array.put(article);
            article = new JSONObject();
            article.put("original_link", "http://gz.feixin.10086.cn/Public/Uploads/user/7/1/00/0/1151587100/imgs/5aaf9c5182889.jpg");
            article.put("title", "APP版本升级——新功能抢先看！");
            array.put(article);
            article = new JSONObject();
            article.put("original_link", "http://gz.feixin.10086.cn//Public/Uploads/user/7/1/00/0/1151587100/imgs/5aaf355068115.png");
            article.put("title", "你好,多图文");
            array.put(article);
            data.put("article", array);

            array = new JSONArray();
            JSONObject action = new JSONObject();
            action.put("title", "按钮1");
            array.put(action);
            action = new JSONObject();
            action.put("title", "按钮2");
            array.put(action);
            data.put("action", array);
            return data;
        }
        catch (JSONException e) {
            return null;
        }
    }

    private static String readAssetString(Context context, String filename) {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(filename);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            for (int n; (n = stream.read(buffer)) > 0; ) {
                output.write(buffer, 0, n);
            }
            return new String(output.toByteArray(), Charset.forName("UTF-8"));
        }
        catch (Exception e) {
            Log.e("IOUtils", "readAll close stream exception: " + Log.getStackTraceString(e));
            return null;
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            }
            catch (Exception e) {
                Log.e("IOUtils", "readAll close stream exception: " + Log.getStackTraceString(e));
            }
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
