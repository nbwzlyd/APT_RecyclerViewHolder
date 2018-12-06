package android.Interface;


public abstract class AbsAndroidMethodHandler implements IAndroidMethodHandler {

    protected String packageName;
    protected String className;

    public AbsAndroidMethodHandler(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getViewClassName() {
        return className;
    }

    @Override
    public String generatorMethod(String varName) {
        return null;
    }

    @Override
    public String generatorFindViewById(String ViewName, String idName) {
        return ViewName + " = itemView.findViewById($T.id." + idName+")";
    }
}
