package android.Factory;

import android.Constant.AndroidConstant;
import android.Interface.IAndroidPackageHandler;
import android.Interface.ImageViewPackageHandler;
import android.Interface.TextViewPackageHandler;

import java.util.HashMap;
import java.util.Map;

public class AndroidPackageFatory {
    private Map<String, IAndroidPackageHandler> iAndroidPackageHandlerMap = new HashMap<>();

    public AndroidPackageFatory() {
        iAndroidPackageHandlerMap.put("TextView",
                new TextViewPackageHandler(AndroidConstant.Android_Widget_Package, "TextView"));
        iAndroidPackageHandlerMap.put("ImageView",
                new ImageViewPackageHandler(AndroidConstant.Android_Widget_Package, "ImageView"));

    }

    public Map<String, IAndroidPackageHandler> getPackageHandlerMap() {
        return iAndroidPackageHandlerMap;
    }

    public AndroidPackageFatory addPackHandler(IAndroidPackageHandler handler) {
        iAndroidPackageHandlerMap.put(handler.getViewClassName(), handler);
        return this;
    }
}
