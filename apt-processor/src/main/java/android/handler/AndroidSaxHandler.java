package android.handler;

import android.Constant.AndroidConstant;
import android.StringUtil;
import android.androidViewTag.AndroidView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static android.Constant.AndroidConstant.ANDROID_ID;

public class AndroidSaxHandler extends DefaultHandler {

    private List<AndroidView> list = new ArrayList<>();


    public AndroidSaxHandler() {
        super();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (attributes!=null){
            int idIndex = attributes.getIndex(ANDROID_ID);
            if (idIndex>-1){
                AndroidView androidView = new AndroidView();
                androidView.id = attributes.getValue(idIndex).split("/")[1];
                System.err.println(qName);
                androidView.ViewName = "m"+ StringUtil.firstChar2UperCase(androidView.id);
                if (qName.contains(".")){
                    String[] attrs = qName.split("\\.");
                    androidView.className = attrs[attrs.length-1];//TextView
                    androidView.packageName = qName.substring(0,qName.lastIndexOf("."));//com.android
                }else {
                    androidView.className =qName;
                    if (AndroidConstant.Android_ViewClss_View.equals(qName)){
                        androidView.packageName = AndroidConstant.Android_View_Package;
                    }else {
                        androidView.packageName = AndroidConstant.Android_Widget_Package;
                    }
                }
                list.add(androidView);
            }
        }
    }

    public List<AndroidView> getList() {
        return list;
    }
}
