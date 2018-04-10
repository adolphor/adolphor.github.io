

## _sass
* _bootstrap-min.scss

> bootStrap 官方文件，但是将CSS文件改为scss文件，为了extends.scss可以继承使用

* _layout.scss

> 框架部分

* _extends.scss

> 继承bootstrap文件，这样可以在不改变bootstrap样式的情况下使用bootstrap类

* _syntax-highlighting.scss

> 代码高亮


## table 自定义样式

可以在文章正文顶部直接嵌入CSS样式：
```
<style>
table tr th:first-child {
    width: 10%;
    text-align: center;
}
table tr td:first-child {
    width: 10%;
    text-align: center;
}
</style>
```