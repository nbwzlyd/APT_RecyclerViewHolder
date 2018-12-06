package android.Factory;

import android.Constant.AndroidConstant;
import android.Interface.IAndroidMethodHandler;
import android.Interface.ImageViewMethodHandler;
import android.Interface.TextViewMethodHandler;

import java.util.HashMap;
import java.util.Map;

public class AndroidMethodMapFactory {
    private Map<String, IAndroidMethodHandler> iAndroidPackageHandlerMap = new HashMap<>();

    public static AndroidMethodMapFactory getInstance(){
        return SingleInstance.factory;
    }

    private static class SingleInstance {
       public static AndroidMethodMapFactory factory = new AndroidMethodMapFactory();
    }

    private AndroidMethodMapFactory() {
        iAndroidPackageHandlerMap.put("TextView",
                new TextViewMethodHandler(AndroidConstant.Android_Widget_Package, "TextView"));
        iAndroidPackageHandlerMap.put("ImageView",
                new ImageViewMethodHandler(AndroidConstant.Android_Widget_Package, "ImageView"));

    }

    public Map<String, IAndroidMethodHandler> getPackageHandlerMap() {
        return iAndroidPackageHandlerMap;
    }

    public AndroidMethodMapFactory addPackHandler(IAndroidMethodHandler handler) {
        iAndroidPackageHandlerMap.put(handler.getViewClassName(), handler);
        return this;
    }
}
