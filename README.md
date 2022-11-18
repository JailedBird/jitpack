# Jitpack发布最强指南.md

[TOC]

## 0、 前言

发表这篇文章的原因有以下几点：

- 最近想发布fork之后Arouter版本到远端， 但是发现配置私人Maven服务器、MavenCentral等实在是太复杂了，折腾的要死；
- 看了jitpack的发布，觉得不错，但是网上和官网的文档其实一点都不好， 看的也是非常懵逼；
- 后续四处找资料，请教大佬，终于搞好了各种情况下的jitpack发布。于是整理这篇文章希望能帮助大家；



下面先铺垫下本文的一些术语：

1、 Java库

在模块的build.gradle配置java插件， 这就是典型的Java库; 注解处理器模块必须要Java模块编写(如 [arouter-annotation](https://github.com/JailedBird/ARouter/blob/develop/arouter-annotation/build.gradle)) ，大部分Android开发者是不会单独写Java库的; 

```kotlin
apply plugin: 'java'
```



2、 Android库

在模块下的build配置android-library（或android-application）插件(如[arouter-api](https://github.com/JailedBird/ARouter/blob/develop/arouter-api/build.gradle)) , 这也是大部分Android开发者写的库的格式；

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

gradle7之下的库基本上都可以用这种方式发布，发布及其简单



### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP4-muti-lib



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

需要发布的模块build.gradle中， 导入'com.github.dcendents.android-maven' 插件（注：这个插件不区分Android模块和Java模块，直接用即可）

```kotlin
plugins {
    id 'com.github.dcendents.android-maven'
}
```

然后就不用配置任何东西， `GROUP_ID`, `ARTIFICAL_ID`, `VERSION`也不需要再去配置;



### github发布版本 

细节请参考这篇文章：[**Creating Releases 创建发布包**](https://github.com/waylau/github-help/blob/master/Creating%20Releases%20%E5%88%9B%E5%BB%BA%E5%8F%91%E5%B8%83%E5%8C%85.md)； 确定**发布的版本号**、**发布的分支** :

![](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/20221118161410.png)

创建完毕后， 会自动给选择的分支打上标签（等价git tag指令， 因此下文会用TAG去称呼这个发布版本号） ：

![image-20221118161536572](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118161536572.png)

github release下也能找到对应的发布源码：

![image-20221118161604037](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118161604037.png)



### jitpack构建

使用当前github账号登录 jitpack.io, 找到之前发布的release， 点击get it， 等待构建成功

![](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/20221118161826.png)

如下显示绿色的Log图标表示构建成功， 点击它可查看日志；如果构建失败， 请点击其错误日志寻找错误原因；

![image-20221118162358696](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118162358696.png) 

 再次点击 `Get it` 会滑到网页下方， 会告诉你导入插件的方式， 按照路径导入即可

```css
	dependencies {
	    implementation 'com.github.JailedBird.jitpack:lib1:feat-AGP4-muti-lib-V1.0.0'
        implementation 'com.github.JailedBird.jitpack:lib_jvm:feat-AGP4-muti-lib-V1.0.0'
	}
```



### 导入方式总结

多模块下，依赖格式为 `com.github.githubusername.projectname:libraryname:release_tag`

> GROUP_ID = com.github.JailedBird.jitpack
>
> ARTIFICAL_ID = lib1 、 lib_jvm
>
> VERSION=feat-AGP4-muti-lib-V1.0.0



## 2、 AGP4 单library发布

### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP4-single-lib

其他的操作都是一致的， 区别在于导入的方式有细微区别：

![](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118163008299.png)

```
implementation 'com.github.JailedBird:jitpack:feat-AGP4-single-lib-V1.0.0'
```



### 导入方式总结

单模块下，依赖格式为 `com.github.githubusername:projectname:release_tag`

> GROUP_ID = com.github.JailedBird
>
> ARTIFICAL_ID = jitpack
>
> VERSION=feat-AGP4-single-lib-V1.0.0



## AGP4发布小结

- 项目中无法主动配置GROUP_ID、ARTIFICAL_ID、VERSION， 这些都是github用户名、项目名、模块名、TAG这几个元素限定死的；

- 无法本地发布， 因此无法本地验证是否可以正确生成aar文件； 对此可以在不发布github release的情况下使用SNAPSHOT验证你的分支是否可以正常打包； 在jitpack的Branches这里选择选择你要打包的分支，默认的TAG名称: 分支名+SNAPSHOT, 通过他可以快速发布SNAPSHOT library, 也可避免平凡的占用版本号;      

  ![image-20221118164010170](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118164010170.png)

- 默认会发布源码和注释， 不用去专门配置;

- 多模块下，所有模块的版本号（TAG） 都是一样的， 这方便了多library的管理；

- 多模块下，依赖格式为 `com.github.githubusername:projectname:release_tag`

- 单模块下，依赖格式为 `com.github.githubusername:projectname:release_tag`

  

## 3、 AGP7发布多模块发布

AGP7下使用maven-publish插件发布， 功能强很多但是用起来相对也更加麻烦、 门槛更高；



### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP7-muti-lib



### 配置脚本



#### jitpack.yml

AGP7使用的是java11， 配置openjdk11（默认为8, 可能会导致编译不过）, 因此根目录下配置jitpack.yml显示指定jdk11

```
# https://github.com/jitpack/jitpack.io/issues/4474
jdk:
  - openjdk11
```



#### [maven-publish.gradle](https://github.com/JailedBird/jitpack/blob/main/gradle/maven-publish.gradle) 

**该脚本支持android模块和java模块， 默认会发布源码和注释到aar依赖中**；

```
apply plugin: "maven-publish"
/**
 * Reference doc:
 * https://docs.gradle.org/current/dsl/org.gradle.api.publish.maven.MavenPublication.html#org.gradle.api.publish.maven.MavenPublication:artifact(java.lang.Object)
 * https://docs.gradle.org/current/userguide/publishing_setup.html
 * */
afterEvaluate { project ->
    if (plugins.hasPlugin('com.android.application') || plugins.hasPlugin('com.android.library')) {
        /** Android doc*/
        task androidJavadocs(type: Javadoc) {
            failOnError false
            source = android.sourceSets.main.java.source
            classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
        }
        /** Android doc jar*/
        task androidJavadocsJar(type: Jar, dependsOn: androidJavadocs) {
            getArchiveClassifier().convention('javadoc')
            getArchiveClassifier().set('javadoc')
            from androidJavadocs.destinationDir
        }
        /** Android source jar*/
        task androidSourcesJar(type: Jar) {
            getArchiveClassifier().convention('sources')
            getArchiveClassifier().set('sources')
            from android.sourceSets.main.java.source
        }
    } else if (plugins.hasPlugin("java")) {
        /** Java source jar*/
        task sourcesJar(type: Jar, dependsOn: classes) {
            getArchiveClassifier().convention('sources')
            getArchiveClassifier().set('sources')
            from sourceSets.main.allSource
        }
        /** Java doc jar*/
        task javadocJar(type: Jar, dependsOn: javadoc) {
            getArchiveClassifier().convention('javadoc')
            getArchiveClassifier().set('javadoc')
            from javadoc.destinationDir
        }
    }

    if (JavaVersion.current().isJava8Compatible()) {
        allprojects {
            tasks.withType(Javadoc) {
                options.addStringOption('Xdoclint:none', '-quiet')
            }
        }
    }
    /** publish library with doc and source code */
    publishing {
        publications {
            maven(MavenPublication) {
                groupId = GROUP_ID
                artifactId = ARTIFACT_ID
                version = VERSION
                afterEvaluate {
                    if (plugins.hasPlugin('com.android.application') || plugins.hasPlugin('com.android.library')) {
                        from components.release
                    } else if (plugins.hasPlugin("java")) {
                        from components.java
                    }
                }
                if (plugins.hasPlugin('com.android.application') || plugins.hasPlugin('com.android.library')) {
                    artifact androidSourcesJar
                    artifact androidJavadocsJar
                } else if (plugins.hasPlugin("java")) {
                    artifact sourcesJar
                    artifact javadocJar
                }

            }
        }
    }

}
```

将脚本maven-publish.gradle` 放到gradle目录下, 在待发布的模块build.gradle导入脚本

```
apply from: rootProject.file("gradle/maven-publish.gradle")
```



### 配置版本

脚本需要配置GROUP_ID、 VERSION、ARTIFACT_ID ，这三个参数组成本地依赖：`GROUP_ID:ARTIFACT_ID:VERSION`

注意这里的版本号请不要随便配置

- **此配置只关乎本地依赖的路径（mavenLocal）, 与jitpack的依赖路径无关，但不配置会导致远端发布失败，**
- jitpack远程依赖的路径和AGP4是一致的
  - 多模块下，依赖格式为 `com.github.githubusername:projectname:release_tag`
  - 单模块下，依赖格式为 `com.github.githubusername:projectname:release_tag`
  - 多模块下版本号统一为github release's TAG



因此请严格遵守格式， 不要多事！不要多事！不要多事！



假设按照标准格式来配置， 本分支的的配置如下：

因为`GROUP_ID`、`VERSION`是固定的，直接在根目录`gradle.properties` 配置即可， [完整文件](https://github.com/JailedBird/jitpack/blob/feat-AGP7-muti-lib/gradle.properties)

```
# Version
GROUP_ID=com.github.JailedBird.jitpack
# ARTIFACT_ID=jitpack # config in your module
VERSION=0.0.3-beta01
```

在需要发布的模块下新建`gradle.properties` 文件， 配置独立的 `ARTIFACT_ID`, [完整文件](https://github.com/JailedBird/jitpack/blob/feat-AGP7-muti-lib/lib1/gradle.properties)

```
ARTIFACT_ID=lib1
```



### 本地发布

AGP7下支持本地发布， 执行 `gradlew publishToMavenLocal` 发布本地模块， 默认是发布到`mavenLocal` (对应`USER_HOME/.m2/`文件夹）， 根据配置GROUP_ID、 VERSION、ARTIFACT_ID找到对应的文件; 测试阶段使用本地依赖时， 不要忘记加上 `mavenLocal()`以导入仓库；



### 远端发布

按照之前 AGP4多模块配置-github版本发布， 发布代码； 注意请将 TAG设置为代码中配置的VERSION （此处统一为`0.0.3-beta01`）， 免得出问题；

![image-20221118173801965](https://zhaojunchen-1259455842.cos.ap-nanjing.myqcloud.com/img/image-20221118173801965.png)

导入方式：

```
implementation 'com.github.JailedBird.jitpack:lib1:0.0.3-beta01'
implementation 'com.github.JailedBird.jitpack:lib_jvm:0.0.3-beta01'
```



## 4、 AGP7发布单模块发布

### 演示分支

https://github.com/JailedBird/jitpack/tree/feat-AGP7-single-lib



### 版本配置

其他的步骤和AGP7完全一致， 不同的是由于只存在单个library， 因此直接在本模块下配置`gradle.properties` 即可， 配置格式单library方式配置：

 `com.github.githubusername:projectname:release_tag`

如本分支中使用如下配置：

```
GROUP_ID=com.github.JailedBird
ARTIFACT_ID=jitpack
VERSION=feat-AGP7-single-lib-V1.0.0
```

导入方式：

```
implementation 'com.github.JailedBird:jitpack:feat-AGP7-single-lib-V1.0.0'
```



## AGP7发布小结

- 相比AGP4， AGP7支持本地发布和本地导入， 这对调试是很方便；

- AGP7和AGP4的远程依赖路径完全完全一致；

- 建议将配置的GROUP_ID、ARTIFACT_ID、VERSION和远程依赖相匹配， 避免出问题；





## 最后

如果有帮助到你， 请为项目来点star😘
