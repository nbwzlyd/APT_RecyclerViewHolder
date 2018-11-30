package com.example.apy_processor;
import android.XmlParseUtil;
import android.handler.AndroidSaxHandler;
import android.handler.ClassGeneratorHandler;

import com.example.apt_annotation.ViewHolder;
import com.google.auto.service.AutoService;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.xml.parsers.SAXParser;


@AutoService(Processor.class)
public class ViewHolderProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementUtils;
    private Filer mFiler;
    private Map<String,ClassGeneratorHandler> classGeneratorHandlerMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(ViewHolder.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ViewHolder.class);
        for (Element element : elements) {
            ViewHolder holder = element.getAnnotation(ViewHolder.class);
            SAXParser saxParser = XmlParseUtil.getSaxParser();
            AndroidSaxHandler saxHandler = new AndroidSaxHandler();
            try {
                saxParser.parse(new File(holder.layoutPath()),saxHandler);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ClassGeneratorHandler handler = classGeneratorHandlerMap.get(element.getEnclosingElement().toString());
            if (handler==null){
                handler = new ClassGeneratorHandler(mFiler,saxHandler.getList(),element);
                classGeneratorHandlerMap.put(element.getEnclosingElement().toString(),handler);
                handler.generatorJavaFile();
            }
        }

        return false;
    }
}

