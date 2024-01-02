---
layout:     post
title:      Flutter 错误和解决汇总
date:       2018-09-09 10:17:53 +0800
postId:     2018-09-09-10-17-53
categories: [Flutter]
keywords:   [App,Flutter]
---

## Flutter基本操作指令

```shell
# 创建项目
flutter create my_flutter_app
# 获取依赖包
flutter packages get
# 运行项目
flutter run
```

## Waiting for another flutter command to release the startup lock

找到了解决方法，如下： 
1、打开flutter的安装目录/bin/cache/ 
2、删除lockfile文件 
3、重启AndroidStudio

[参考](https://github.com/flutter/flutter/issues/7768)

## Android studio 中运行按钮不能点击的问题

确认已经安装了dart和flutter插件，但是依然不能再ide中通过运行和调试按钮来启动，
主要是因为么有设置flutter的

## iOS启动报错：配置开发者账号以及应用信息

* 开发者账号登录
* 应用信息
  - 应用名称
  - 应用唯一ID

```log
Launching lib/main.dart on adolphor's iPhone in debug mode...
Multiple valid development certificates available (your choice will be saved):
  1) Mac Developer: 0haizhu0@gmail.com (NZ64EAQNU7)
  2) iPhone Developer: 0haizhu0@gmail.com (NZ64EAQNU7)
  a) Abort
Please select a certificate for code signing [1|2|a]: 1
Certificate choice "Mac Developer: 0haizhu0@gmail.com (NZ64EAQNU7)" saved
Signing iOS app for device deployment using developer identity: "Mac Developer: 0haizhu0@gmail.com (NZ64EAQNU7)"
Starting Xcode build...
Xcode build done.                                            4.4s
Failed to build iOS app
Error output from Xcode build:
↳
    2018-07-27 11:35:05.785 xcodebuild[576:8462743]  DVTPortal: Service '<DVTPortalViewDeveloperService: 0x7fb4a36fa430; action='viewDeveloper'>' encountered an unexpected result code from the portal ('1100')
    2018-07-27 11:35:05.786 xcodebuild[576:8462743]  DVTPortal: Error:
    Error Domain=DVTPortalServiceErrorDomain Code=1100 "Your session has expired.  Please log in." UserInfo={payload=<CFBasicHash 0x7fb4a3a235a0 [0x7fff9a53baf0]>{type = mutable dict, count = 9,
    entries =>
    	0 : responseId = <CFString 0x7fb4a3a31650 [0x7fff9a53baf0]>{contents = "fd907907-51a0-46df-9f76-731e22201274"}
    	2 : <CFString 0x7fff9a45eb58 [0x7fff9a53baf0]>{contents = "protocolVersion"} = QH65B2
    	3 : <CFString 0x7fb4a3a0fc80 [0x7fff9a53baf0]>{contents = "requestUrl"} = <CFString 0x7fb4a3a354a0 [0x7fff9a53baf0]>{contents = "https://developerservices2.apple.com/services/QH65B2/viewDeveloper.action"}
    	6 : <CFString 0x7fb4a3a1bd10 [0x7fff9a53baf0]>{contents = "userLocale"} = en_US
    	8 : resultCode = <CFNumber 0x44c37 [0x7fff9a53baf0]>{value = +1100, type = kCFNumberSInt64Type}
    	9 : userString = <CFString 0x7fb4a3a12cc0 [0x7fff9a53baf0]>{contents = "Your session has expired.  Please log in."}
    	10 : <CFString 0x7fb4a3a01e70 [0x7fff9a53baf0]>{contents = "resultString"} = <CFString 0x7fb4a3a2ed90 [0x7fff9a53baf0]>{contents = "authentication.failed"}
    	11 : httpCode = <CFNumber 0xc837 [0x7fff9a53baf0]>{value = +200, type = kCFNumberSInt64Type}
    	12 : <CFString 0x7fb4a36f0bd0 [0x7fff9a53baf0]>{contents = "creationTimestamp"} = <CFString 0x7fb4a3a12c30 [0x7fff9a53baf0]>{contents = "2018-07-27T03:35:05Z"}
    }
    , NSLocalizedDescription=Your session has expired.  Please log in.}
    2018-07-27 11:35:06.083 xcodebuild[576:8462999]  DVTPortal: Service '<DVTPortalViewDeveloperService: 0x7fb4a3973ac0; action='viewDeveloper'>' encountered an unexpected result code from the portal ('1100')
    2018-07-27 11:35:06.083 xcodebuild[576:8462999]  DVTPortal: Error:
    Error Domain=DVTPortalServiceErrorDomain Code=1100 "Your session has expired.  Please log in." UserInfo={payload=<CFBasicHash 0x7fb4a38dc5d0 [0x7fff9a53baf0]>{type = mutable dict, count = 9,
    entries =>
    	0 : responseId = <CFString 0x7fb4a38dc770 [0x7fff9a53baf0]>{contents = "acbec446-48f5-4f3a-80c3-6eeeed7715fa"}
    	2 : <CFString 0x7fff9a45eb58 [0x7fff9a53baf0]>{contents = "protocolVersion"} = QH65B2
    	3 : <CFString 0x7fb4a38cf560 [0x7fff9a53baf0]>{contents = "requestUrl"} = <CFString 0x7fb4a3f0bb30 [0x7fff9a53baf0]>{contents = "https://developerservices2.apple.com/services/QH65B2/viewDeveloper.action"}
    	6 : <CFString 0x7fb4a3869830 [0x7fff9a53baf0]>{contents = "userLocale"} = en_US
    	8 : resultCode = <CFNumber 0x44c37 [0x7fff9a53baf0]>{value = +1100, type = kCFNumberSInt64Type}
    	9 : userString = <CFString 0x7fb4a38ca290 [0x7fff9a53baf0]>{contents = "Your session has expired.  Please log in."}
    	10 : <CFString 0x7fb4a0c04ce0 [0x7fff9a53baf0]>{contents = "resultString"} = <CFString 0x7fb4a38deac0 [0x7fff9a53baf0]>{contents = "authentication.failed"}
    	11 : httpCode = <CFNumber 0xc837 [0x7fff9a53baf0]>{value = +200, type = kCFNumberSInt64Type}
    	12 : <CFString 0x7fb4a0c10040 [0x7fff9a53baf0]>{contents = "creationTimestamp"} = <CFString 0x7fb4a38cee30 [0x7fff9a53baf0]>{contents = "2018-07-27T03:35:05Z"}
    }
    , NSLocalizedDescription=Your session has expired.  Please log in.}
    ** BUILD FAILED **

Xcode's output:
↳
    error: The operation couldn’t be completed. Unable to log in with account '0haizhu0@gmail.com'. The login details for account '0haizhu0@gmail.com' were rejected. (in target 'Runner')
    error: No profiles for 'com.example.demo' were found: Xcode couldn't find any iOS App Development provisioning profiles matching 'com.example.demo'. (in target 'Runner')
    note: Using new build systemnote: Planning buildnote: Constructing build description
Could not build the precompiled application for the device.

It appears that your application still contains the default signing identifier.
Try replacing 'com.example' with your signing id in Xcode:
  open ios/Runner.xcworkspace

Error launching application on adolphor's iPhone.
```

## iOS启动报错：xcode编译报错，无权限

* Open ios/Runner.xcworkspace in Xcode 10 beta 3
* From Xcode menu select: "File ~> Workspace settings..."
* Change selected build system from "New build system (Default)" to "Legacy build system"

参考：[Getting Error when Run flutter app on iOS 12 device with Xcode 10 beta](https://github.com/flutter/flutter/issues/19241)

```log
Launching lib/main.dart on adolphor's iPhone in debug mode...
Automatically signing iOS for device deployment using specified development team in Xcode project: YWSQE478A3
Starting Xcode build...
 ├─Assembling Flutter resources...                    9.6s
 └─Compiling, linking and signing...                 12.1s
Xcode build done.                                           29.3s
Failed to build iOS app
Error output from Xcode build:
↳
    ** BUILD FAILED **

Xcode's output:
↳
    While building module 'Flutter' imported from /Users/adolphor/ApkProjects/flutter/demo/ios/Runner/GeneratedPluginRegistrant.h:8:
    In file included from <module-includes>:1:
    In file included from /Users/adolphor/ApkProjects/flutter/demo/ios/Flutter/Flutter.framework/Headers/Flutter.h:37:
    In file included from /Users/adolphor/ApkProjects/flutter/demo/ios/Flutter/Flutter.framework/Headers/FlutterAppDelegate.h:11:
    /Users/adolphor/ApkProjects/flutter/demo/ios/Flutter/Flutter.framework/Headers/FlutterPlugin.h:140:58: warning: this block declaration is not a prototype [-Wstrict-prototypes]
                          completionHandler:(nonnull void (^)())completionHandler;
                                                             ^
                                                              void
    1 warning generated.
    1 warning generated.
    While building module 'Flutter' imported from /Users/adolphor/ApkProjects/flutter/demo/ios/Runner/main.m:1:
    In file included from <module-includes>:1:
    In file included from /Users/adolphor/ApkProjects/flutter/demo/ios/Flutter/Flutter.framework/Headers/Flutter.h:37:
    In file included from /Users/adolphor/ApkProjects/flutter/demo/ios/Flutter/Flutter.framework/Headers/FlutterAppDelegate.h:11:
    /Users/adolphor/ApkProjects/flutter/demo/ios/Flutter/Flutter.framework/Headers/FlutterPlugin.h:140:58: warning: this block declaration is not a prototype [-Wstrict-prototypes]
                          completionHandler:(nonnull void (^)())completionHandler;
                                                             ^
                                                              void
    1 warning generated.
    1 warning generated.
    error: open /Users/adolphor/ApkProjects/flutter/demo/build/ios/Debug-iphoneos/Runner.app/Frameworks/Flutter.framework/Flutter: Permission denied
    note: Using new build systemnote: Planning buildnote: Constructing build description
Could not build the precompiled application for the device.

Error launching application on adolphor's iPhone.
```

## iOS启动报错：需要在手机上信任开发者账号描述文件

出现以下问题，说明app已经安装到手机，下面要做的就是，在手机上信任开发者账号描述文件

```log
Launching lib/main.dart on adolphor's iPhone in debug mode...
Automatically signing iOS for device deployment using specified development team in Xcode project: YWSQE478A3
Starting Xcode build...
 ├─Assembling Flutter resources...                    1.4s
 └─Compiling, linking and signing...                  2.5s
Xcode build done.                                            5.7s
Installing and launching...
Traceback (most recent call last):
  File "<input>", line 1, in <module>
  File "/usr/local/Cellar/python@2/2.7.15_1/Frameworks/Python.framework/Versions/2.7/lib/python2.7/copy.py", line 52, in <module>
    import weakref
  File "/usr/local/Cellar/python@2/2.7.15_1/Frameworks/Python.framework/Versions/2.7/lib/python2.7/weakref.py", line 14, in <module>
    from _weakref import (
ImportError: cannot import name _remove_dead_weakref
Could not install build/ios/iphoneos/Runner.app on a02a9dc7bbd896a2131403e45e55132c1c7067ae.
Try launching Xcode and selecting "Product > Run" to fix the problem:
  open ios/Runner.xcworkspace

Error launching application on adolphor's iPhone.
```

启动成功之后会显示如下信息：

```log
Launching lib/main.dart on adolphor's iPhone in debug mode...
Automatically signing iOS for device deployment using specified development team in Xcode project: YWSQE478A3
Starting Xcode build...
 ├─Assembling Flutter resources...                    1.5s
 └─Compiling, linking and signing...                  2.7s
Xcode build done.                                            5.8s
Installing and launching...
Traceback (most recent call last):
  File "<input>", line 1, in <module>
  File "/usr/local/Cellar/python@2/2.7.15_1/Frameworks/Python.framework/Versions/2.7/lib/python2.7/copy.py", line 52, in <module>
    import weakref
  File "/usr/local/Cellar/python@2/2.7.15_1/Frameworks/Python.framework/Versions/2.7/lib/python2.7/weakref.py", line 14, in <module>
    from _weakref import (
ImportError: cannot import name _remove_dead_weakref
Syncing files to device adolphor's iPhone...                 4.8s

🔥  To hot reload changes while running, press "r". To hot restart (and rebuild state), press "R".
An Observatory debugger and profiler on adolphor's iPhone is available at: http://127.0.0.1:8100/
For a more detailed help message, press "h". To quit, press "q".
```

## 参考资料

* [Getting Error when Run flutter app on iOS 12 device with Xcode 10 beta](https://github.com/flutter/flutter/issues/19241)
