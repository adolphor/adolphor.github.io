---
layout:     post
title:      各种开源协议的区别和选择
date:       2017-03-21 10:01:02 +0800
postId:     2017-03-21-10-01-02
categories: []
keywords:   [CS]
---

## 各协议授权详情

先来了解一些下相关用词的解释：

* 协议和版权信息(License and copyright notice)：在代码中保留作者提供的协议和版权信息
* 声明变更(State Changes)：在代码中声明对原来代码的重大修改及变更
* 公开源码(Disclose Source)：代码必需公开。如果是基于LGPL协议 下，则只需使用的开源代码公开，不必将整个软件源码公开
* 库引用(Library usage)：该库可以用于商业软件中
* 责任承担(Hold Liable)：代码的作者承担代码使用后的风险及产生的后果
* 商标使用(Use Trademark)：可以使用作者的姓名，作品的Logo，或商标
* 附加协议(Sublicensing)：允许在软件分发传播过程中附加上原来没有的协议条款等

<table>
    <thead>
    <tr>
        <td width="14%">协议</td>
        <td width="30%">描述</td>
        <td width="20%">要求</td>
        <td width="18%">允许</td>
        <td width="18%">禁止</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
            Apache
        </td>
        <td>
            一个较宽松且简明地指出了专利授权的协议。
        </td>
        <td>
            <ul>
                <li>协议和版权信息</li>
                <li>声明变更</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>专利授权</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担（禁止让作者承担责任，可以理解为免责）</li>
                <li>商标使用</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            GPL
        </td>
        <td>
            此协议是应用最为广泛的开源协议，拥有较强的版权自由( copyleft )要求。衍生代码的分发需开源并且也要遵守此协议。
            此协议有许多变种，不同变种的要求略有不同。
        </td>
        <td>
            <ul>
                <li>公开源码</li>
                <li>协议和版权信息</li>
                <li>声明变更</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>专利授权</li>
                <li>私用</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
                <li>附加协议</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            MIT
        </td>
        <td>
            宽松简单且精要的一个协议。在适当标明来源及免责的情况下，它允许你对代码进行任何形式的使用。
        </td>
        <td>
            <ul>
                <li>协议和版权信息</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            Artistic
        </td>
        <td>
            Perl社区尤为钟爱此协议。要求更改后的软件不能影响原软件的使用。
        </td>
        <td>
            <ul>
                <li>协议和版权信息</li>
                <li>声明变更</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
                <li>商标使用</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            BSD
        </td>
        <td>
            较为宽松的协议，包含两个变种BSD 2-Clause 和BSD 3-Clause，两者都与MIT协议只存在细微差异。
        </td>
        <td>
            <ul>
                <li>协议和版权信息</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            Eclipse
        </td>
        <td>
            对商用非常友好的一种协议，可以用于软件的商业授权。包含对专利的优雅授权，并且也可以对相关代码应用商业协议。
        </td>
        <td>
            <ul>
                <li>公开源码</li>
                <li>协议和版权信息</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>专利授权</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            LGPL
        </td>
        <td>
            主要用于一些代码库。衍生代码可以以此协议发布（言下之意你可以用其他协议），但与此协议相关的代码必需遵循此协议。
        </td>
        <td>
            <ul>
                <li>公开源码</li>
                <li>库引用</li>
                <li>协议和版权信息</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>专利授权</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            Mozilla
        </td>
        <td>
            Mozilla Public License(MPL 2.0)是由Mozilla基金创建维护的。此协议旨在较为宽松的BSD协议和更加互惠的GPL协议中寻找一个折衷点。
        </td>
        <td>
            <ul>
                <li>公开源码</li>
                <li>协议和版权信息</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>专利授权</li>
                <li>私用</li>
                <li>附加协议</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
                <li>商标使用</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            No license
        </td>
        <td>
            你保留所有权利，不允许他人分发，复制或者创造衍生物。当你将代码发表在一些网站上时需要遵守该网站的协议，
            此协议可能包含了一些对你劳动成果的授权许可。比如你将代码发布到GitHub，那么你就必需同意别人可以查看和Fork你的代码。
        </td>
        <td>
            <ul>
                <li>协议和版权信息</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>私用</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>分发</li>
                <li>修改</li>
                <li>附加协议</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>
            Public domain dedication
        </td>
        <td>
            在许多国家，默认版权归作者自动拥有，所以Unlicense协议提供了一种通用的模板，此协议表明你放弃版权，
            将劳动成果无私贡献出来。你将丧失对作品的全部权利，包括在MIT/X11中定义的无担保权利。
        </td>
        <td>
            <ul>
                <li>N/A</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>商用</li>
                <li>分发</li>
                <li>修改</li>
                <li>私用</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>责任承担</li>
            </ul>
        </td>
    </tr>
    </tbody>
</table>

## 协议图解

如下的如表更容易清晰的讲解常用协议区别：

![常用协议]({{ site.baseurl }}/image/post/2017/03/21/20170321-0101.jpg)


下面还有个恶搞版本的协议说明：

![恶搞版]({{ site.baseurl }}/image/post/2017/03/21/20170321-0102.png)

## 协议选择

### 代码

进行开源，别人可以用来做任何想要做的事，可以修改之后进行闭源，但是需要保持版本说明。

我选择的是：MIT

### 文章

署名、非商业使用、进制演绎，

选择的是：[创意共享3.0](https://creativecommons.org/licenses/by-nc-nd/3.0/deed.zh)

## 参考资料

* [如何选择开源许可证？](http://www.ruanyifeng.com/blog/2011/05/how_to_choose_free_software_licenses.html)
* [如何为你的代码选择一个开源协议](http://www.cnblogs.com/Wayou/p/how_to_choose_a_license.html)
