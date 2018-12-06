package android.Interface;

public class ImageViewMethodHandler extends AbsAndroidMethodHandler {
    public ImageViewMethodHandler(String packageName, String className){
        super(packageName,className);
    }

    @Override
    public String generatorMethod(String varName) {
        return varName.concat(".setImageResource(0)");
    }


}