---
layout:     post
title:      brew 使用相关
date:       2018-04-29 20:58:37 +0800
postId:     2018-04-29-20-58-37
categories: [macOS]
keywords:   [brew]
---
## 安装

## 一般应用

```shell
# 检查过时应用
brew outdated
# 升级
brew update && brew upgrade
# 清除安装包
brew cleanup --prune=10
```

## 可视化应用

```shell
# 首先确保本命令执行过
brew tap buo/cask-upgrade
# 检查过时
brew cask outdated
# 升级
brew cu -a -y --cleanup
# 清除安装包
brew cask cleanup --outdated
```

## 清理缓存
新版本会超过30天自动清理，当然也可以手动清理：
```shell
brew cleanup
```

## 手动安装

2021年10月的时候，发现brew不再支持macOS 10.13.6 High Serria版本，但如果把MacBook Pro 2016
的macOS 升级到 10.15，会感觉系统性能不够，idea开多个实例的时候会卡，所以如果退回10.13.6，
那么部分brew软件既没有编译完成的包可直接使用，从源码编译安装也会遇到各种问题，所以有些软件需要
使用其他方式安装，安装完成之后，可以软链到brew，作为brew其他软件依赖的时候使用。

### rust
在macOS High Sierra 上使用brew安装 rust 1.55.0 失败，改用如下安装方式：
```shell
# https://www.rust-lang.org/tools/install
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
```
```
info: downloading installer

Welcome to Rust!

This will download and install the official compiler for the Rust
programming language, and its package manager, Cargo.

Rustup metadata and toolchains will be installed into the Rustup
home directory, located at:

  /Users/adolphor/.rustup

This can be modified with the RUSTUP_HOME environment variable.

The Cargo home directory located at:

  /Users/adolphor/.cargo

This can be modified with the CARGO_HOME environment variable.

The cargo, rustc, rustup and other commands will be added to
Cargo's bin directory, located at:

  /Users/adolphor/.cargo/bin

This path will then be added to your PATH environment variable by
modifying the profile files located at:

  /Users/adolphor/.profile
  /Users/adolphor/.bash_profile
  /Users/adolphor/.zshenv

You can uninstall at any time with rustup self uninstall and
these changes will be reverted.

Current installation options:


   default host triple: x86_64-apple-darwin
     default toolchain: stable (default)
               profile: default
  modify PATH variable: yes

1) Proceed with installation (default)
2) Customize installation
3) Cancel installation
>1

info: profile set to 'default'
info: default host triple is x86_64-apple-darwin
info: syncing channel updates for 'stable-x86_64-apple-darwin'
info: latest update on 2021-09-09, rust version 1.55.0 (c8dfcfe04 2021-09-06)
info: downloading component 'cargo'
  4.4 MiB /   4.4 MiB (100 %) 854.0 KiB/s in  5s ETA:  0s
info: downloading component 'clippy'
  1.7 MiB /   1.7 MiB (100 %) 485.3 KiB/s in  3s ETA:  0s
info: downloading component 'rust-docs'
 17.1 MiB /  17.1 MiB (100 %) 777.7 KiB/s in 39s ETA:  0s
info: downloading component 'rust-std'
 21.0 MiB /  21.0 MiB (100 %) 920.0 KiB/s in 25s ETA:  0s
info: downloading component 'rustc'
 75.9 MiB /  75.9 MiB (100 %) 658.9 KiB/s in  1m 52s ETA:  0s
info: downloading component 'rustfmt'
  2.3 MiB /   2.3 MiB (100 %) 689.6 KiB/s in  3s ETA:  0s
info: installing component 'cargo'
info: installing component 'clippy'
info: installing component 'rust-docs'
 17.1 MiB /  17.1 MiB (100 %)   3.7 MiB/s in  4s ETA:  0s
info: installing component 'rust-std'
 21.0 MiB /  21.0 MiB (100 %)  12.0 MiB/s in  1s ETA:  0s
info: installing component 'rustc'
 75.9 MiB /  75.9 MiB (100 %)  12.5 MiB/s in  6s ETA:  0s
info: installing component 'rustfmt'
info: default toolchain set to 'stable-x86_64-apple-darwin'

  stable-x86_64-apple-darwin installed - rustc 1.55.0 (c8dfcfe04 2021-09-06)

Rust is installed now. Great!

To get started you may need to restart your current shell.
This would reload your PATH environment variable to include
Cargo's bin directory ($HOME/.cargo/bin).

To configure your current shell, run:
source $HOME/.cargo/env
```

软链到brew：

```shell
# 配置 rust 软链
app=rust
ver=1.55.0
home=/Users/adolphor/.cargo/bin/

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app

# 配置 cargo-c 软链
app=cargo-c
ver=0.9.4_1
home=/Users/adolphor/.cargo/bin/

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app
```

### ffmpeg
```shell
# https://evermeet.cx/ffmpeg/
app=ffmpeg
ver=4.4
home=/Users/adolphor/Applications/ffmpeg

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app
```

如果想在命令行使用，需要添加环境变量：
```shell
# ffmpeg
export PATH="/usr/local/opt/ffmpeg:$PATH"
```


### openJDK
```
app=openjdk
ver=16.0.2
home=/Library/Java/JavaVirtualMachines/jdk-16.0.2.jdk/Contents/Home

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app
```

## 参考资料
* [Github - cask-upgrade-workflow](https://github.com/NotAlexNoyle/cask-upgrade-workflow)

