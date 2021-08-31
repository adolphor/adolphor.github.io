---
layout:     post
title:      macOS 系统软件备份
date:       2021-08-28 18:48:55 +0800
postId:     2021-08-28-18-48-55
categories: [macOS]
keywords:   [macOS]
---

## brew安装软件备份
```
# 备份
$ brew install htop
$ brew tap Homebrew/bundle
$ brew bundle dump
$ mv Brewfile ~/cloud
# 还原
$ brew bundle
```
[](https://tomlankhorst.nl/brew-bundle-restore-backup/)

## iter2 备份和还原

```
iTerm -> Preferences -> Profiles -> Other Actions -> Copy All Profiles as JSON
```

## maven 备份和还原
setting.xml备份一下即可

## ssh
文件夹备份即可

## application

软链备份：
```
lrwxr-xr-x   1 adolphor  staff    29B Dec 19  2019 gradle -> /usr/local/opt/gradle/libexec
lrwxr-xr-x   1 adolphor  staff    28B Dec 19  2019 maven -> /usr/local/opt/maven/libexec
lrwxr-xr-x   1 adolphor  staff    32B Dec 19  2019 tomcat8 -> /usr/local/opt/tomcat@8/libexec/
lrwxr-xr-x   1 adolphor  staff    31B Mar 11  2020 tomcat9 -> /usr/local/opt/tomcat@9/libexec
```

项目备份：
```
BaiduExporter
Downie
UnblockNeteaseMusic
YAAW-for-Chrome
crackJetbrain
dbeaver-agent
diy-shell
flutter
istio-1.10.0
loginShell.sh
playFramework
surge
v2ray
yaaw
```

## 文档备份
工作空间

## 项目备份
IdeaProjects

## 手动安装brew软件

### openJDK
```
mkdir -p /usr/local/Cellar/openjdk
cd /usr/local/Cellar/openjdk
ln -s /Library/Java/JavaVirtualMachines/jdk-16.0.2.jdk/Contents/Home 16.0.2
cd /usr/local/opt
ln -s ../Cellar/openjdk/16.0.2 openjdk
```

### openSSL@1.1
再macOS 10.13.6 上安装 OpenSSL@1.1报错：
```
In file included from crypto/rand/rand_unix.c:38:
/usr/include/CommonCrypto/CommonRandom.h:35:9: error: unknown type name 'CCCryptorStatus'
typedef CCCryptorStatus CCRNGStatus;
        ^
crypto/rand/rand_unix.c:385:47: error: use of undeclared identifier 'kCCSuccess'
    if (CCRandomGenerateBytes(buf, buflen) == kCCSuccess)
                                              ^
2 errors generated.
make[1]: *** [crypto/rand/rand_unix.o] Error 1
make: *** [all] Error 2
```
参考 [StackOverflow](https://stackoverflow.com/questions/68957915/macos-10-12-brew-install-openssl-issue?rq=1) 
答案需要编辑brew编译参数：
```
$ brew edit openssl
```
找到 `def configure_args` 代码片段：
```
  def configure_args
    args = %W[
      --prefix=#{prefix}
      --openssldir=#{openssldir}
      no-ssl3
      no-ssl3-method
      no-zlib
    ]
    on_linux do
      args += (ENV.cflags || "").split
      args += (ENV.cppflags || "").split
      args += (ENV.ldflags || "").split
      args << "enable-md2"
    end
    args
  end
```
再 no-zlib 后面添加一行 `-I/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include`：
```
  def configure_args
    args = %W[
      --prefix=#{prefix}
      --openssldir=#{openssldir}
      no-ssl3
      no-ssl3-method
      no-zlib
      -I/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include
    ]
    on_linux do
      args += (ENV.cflags || "").split
      args += (ENV.cppflags || "").split
      args += (ENV.ldflags || "").split
      args << "enable-md2"
    end
    args
  end
```
再次使用brew尝试重新安装即可：
```
$ brew reinstall openssl

==> Downloading https://www.openssl.org/source/openssl-1.1.1l.tar.gz
Already downloaded: /Users/adolphor/Library/Caches/Homebrew/downloads/b6ccc5a2a602c2af3480bbcf1656bd9844595974ba60501871ac12504508e818--openssl-1.1.1l.tar.gz
==> Reinstalling openssl@1.1
==> perl ./Configure --prefix=/usr/local/Cellar/openssl@1.1/1.1.1l --openssldir=/usr/local/etc/openssl@1.1 no-ssl3 no-ssl3-method no-zlib -I/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include darwin64-x86_64-cc enable-ec_nistp_64_gcc_128
==> make
==> make install MANDIR=/usr/local/Cellar/openssl@1.1/1.1.1l/share/man MANSUFFIX=ssl
==> make test
==> Regenerating CA certificate bundle from keychain, this may take a while...
==> Caveats
A CA file has been bootstrapped using certificates from the system
keychain. To add additional certificates, place .pem files in
  /usr/local/etc/openssl@1.1/certs

and run
  /usr/local/opt/openssl@1.1/bin/c_rehash

openssl@1.1 is keg-only, which means it was not symlinked into /usr/local,
because macOS provides LibreSSL.

If you need to have openssl@1.1 first in your PATH, run:
  echo 'export PATH="/usr/local/opt/openssl@1.1/bin:$PATH"' >> ~/.zshrc

For compilers to find openssl@1.1 you may need to set:
  export LDFLAGS="-L/usr/local/opt/openssl@1.1/lib"
  export CPPFLAGS="-I/usr/local/opt/openssl@1.1/include"

For pkg-config to find openssl@1.1 you may need to set:
  export PKG_CONFIG_PATH="/usr/local/opt/openssl@1.1/lib/pkgconfig"

==> Summary
🍺  /usr/local/Cellar/openssl@1.1/1.1.1l: 8,066 files, 18.4MB, built in 4 minutes 56 seconds
```

### 手动软链示意
```
perl ./Configure --prefix=/usr/local/Cellar/openssl@1.1/1.1.1l \
    --openssldir=/usr/local/etc/openssl@1.1 no-ssl3 no-ssl3-method no-zlib \
    -I/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include \
    darwin64-x86_64-cc enable-ec_nistp_64_gcc_128

make
make install

cd /usr/local/opt
ln -s ../Cellar/openssl@1.1/1.1.1l openssl
ln -s ../Cellar/openssl@1.1/1.1.1l openssl@1.1
```

## 参考资料
* [macOS 系统软件备份]({% post_url system/macos/2021-08-28-01-macos-system-software-backup %})

