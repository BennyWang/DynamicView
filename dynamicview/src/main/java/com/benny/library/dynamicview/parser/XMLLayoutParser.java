package com.benny.library.dynamicview.parser;

import android.text.TextUtils;
import android.util.Log;

import com.benny.library.dynamicview.parser.expression.IllegalExprException;
import com.benny.library.dynamicview.parser.node.DynamicNode;
import com.benny.library.dynamicview.parser.node.DynamicNodeFactory;
import com.benny.library.dynamicview.parser.node.DynamicViewNode;
import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.util.ViewIdGenerator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;

public class XMLLayoutParser {
    private XmlPullParserFactory parserFactory;
    private SerialNumberHandler serialNumberHandler;

    public XMLLayoutParser(SerialNumberHandler serialNumberHandler) {
        try {
            this.serialNumberHandler = serialNumberHandler;
            parserFactory = XmlPullParserFactory.newInstance();
            parserFactory.setNamespaceAware(false);
        }
        catch (Exception ignored) {
        }
    }

    public synchronized DynamicViewTree parseDocument(File layoutFile) throws Exception {
        XmlPullParser parser = parserFactory.newPullParser();
        parser.setInput(new FileInputStream(layoutFile), "UTF-8");
        return parse(parser);
    }

    public synchronized DynamicViewTree parseDocument(String xml) throws Exception {
        XmlPullParser parser = parserFactory.newPullParser();
        parser.setInput(new StringReader(xml));
        return parse(parser);
    }

    private DynamicViewTree parse(XmlPullParser parser) throws Exception {
        long tick = System.currentTimeMillis();
        ViewIdGenerator viewIdGenerator = new ViewIdGenerator();
        DynamicNode currentNode = null;
        for (int event; (event = parser.getEventType()) != XmlPullParser.END_DOCUMENT; ) {
            if (event == XmlPullParser.START_TAG) {
                DynamicNode viewNode = parseNode(parser, viewIdGenerator);
                if (currentNode != null) {
                    currentNode.addChild(viewNode);
                }
                currentNode = viewNode;

                if (viewNode.isRoot()) {
                    String serialNumber = viewNode.getProperty("sn");
                    long version = viewNode.getLongProperty("version", 0);
                    if (TextUtils.isEmpty(serialNumber)) {
                        throw new XmlPullParserException("sn does not defined in root node");
                    }
                    DynamicViewTree viewTree = serialNumberHandler.onReceive(serialNumber, version);
                    if (viewTree != null) {
                        return viewTree;
                    }
                }
            }
            else if (event == XmlPullParser.END_TAG) {
                if (currentNode != null && !currentNode.isRoot()) {
                    currentNode = currentNode.getParent();
                }
            }
            parser.next();
        }
        Log.i("DynamicViewEngine", "parse " + currentNode.getProperty("sn") + " cost " + (System.currentTimeMillis() - tick));
        return new DynamicViewTree((DynamicViewNode) currentNode);
    }

    private DynamicNode parseNode(XmlPullParser parser, ViewIdGenerator viewIdGenerator) throws Exception {
        String className = parser.getName();
        NodeProperties properties = parseAttributes(parser, viewIdGenerator);
        return DynamicNodeFactory.create(className, properties);
    }

    private NodeProperties parseAttributes(XmlPullParser parser, ViewIdGenerator viewIdGenerator) throws IllegalExprException {
        NodeProperties properties = new NodeProperties(viewIdGenerator);
        int attrCount = parser.getAttributeCount();
        for (int i = 0; i < attrCount; ++i) {
            properties.add(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
        return properties;
    }

    public interface SerialNumberHandler {
        DynamicViewTree onReceive(String serialNumber, long version);
    }
}
