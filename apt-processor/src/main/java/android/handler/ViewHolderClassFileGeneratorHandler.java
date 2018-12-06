package android.handler;

import android.Constant.AndroidConstant;
import android.Factory.AndroidMethodMapFactory;
import android.Interface.DefaultAndroidMethodHandler;
import android.Interface.IAndroidMethodHandler;
import android.androidViewTag.AndroidView;

import com.example.apt_annotation.ViewHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class ViewHolderClassFileGeneratorHandler implements IJavaFileGeneratorHandler {

    private Element mElement;
    private ViewHolder mHoder;
    private Filer mFiler;
    private List<AndroidView> mList;
    private AndroidMethodMapFactory mAndroidFactory;

    public ViewHolderClassFileGeneratorHandler(Filer filer, List<AndroidView> list, Element element) {
        mElement = element;
        mHoder = element.getAnnotation(ViewHolder.class);
        mFiler = filer;
        mList = list;
        mAndroidFactory = AndroidMethodMapFactory.getInstance();
    }

    @Override
    public void generatorJavaFile() {

        ClassName className = ClassName.get(AndroidConstant.Android_V7_Package, AndroidConstant.Android_ViewClssRecyclerView, "ViewHolder");
        TypeSpec.Builder builder = TypeSpec.classBuilder(mHoder.ViewHolderName())
                .superclass(className)
                .addModifiers(Modifier.PUBLIC);
        String entityPackageName = mElement.getEnclosingElement().toString();
        String entityVar = mElement.getSimpleName().toString();

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(AndroidConstant.Android_View_Package, "View"), "itemView")
                .addStatement("super(itemView)");

        FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(entityPackageName,
                entityVar), "m" + entityVar
        ).addModifiers(Modifier.PUBLIC).build();


        MethodSpec.Builder BindDataBuilder = MethodSpec.methodBuilder("bindData")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(entityPackageName, entityVar),
                        "data")
                .addStatement("this." + fieldSpec.name + " = data");


        for (AndroidView androidView : mList) {//AllViews

            IAndroidMethodHandler handler = mAndroidFactory.getPackageHandlerMap().get(androidView.className);
            if (handler == null) {
                handler = new DefaultAndroidMethodHandler(androidView.packageName, androidView.className);
                mAndroidFactory.addPackHandler(handler);
            }

            String method = handler.generatorMethod(androidView.ViewName);
            if (method != null) {//setXXX方法
                BindDataBuilder.addStatement(method);
            }
            //全局变量
            FieldSpec fieldSpec1 = FieldSpec.builder(ClassName.get(handler.getPackageName(),
                    handler.getViewClassName()), androidView.ViewName).addModifiers(Modifier.PUBLIC).build();
            builder.addField(fieldSpec1);
            //findViewById
            constructorBuilder.addStatement(handler.generatorFindViewById(androidView.ViewName, androidView.id),
                    ClassName.get(mHoder.packageName(),"R"));
        }
        builder.addMethod(constructorBuilder.build())
                .addMethod(BindDataBuilder.build())
                .addField(fieldSpec);

        JavaFile javaFile = JavaFile.builder("com", builder.build())
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
