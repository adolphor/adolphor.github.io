---
layout:     post
title:      Linux系统权限控制基础
date:       2022-12-01 11:25:55 +0800
postId:     2022-12-01-11-25-55
categories: [Linux]
keywords:   [Linux]
---

Linux系统上对文件的权限有着严格的控制，如果想对某个文件执行某种操作，必须具有对应的权限方可执行成功。
权限类型一般包括读、写、执行，对应字母为 r、w、x。
权限的粒度有 拥有者、群组、其它组 三种。每个文件都可以针对三个粒度，设置不同的rwx(读写执行)权限。
通常情况下，一个文件只能归属于一个用户和组，如果其它的用户想有这个文件的权限，则可以将该用户加入具备权限的群组，
一个用户可以同时归属于多个组。

## 十位权限表示
也就是`十位二进制表示法`，
`- rwx rwx rwx`，或者 `- 111 111 111`：
* 最高位：`-`，
* 第2-4位：`拥有者`权限
* 第5-7位：`群组`权限
* 第8-10位：`其它组`权限

### 对应关系

```
r-- = 100
-w- = 010
--x = 001
--- = 000
```
转换成八进制数，则为 r=4, w=2, x=1, -=0

权限数值定义：
* 数字 4：读权限
* 数字 2：写权限
* 数字 1：执行权限

实际上，我们可以将所有的权限用二进制形式表现出来，并进一步转变成八进制数字：
```
rwx = 111 = 7
rw- = 110 = 6
r-x = 101 = 5
r-- = 100 = 4
-wx = 011 = 3
-w- = 010 = 2
--x = 001 = 1
--- = 000 = 0
```

即：
* 若要同时设置 rwx (可读写运行） 权限则将该权限位 设置 为 4 + 2 + 1 = 7
* 若要同时设置 rw- （可读写不可运行）权限则将该权限位 设置 为 4 + 2 = 6
* 若要同时设置 r-x （可读可运行不可写）权限则将该权限位 设置 为 4 +1 = 5

### 常见表示

```
-rw------- (600)    只有拥有者有读写权限。
-rw-r--r-- (644)    只有拥有者有读写权限；而属组用户和其他用户只有读权限。
-rwx------ (700)    只有拥有者有读、写、执行权限。
-rwxr-xr-x (755)    拥有者有读、写、执行权限；而属组用户和其他用户只有读、执行权限。
-rwx--x--x (711)    拥有者有读、写、执行权限；而属组用户和其他用户只有执行权限。
-rw-rw-rw- (666)    所有用户都有文件读、写权限。
-rwxrwxrwx (777)    所有用户都有读、写、执行权限。
```

### 使用方式

上面我们提到，每个文件都可以针对三个粒度，设置不同的rwx(读写执行)权限。
即我们可以用用三个8进制数字分别表示 拥有者 、群组 、其它组( u、 g 、o)的权限详情，
并用chmod直接加三个8进制数字的方式直接改变文件权限。语法格式为 ：
```shell
chmod <abc> file...
```

## 更改文件拥有者
linux/Unix 是多人多工作业系统，每个的文件都有拥有者（所有者），如果我们想变更文件的拥有者
（利用 chown 将文件拥有者加以改变），一般只有系统管理员(root)拥有此操作权限，
而普通用户则没有权限将自己或者别人的文件的拥有者设置为别人。
```shell
chown [可选项] user[:group] file...
```

## 帮助手册
```shell
chmod -help
```
```
Usage: chmod [OPTION]... MODE[,MODE]... FILE...
  or:  chmod [OPTION]... OCTAL-MODE FILE...
  or:  chmod [OPTION]... --reference=RFILE FILE...
Change the mode of each FILE to MODE.
With --reference, change the mode of each FILE to that of RFILE.

  -c, --changes          like verbose but report only when a change is made
  -f, --silent, --quiet  suppress most error messages
  -v, --verbose          output a diagnostic for every file processed
      --no-preserve-root  do not treat '/' specially (the default)
      --preserve-root    fail to operate recursively on '/'
      --reference=RFILE  use RFILE's mode instead of MODE values
  -R, --recursive        change files and directories recursively
      --help     display this help and exit
      --version  output version information and exit

Each MODE is of the form '[ugoa]*([-+=]([rwxXst]*|[ugo]))+|[-+=][0-7]+'.

GNU coreutils online help: <http://www.gnu.org/software/coreutils/>
For complete documentation, run: info coreutils 'chmod invocation'
```

## 范例

### 范例1
```shell
# 设置所有用户可读取文件 a.conf：
chmod ugo+r a.conf 
chmod a+r  a.conf
# 设置 c.sh 只有 拥有者可以读写及执行
chmod u+rwx c.sh
# 设置文件 a.conf 与 b.xml 权限为拥有者与其所属同一个群组 可读写，其它组可读不可写
chmod a+r,ug+w,o-w a.conf b.xml
# 设置当前目录下的所有档案与子目录皆设为任何人可读写
chmod -R a+rw *
```

### 范例2
所有人可以读写及执行：
```shell
chmod 777 file
# 等价于
chmod u=rwx,g=rwx,o=rwx file
# 等价于
chmod a=rwx file
```

### 范例3
设置拥有者可读写，其他人不可读写执行：
```shell
chmod 600 file
# 等价于
chmod u=rw,g=---,o=--- file
# 等价于
chmod u=rw,go-rwx file )
```


## 参考资料
* [Linux系统权限控制基础]({% post_url system/linux/2022-12-01-02-linux-system-permission %})
* [Linux权限详解](https://blog.csdn.net/u013197629/article/details/73608613)

```
![image-alter]({{ site.baseurl }}/image/post/2022/12/01/02/xxx.jpg)
```
