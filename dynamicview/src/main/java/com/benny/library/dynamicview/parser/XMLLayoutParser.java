package com.benny.library.dynamicview.parser;

import android.text.TextUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.benny.library.dynamicview.parser.node.DynamicNodeFactory;
import com.benny.library.dynamicview.parser.node.DynamicViewNode;
import com.benny.library.dynamicview.property.DynamicProperties;

import java.io.StringReader;

public class XMLLayoutParser {
    private XmlPullParser parser;
    private SerialNumberHandler serialNumberHandler;

    public XMLLayoutParser(SerialNumberHandler serialNumberHandler) {
        try {
            this.serialNumberHandler = serialNumberHandler;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            parser = factory.newPullParser();
        }
        catch (Exception ignored) {
        }
    }

    public synchronized DynamicViewTree parseDocument(String xml) throws Exception {
        parser.setInput(new StringReader(xml));
        DynamicViewNode currentNode = null;
        for (int event; (event = parser.getEventType()) != XmlPullParser.END_DOCUMENT; ) {
            if (event == XmlPullParser.START_TAG) {
                DynamicViewNode viewNode = parseNode(parser);
                if (currentNode != null) {
                    currentNode.addChild(viewNode);
                }
                currentNode = viewNode;

                if (viewNode.isRoot()) {
                    String serialNumber = viewNode.getProperty("sn");
                    if (TextUtils.isEmpty(serialNumber)) {
                        throw new XmlPullParserException("sn does not defined in root node");
                    }
                    DynamicViewTree viewTree = serialNumberHandler.onReceive(serialNumber);
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
        return new DynamicViewTree(currentNode);
    }

    private DynamicViewNode parseNode(XmlPullParser parser) throws Exception {
        String className = parser.getName();
        DynamicProperties properties = parseAttributes(parser);
        return DynamicNodeFactory.create(className, properties);
    }

    private DynamicProperties parseAttributes(XmlPullParser parser) {
        DynamicProperties properties = new DynamicProperties();
        int attrCount = parser.getAttributeCount();
        for (int i = 0; i < attrCount; ++i) {
            properties.add(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
        return properties;
    }

    public interface SerialNumberHandler {
        DynamicViewTree onReceive(String serialNumber);
    }
}
