---
layout:     post
title:      持续集成：travis-ci
date:       2018-12-11 11:11:01 +0800
postId:     2018-12-11-11-11-01
categories: [Microservice]
keywords:   [Microservice, travis-ci]
---

## Travis CI 使用条件和方法

* 必须配合GitHub来使用
* 登录 https://travis-ci.org 使用GitHub账号授权登录
* 添加 `.travis.yml` 配置文件到需要使用的项目
* 在 travis-ci.com 查看运行状态
* macOS的话使用 `gem install travis` 安装 travis

## 生命周期

### Builds, Jobs, Stages 和 Phases 基础概念 

* 阶段（phase） 
  - 某个job中的一系列步骤(step)。比如，安装阶段，在脚本执行阶段之前，脚本执行阶段在可选的部署阶段之前。
* 任务（Job）
  - 将仓库代码拷贝到一个虚拟环境，然后执行一系列 phase，比如编译、测试等，如果某个phase返回值不为0，则job失败
* 构造（build）
  - 一组job。比如，一次构造包含两个任务，每个任务测试编程语言的一个版本。只有当所有job都执行完毕的时候，build才算执行完毕。
* stage
  - a group of jobs that run in parallel as part of sequential build process composed of multiple stages.
  - 怎么翻译？作为由多个阶段组成的顺序构建过程的一部分并行运行的一组作业？
 
### The job lifecycle

Travis CI 中的一个job主要分为两部分：

* install: 安装所有的依赖包或文件
* script: 运行构造脚本

可以在安装阶段（before_install），或在此阶段之前（before_script）或之后（after_script）运行自定义命令。

可以在构造成功（after_success）或失败（after_failure）阶段之后添加额外的操作，比如构造文档，上传日志文件等。
不管成功或失败，都可以使用 `$TRAVIS_TEST_RESULT` 环境变量获取构造结果。

完整的生命周期如下：

* OPTIONAL Install `apt addons`
* OPTIONAL Install `cache components`
* `before_install`
* `install`
* `before_script`
* `script`
* OPTIONAL `before_cache` (for cleaning up cache)
* `after_success` or `after_failure`
* OPTIONAL `before_deploy`
* OPTIONAL `deploy`
* OPTIONAL `after_deploy`
* `after_script`

## 配置详解

### 自定义安装阶段

* 可以直接配置脚本文件，但必须是可执行sh文件且包含组织行（shebang line）：`/usr/bin/env sh`, `/usr/bin/env ruby`, 或`/usr/bin/env python`.

  ```yaml
  install: ./install-dependencies.sh
  ```
* 也可以配置多步骤
  
  ```yaml
  - bundle install --path vendor/bundle
  - npm install
  ```
* 跳过安装阶段
  ```yaml
  install: true
  ```

### 自定义构造阶段

* 单个指令
  ```yaml
  cript: bundle exec thor build
  ```

* 多指令形式
  ```yaml
  script:
  - bundle exec rake build
  - bundle exec rake builddoc
  ```
* 合并形式
  ```yaml
  script: bundle exec rake build && bundle exec rake builddoc
  ```

## 其他

### macOS下配置
```yaml
os: osx

before_install:
  - brew install git-lfs

before_script:
  - git lfs pull
```

### 支持的语言

* ruby
* java
* node_js
* python
* php

配置范例：
```yaml
language: ruby
```


## 参考资料

* [travis-ci 官方文档](https://docs.travis-ci.com)
* [travis org](https://travis-ci.org)
* [从Travis回GitHub](https://www.jianshu.com/p/5f96c27baaa5)
* [从GitHub到Travis](https://www.jianshu.com/p/c80b37f775a0)
