package android.Interface;

import android.androidViewTag.AndroidView;

public class ImageViewPackageHandler extends AbsAndroidPackageHandler {
    public ImageViewPackageHandler(String packageName,String className){
        super(packageName,className);
    }

    @Override
    public String generatorMethod(String varName) {
        return varName.concat(".setImageResource(0)");
    }


}
