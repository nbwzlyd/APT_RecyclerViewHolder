package android.Interface;

public class TextViewMethodHandler extends AbsAndroidMethodHandler {
    public TextViewMethodHandler(String packageName, String className){
        super(packageName,className);
    }

    @Override
    public String generatorMethod(String varName) {
        return varName.concat(".setText(\"\")");
    }

}
