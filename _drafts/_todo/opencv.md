
* [安装](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#install-opencv-3-x-under-macos)
* [Install OpenCV for Java on Mac OS X](https://deepturf.wordpress.com/2016/08/08/install-opencv-on-mac-osx/)

安装编译jar的时候打开jdk以及制定jdk版本：
```
$ brew edit opencv
# OFF修改为ON
-DBUILD_opencv_java=ON
# 指定JDK版本
-DOPENCV_JAVA_SOURCE_VERSION=1.8 
-DOPENCV_JAVA_TARGET_VERSION=1.8

$ brew install --build-from-source opencv
```

* [绘制四方形](https://www.yiibai.com/opencv/opencv_drawing_rectangle.html)
* [人脸识别](https://blog.csdn.net/u010697780/article/details/89929132)


