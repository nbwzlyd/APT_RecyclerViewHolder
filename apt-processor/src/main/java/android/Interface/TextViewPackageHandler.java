package android.Interface;

public class TextViewPackageHandler extends AbsAndroidPackageHandler {
    public TextViewPackageHandler(String packageName,String className){
        super(packageName,className);
    }

    @Override
    public String generatorMethod(String varName) {
        return varName.concat(".setText(\"\")");
    }

}
