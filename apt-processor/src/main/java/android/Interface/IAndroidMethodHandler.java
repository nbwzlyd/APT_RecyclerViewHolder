package android.Interface;

public interface IAndroidMethodHandler {
    public String getPackageName();
    String getViewClassName();
    String generatorMethod(String varName);
    String generatorFindViewById(String ViewName,String idName);
}
