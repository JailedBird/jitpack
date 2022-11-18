# Jitpack发布最强指南.md

[TOC]

## 0、 前言

发表这篇文章的原因有以下几点：

- 最近想发布fork之后Arouter版本到远端， 但是发现配置私人Maven服务器、MavenCentral等实在是太复杂了，折腾的要死；
- 看了jitpack的发布，觉得不错，但是网上和官网的文档其实一点都不好， 看的也是非常懵逼；
- 后续四处找资料，请教大佬，终于搞好了各种情况下的jitpack发布。于是整理这篇文章希望能帮助大家；

下面先铺垫下本文的一些术语：
1、 Java库
如 [arouter-annotation](https://github.com/JailedBird/ARouter/blob/develop/arouter-annotation/build.gradle) 在模块的build.gradle配置java插件， 这就是典型的Java库; 注解处理器模块必须要Java模块编写，大部分Android开发者是不会单独写Java库的;

```kotlin
apply plugin: 'java'
```

2、 Android库
如[arouter-api](https://github.com/JailedBird/ARouter/blob/develop/arouter-api/build.gradle) 在模块下的build配置android-library（或android-application）插件, 这也是大部分Android开发者写的库的格式；

```kotlin
apply plugin: 'com.android.library'
```

3、 AGP插件版本
升到gradle7后插件由AGP4变为AGP7，相应的插件、发布方式也变化很大， 因此非常必要区分AGP4和AGP7下的发布插件

4、 单library、多library
单library指的是工程下只存在一个需要发布到远端的库, 大多数库都是都是这样
多library指的是工程下存在多个需要发布到远端的库，coil、arouter都是这样的


接下来我会分四种情况介绍jitpack的发布

- AGP4下环境下，多library下的插件发布
- AGP4下环境下，单library下的插件发布
- AGP7下环境下，多library下的插件发布
- AGP7下环境下，单library下的插件发布



## 1、 AGP4 多library发布

### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP4-muti-lib

gradle7之下的库基本上都可以用这种方式发布，发布及其简单

### 配置插件

- 导入jitpack仓库
- 配置android-maven-gradle-plugin插件， 可参考[此处](https://plugins.gradle.org/plugin/com.github.dcendents.android-maven)

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
    }
}

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

注意： 这个插件被标记废弃了， 在AGP7就不要使用了， 但是AGP4下使用还是非常香的！

### 导入插件

需要发布的模块build.gradle中， 导入'com.github.dcendents.android-maven' 插件
（注：这个插件不区分Android模块和Java模块，直接用即可）

```kotlin
plugins {
    id 'com.github.dcendents.android-maven'
}
```

然后就不用配置任何东西， 包括GROUP_ID, ARTIFICAL_ID, VERSION

### github发布版本

细节请参考这篇文章：[**Creating Releases 创建发布包**](https://github.com/waylau/github-help/blob/master/Creating%20Releases%20%E5%88%9B%E5%BB%BA%E5%8F%91%E5%B8%83%E5%8C%85.md)； 一通操作下来按照如下操作, 确定**发布版本号**、**发布的分支** ， 如下图：

![](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/20221118161410.png)

上述的TAG和分支及其重要， 发布时候不要选错！

创建完毕后， 会给选择的分支打上标签：

![image-20221118161536572](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118161536572.png)

github release下也能找到对应的发布源码：

![image-20221118161604037](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118161604037.png)



### jitpack构建

使用当前github账号登录 jitpack.io, 找到之前发布的release， 点击get it， 等待构建成功

![](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/20221118161826.png)

构建完成后可以查看日志排查问题， 构建成功后就可以滑到下方

![image-20221118162358696](Jitpack发布最强指南.assets/image-20221118162358696.png)

网站会告诉你导入方式：Add the dependency

```css
	dependencies {
	    implementation 'com.github.JailedBird.jitpack:lib1:feat-AGP4-muti-lib-V1.0.0'
        implementation 'com.github.JailedBird.jitpack:lib_jvm:feat-AGP4-muti-lib-V1.0.0'
	}
```



### 导入方式总结

多模块下，格式为： `com.github.github用户名.项目名:模块名:release发布时的TAG`



## 2、 AGP4 单library发布

### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP4-single-lib

其他的操作都是一致的， 区别在于导入的方式有细微区别：



![image-20221118163008299](Jitpack发布最强指南.assets/image-20221118163008299.png)

```
implementation 'com.github.JailedBird:jitpack:feat-AGP4-single-lib-V1.0.0'
```



### 导入方式总结

单模块下，格式为： `com.github.github用户名.项目名:release发布时的TAG`



## AGP4发布小结

- 项目中无法决定版本号、groupid，artificialid，这些都是github和项目的名称决定的， 这样某种意义上是存在局限的， 但是确实方便

- 无法本地发布， 因此无法本地验证生成aar

- 可以再不发布github release的情况下使用SNAPSHOT验证你的分支是否可以正常打包， 这是TAG的名称为 分支名+SNAPSHOT, 通过他可以快速发布调试library

  ![image-20221118164010170](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118164010170.png)

- 默认会发布源码和注释， 不用去专门配置

- 多模块下， 所有模块的版本号（TAG） 都是一样的， 这方便了管理



## 3、 AGP7发布多模块发布

### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP7-muti-lib

### 配置脚本

根目录下配置jitpack.yml， AGP7使用的是java11， 配置openjdk11（默认貌似为8）

```
# https://github.com/jitpack/jitpack.io/issues/4474
jdk:
  - openjdk11
```

将脚本放到`maven-publish.gradle` 放到项目中， 本项目放到gradle目录下， [请下载脚本到自己的目录中](https://github.com/JailedBird/jitpack/blob/main/gradle/maven-publish.gradle)

**该脚本支持android模块和java模块， 默认会发布源码和注释到aar依赖中**

待发布的模块build.gradle导入脚本

```
apply from: rootProject.file("gradle/maven-publish.gradle")
```



### 配置版本

脚本需要配置GROUP_ID、 VERSION、ARTIFACT_ID 这三个参数组成：`GROUP_ID:ARTIFACT_ID:VERSION`

注意这里的版本号请不要随便配置

- 这个配置只关乎本地发布相关（mavenLocal），但是不配置远端发布会失败，但是这个和jitpack的依赖路径无关

- jitpack远程依赖的路径和AGP4下是一模一样的

    -  多模块下 `com.github.github用户名.项目名:模块名:release发布时的TAG`
    -  单模块下 `com.github.github用户名.项目名:release发布时的TAG`
    - 多模块下版本号统一为TAG

  因此请严格遵守之前的格式来设置这三个参数， 不要多事！不要多事！不要多事！ 给自己找麻烦

```
# Version
GROUP_ID=com.github.JailedBird.jitpack
ARTIFACT_ID=lib1
# ARTIFACT_ID=jitpack # config in your module
VERSION=0.0.2
```



假设按照标准的方式来配置， 下面说下本项目的配置方式：

因为GROUP_ID、VERSION是固定的，直接在根目录`gradle.properties` 配置即可， [完整文件](https://github.com/JailedBird/jitpack/blob/main/gradle.properties)

```
# Version
GROUP_ID=com.github.JailedBird.jitpack
# ARTIFACT_ID=jitpack # config in your module
VERSION=0.0.3-beta01
```

在需要发布的模块下新建`gradle.properties` 文件， 配置ARTIFACT_ID, [完整文件](https://github.com/JailedBird/jitpack/blob/main/lib1/gradle.properties)

```
ARTIFACT_ID=lib1
```



### 本地发布

AGP7下支持本地发布， 执行 `gradlew publishToMavenLocal` 发布本地模块， 默认是发布到`mavenLocal` (对应USER_HOME/.m2/文件夹）， 根据配置GROUP_ID、 VERSION、ARTIFACT_ID找到对应的文件， 测试阶段本地依赖， 请在仓库导入处配置 `mavenLocal()`



### 远端发布

首先完成github release， 参照之前即可， 注意请把 TAG配置的VERSION一致， 然后去jitpack构建发布 根据提示依赖即可（注意：最好按照之前说的统一配置）

![image-20221118173801965](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118173801965.png)

导入方式：

```
implementation 'com.github.JailedBird.jitpack:lib1:0.0.3-beta01'
implementation 'com.github.JailedBird.jitpack:lib_jvm:0.0.3-beta01'
```



### 导入方式总结

多模块下，格式为： `com.github.github用户名.项目名:模块名:release发布时的TAG` ，不会因为GROUP_ID、 VERSION、ARTIFACT_ID而改变



## 4、 AGP7发布单模块发布

演示分支：

和AGP7多模块配置类似， 但是版本号格式变为了 `com.github.github用户名.项目名:release发布时的TAG` ， 由于只是单个library， 因此直接在本模块下配置`gradle.properties` 即可

```
GROUP_ID=com.github.JailedBird
ARTIFACT_ID=jitpack
VERSION=feat-AGP7-single-lib-V1.0.0
```

导入方式：

```
implementation 'com.github.JailedBird:jitpack:feat-AGP7-single-lib-V1.0.0'
```



### 导入方式总结

单模块下，格式为： `com.github.github用户名.项目名:release发布时的TAG`, 不会因为GROUP_ID、 VERSION、ARTIFACT_ID而改变





## AGP7发布小结



- 相比AGP4， AGP7支持本地发布和本地导入， 这对调试是很方便的

- 配置的插件版本只适用本地依赖， jitpack依赖的全称路径和AGP4是一致的， 为避免麻烦最好统一起来

  

