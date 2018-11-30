package android.Interface;

import android.androidViewTag.AndroidView;

public interface IAndroidPackageHandler {
    public String getPackageName();
    String getViewClassName();
    String generatorMethod(String varName);
    String generatorFindViewById(String ViewName,String idName);
}
