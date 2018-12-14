# APT_RecyclerViewHolder
利用注解、APT、javaPoet自动生成RecyclerViewHolder代码
![自动生成代码](https://github.com/nbwzlyd/APT_RecyclerViewHolder/blob/master/app/gif/code.gif)
@[TOC](这里写自定义目录标题)
具体看博客 https://blog.csdn.net/wzlyd1/article/details/85005840

# 开题

注解在很多框架中频繁的使用，比如大名鼎鼎的ButterKnife，ButterKnife运用的就是编译时注解,ButterKnife在我们编译时，就根据注解，自动生成了一些辅助类。当然还有EventBus3,也用到了注解的方法。所以，学习注解显得就尤为重要。

## 注解的核心方法

在介绍核心用法之前，先照例引用一些概念

    @Retention 
    这个注解表示注解的保留方式，有如下三种： 
    SOURCE:只保留在源码中，不保留在class中，同时也不加载到虚拟机中 
    CLASS:保留在源码中，同时也保留到class中，但是不加载到虚拟机中 
    RUNING:保留到源码中，同时也保留到class中，最后加载到虚拟机中

    @Target 
    这个注解表示注解的作用范围，主要有如下:
    ElementType.FIELD 注解作用于变量
    ElementType.METHOD 注解作用于方法
    ElementType.PARAMETER 注解作用于参数
    ElementType.CONSTRUCTOR 注解作用于构造方法
    ElementType.LOCAL_VARIABLE 注解作用于局部变量
    ElementType.PACKAGE 注解作用于包
    由于该例子只用到这几个元注解，所以只需要了解这两个即可，其他自行查询资料。

在我理解看来，注解的核心使用方法就三个 
 1. **自定义注解** ，如 

```
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ViewHolder {
     String layoutPath();
     String ViewHolderName();
     String packageName();
}
```
 2. **获取注解**  如 ：
```Test.class.getAnnotation(ViewHolder.class)```
或者```xxx.getAnnotation(ViewHolder.class)```

 3. **获取注解值** 如：
 ```ViewHolder holder = XXX.getAnnotation(ViewHolder.class) ;```
 ```String layoutPath = holder.layoutPath()```

能拿到值，我们的成功之路就走了80%，只要有值，我们就能干我们想干的事情。

## APT
APT(Annotation Processing Tool)是一种处理注释的工具,它对源代码文件进行检测找出其中的Annotation，使用Annotation进行额外的处理。
Annotation处理器在处理Annotation时可以根据源文件中的Annotation生成额外的源文件和其它的文件(文件具体内容由Annotation处理器的编写者决定),APT还会编译生成的源文件和原来的源文件，将它们一起生成class文件。
在编译初期，我们想利用注解的值做处理的时候，就用到了这个技术，或者工具。
 
## JavaPoet
又是大神JakeWharton开源的一款快速代码生成工具，可使用API 快速生成.java文件，与APT绝配
 [JavaPoet github地址](https://github.com/square/javapoet)

有了这三样工具，我们就可以让计算机帮我们实现一些模板代码或者口水代码，比如ViewHolder。

## 为什么要写这么一套代码？

 1. 学习，毕竟注解很重要，APT也很重要，学无止境
 2. 懒，虽然butterKnife可以帮我们很快的绑定 事件以及view，可是我还是觉得繁琐，我觉得直接写完布局后一键生成所需代码最简单粗暴。当然有as插件帮你写butterKnife。
 3.  还是为了学习，写这套代码我自己都不一定会用，而且很多AS插件支持一键生成代码，比如：Android code Generator  。
 废话不多说，我们开始demo之路。


## apt-annotaition
在一个新的Android工程新建后，我们在项目上右击，新建Module，选择JavaLibrary.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181214152507310.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3d6bHlkMQ==,size_16,color_FFFFFF,t_70)
起名为 apt-annotaition，用来存放我们的自定义注解。
然后在该library的 build.gradle 中增加如下代码

```
apply plugin: 'java-library'
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8"
```

## apt-processor

重复上一遍操作，新建javaLibrary ，然后起名为apt-processor，这个就是我们的核心代码存放库，用来处理我们的注解，生成.java模板代码的地方。这个library要依赖于**apt-annotaition**
build.gradle文件做如下处理：

```
apply plugin: 'java-library'
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.google.auto.service:auto-service:1.0-rc2'
    implementation 'com.squareup:javapoet:1.11.1'
    implementation project(':apt-annotation')

}
sourceCompatibility = "1.8"
targetCompatibility = "1.8"
```
其中`implementation 'com.google.auto.service:auto-service:1.0-rc2'` 是谷歌爸爸的一个库，引用网上一段说法就是

    **介绍下依赖库auto-service
    在使用注解处理器需要先声明，步骤：
    1、需要在 processors 库的 main 目录下新建 resources 资源文件夹；
    2、在 resources文件夹下建立 META-INF/services 目录文件夹；
    3、在 META-INF/services 目录文件夹下创建 javax.annotation.processing.Processor 文件；
    4、在 javax.annotation.processing.Processor 文件写入注解处理器的全称，包括包路径；）
    这样声明下来也太麻烦了？这就是用引入auto-service的原因。
    通过auto-service中的@AutoService可以自动生成AutoService注解处理器是Google开发的，用来生成 META-INF/services/javax.annotation.processing.Processor 文件的**
```
 implementation 'com.squareup:javapoet:1.11.1'
```
是javaPoet，帮助我们生成.java文件。
到这里，我们的准备工作基本完工。
但是别忘了app的build.gradle配置
## app的build.gradle
配置如下，不用完全相同，主要是标注部分，要依赖于 **apt-processor apt-annotaition**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181214154313658.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3d6bHlkMQ==,size_16,color_FFFFFF,t_70)
至此，准备工作完毕。


## 自定义我们的注解ViewHolder
在定义之前，我们要先想一下我们的需求需要哪些参数，因为涉及到布局文件，所以我肯定需要文件的路径，以便我们解析，除此之外，我还要一个名字参数，用来给ViewHolder起名字。
另外，我们还需要包名，这个是后续开发过程中才想到的，因为我们在findViewById的时候引入R包需要包名。
基本上就这三个参数，想明白了，就动手吧
 在**apt-annotaition**中新建一个class 文件，代码如下
```
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ViewHolder {
     String layoutPath();
     String ViewHolderName();
     String packageName();
}
```



### 创建核心代码 ViewHolderProcessor
 在**apt-processor**中新建类，继承自**AbstractProcessor**，复写相关方法
```
public Set<String> getSupportedAnnotationTypes()
支持的注解类型
```
由于我们只定义了ViewHolder类型，这里返回 ViewHolder类型的set集合即可

```
 public SourceVersion getSupportedSourceVersion()
```
支持的源码版本，写死`return SourceVersion.latestSupported()`即可

```
public synchronized void init(ProcessingEnvironment processingEnvironment)
```
初始化方法，用来获取一些必要参数，这里只用到了filer，mMessager
代码如下

```
@Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }
```
 mFiler 你可以理解为时文件写入
  mMessager 可以理解为logcat;

```
public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
```
是我们处理注解的核心方法，在这里我们将获取到注解的类型和值。来生成我们需要的模板代码。
具体代码可见源码。

##  整体架构
写这个标题，我自己都笑了，一个demo有啥架构，不过还是贴上来吧，毕竟把demo完整介绍一遍也不现实
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181214163109751.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3d6bHlkMQ==,size_16,color_FFFFFF,t_70)

## 遇到的问题
1.如果你想看mMessger打印的日志信息，可以在build选项卡中选择
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181214162108104.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3d6bHlkMQ==,size_16,color_FFFFFF,t_70)
网上说有message选项卡，不知道为什么我的没有

2. R包无法导入，之前刚写完的时候没问题，但是现在突然出现了，不知道原因，另外，如果生成的模板文件如果包名和你的app包名重复，R的包更是无法导入，原因未知，所以我现在随便写了一个com包。
##  代码生成办法
一键，点击锤子按钮即可 具体看github 
[apt一键生成viewHolder](https://github.com/nbwzlyd/APT_RecyclerViewHolder)

