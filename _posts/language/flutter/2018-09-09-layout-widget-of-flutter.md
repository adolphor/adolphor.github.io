---
layout:     post
title:      Flutter 组件
date:       2018-09-09 12:05:46 +0800
postId:     2018-09-09-12-05-47
categories: []
keywords:   [App,Flutter]
---

## 基本概念

Flutter Widget采用现代响应式框架构建，这是从 React 中获得的灵感，中心思想是用widget构建你的UI。 Widget描述了他们的视图在给定其当前配
置和状态时应该看起来像什么。当widget的状态发生变化时，widget会重新构建UI，Flutter会对比前后变化的不同， 以确定底层渲染树从一个状态转换
到下一个状态所需的最小更改（译者语：类似于React/Vue中虚拟DOM的diff算法）。

### StatelessWidget & StatefulWidget







## Material Components 

实现了Material Design 指南的视觉、效果、motion-rich的widget。前半部分是App结构、导航和布局，后半部分是基础组件。

### Scaffold

[API文档](https://docs.flutter.io/flutter/material/Scaffold-class.html)

Material Design布局结构的基本实现。此类提供了用于显示drawer、snackbar和底部sheet的API。

### AppBar

[API文档](https://docs.flutter.io/flutter/material/AppBar-class.html)

一个Material Design应用程序栏，由工具栏和其他可能的widget（如TabBar和FlexibleSpaceBar）组成。

### BottomNavigationBar

[API文档](https://docs.flutter.io/flutter/material/BottomNavigationBar-class.html)

底部导航条，可以很容易地在tap之间切换和浏览顶级视图。

### TabBar

[API文档](https://docs.flutter.io/flutter/material/TabBar-class.html)

一个显示水平选项卡的Material Design widget。

### TabBarView

[API文档](https://docs.flutter.io/flutter/material/TabBarView-class.html)

显示与当前选中的选项卡相对应的页面视图。通常和TabBar一起使用。

### MaterialApp

[API文档](https://docs.flutter.io/flutter/material/MaterialApp-class.html)

一个方便的widget，它封装了应用程序实现Material Design所需要的一些widget。

* title ： 在任务管理窗口中所显示的应用名字
  - 这个和启动图标名字是不一样的，和当前 Activity 的名字也是不一样的。 这个 Title 是用来定义任务管理窗口界面所看到应用名字的。
    在原生 Android 系统中点击圆圈 Home 按钮右边的方块按钮就会打开多任务切换窗口。
* theme ： 应用各种 UI 所使用的主题颜色
  - 定义应用所使用的主题颜色，在纸墨设计中定义了 primaryColor、accentColor、hintColor 等颜色值。可以通过这个来指定一个 ThemeData 
    定义应用中每个控件的颜色。
* color ： 应用的主要颜色值（primary color），也就是安卓任务管理窗口中所显示的应用颜色
* home ： 应用默认所显示的界面 Widget
  - 这个是一个 Widget 对象，用来定义当前应用打开的时候，所显示的界面。
* routes ： 应用的顶级导航表格，这个是多页面应用用来控制页面跳转的，类似于网页的网址
  - 定义应用中页面跳转规则。 该对象是一个 Map<String, WidgetBuilder>。
    当使用 Navigator.pushNamed 来路由的时候，会在 routes 查找路由名字，然后使用 对应的 WidgetBuilder 来构造一个带有页面切换动画的 
    MaterialPageRoute。如果应用只有一个界面，则不用设置这个属性，使用 home 设置这个界面即可。
    
    如果 home 不为 null，当 routes 中包含 Navigator.defaultRouteName（'/'） 的时候会出错，两个都是 home 冲突了。
    
    如果所查找的路由在 routes 中不存在，则会通过 onGenerateRoute 来查找。
* initialRoute ：第一个默认显示的路由名字，默认值为 Window.defaultRouteName
* onGenerateRoute ： 生成路由的回调函数，当导航的命名路由的时候，会使用这个来生成界面
* onLocaleChanged ： 当系统修改语言的时候，会触发å这个回调
* navigatorObservers ： 应用 Navigator 的监听器
* debugShowMaterialGrid ： 是否显示 纸墨设计 基础布局网格，用来调试 UI 的工具
* showPerformanceOverlay ： 显示性能标签，https://flutter.io/debugging/#performanceoverlay
* checkerboardRasterCacheImages 、showSemanticsDebugger、debugShowCheckedModeBanner 各种调试开关


Read more: http://blog.chengyunfeng.com/?p=1041#ixzz5QaNaJDNI

### WidgetsApp

[API文档](https://docs.flutter.io/flutter/widgets/WidgetsApp-class.html)

一个方便的类，它封装了应用程序通常需要的一些widget。

### Drawer

[API文档](https://docs.flutter.io/flutter/material/Drawer-class.html)

从Scaffold边缘水平滑动以显示应用程序中导航链接的Material Design面板。

### ListTile

[API文档](https://docs.flutter.io/flutter/material/ListTile-class.html)

一个固定高度的行，通常包含一些文本，以及一个行前或行尾图标。


### Stepper

[API文档](https://docs.flutter.io/flutter/material/Stepper-class.html)

一个Material Design 步骤指示器，显示一系列步骤的过程

### Divider

[API文档](https://docs.flutter.io/flutter/material/Divider-class.html)

一个逻辑1像素厚的水平分割线，两边都有填充

### RaisedButton

[API文档](https://docs.flutter.io/flutter/material/RaisedButton-class.html)

Material Design中的button， 一个凸起的材质矩形按钮

### FlatButton

[API文档](https://docs.flutter.io/flutter/material/FlatButton-class.html)

一个扁平的Material按钮

### ButtonBar

[API文档](https://docs.flutter.io/flutter/material/ButtonBar-class.html)

水平排列的按钮组

### FloatingActionButton

[API文档](https://docs.flutter.io/flutter/material/FloatingActionButton-class.html)

一个圆形图标按钮，它悬停在内容之上，以展示应用程序中的主要动作。FloatingActionButton通常用于Scaffold.floatingActionButton字段。

### IconButton

[API文档](https://docs.flutter.io/flutter/material/IconButton-class.html)

一个Material图标按钮，点击时会有水波动画

### PopupMenuButton

[API文档](https://docs.flutter.io/flutter/material/PopupMenuButton-class.html)

当菜单隐藏式，点击或调用onSelected时显示一个弹出式菜单列表

### Switch

[API文档](https://docs.flutter.io/flutter/material/Switch-class.html)

On/off 用于切换一个单一状态

### Slider

[API文档](https://docs.flutter.io/flutter/material/Slider-class.html)

滑块，允许用户通过滑动滑块来从一系列值中选择。

### SimpleDialog

[API文档](https://docs.flutter.io/flutter/material/SimpleDialog-class.html)

简单对话框可以显示附加的提示或操作

### AlertDialog

[API文档](https://docs.flutter.io/flutter/material/AlertDialog-class.html)

一个会中断用户操作的对话款，需要用户确认

### BottomSheet

[API文档](https://docs.flutter.io/flutter/material/BottomSheet-class.html)

BottomSheet是一个从屏幕底部滑起的列表（以显示更多的内容）。你可以调用showBottomSheet()或showModalBottomSheet弹出

### ExpansionPanel

[API文档](https://docs.flutter.io/flutter/material/ExpansionPanel-class.html)

Expansion panels contain creation flows and allow lightweight editing of an element. The ExpansionPanel widget implements this component.

### SnackBar

[API文档](https://docs.flutter.io/flutter/material/SnackBar-class.html)

具有可选操作的轻量级消息提示，在屏幕的底部显示。

### Chip

[API文档](https://docs.flutter.io/flutter/material/Chip-class.html)

标签，一个Material widget。 它可以将一个复杂内容实体展现在一个小块中，如联系人。

### Tooltip

[API文档](https://docs.flutter.io/flutter/material/Tooltip-class.html)

一个文本提示工具，帮助解释一个按钮或其他用户界面，当widget长时间按下时（当用户采取其他适当操作时）显示一个提示标签。

### LinearProgressIndicator

[API文档](https://docs.flutter.io/flutter/material/LinearProgressIndicator-class.html)

一个线性进度条，另外还有一个圆形进度条CircularProgressIndicator







## Cupertino (iOS风格) Widgets

### CupertinoActivityIndicator

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoActivityIndicator-class.html)

一个iOS风格的loading指示器。显示一个圆形的转圈菊花

### CupertinoAlertDialog

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoAlertDialog-class.html)

iOS风格的alert dialog.

### CupertinoButton

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoButton-class.html)

iOS风格的button.

### CupertinoDialog

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoDialog-class.html)

iOS风格的对话框

### CupertinoDialogAction

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoDialogAction-class.html)

通常用于CupertinoAlertDialog的一个button

### CupertinoSlider

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoSlider-class.html)

从一个范围中选一个值.

### CupertinoSwitch

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoSwitch-class.html)

iOS风格的开关. 用于单一状态的开/关

### CupertinoPageTransition

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoPageTransition-class.html)

提供iOS风格的页面过度动画

### CupertinoFullscreenDialogTransition

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoFullscreenDialogTransition-class.html)

一个iOS风格的过渡，用于调用全屏对话框。

### CupertinoNavigationBar

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoNavigationBar-class.html)

iOS风格的导航栏. 通常和CupertinoPageScaffold一起使用。

### CupertinoTabBar

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoTabBar-class.html)

iOS风格的底部选项卡。 通常和CupertinoTabScaffold一起使用。

### CupertinoPageScaffold

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoPageScaffold-class.html)

一个iOS风格的页面的基本布局结构。包含内容和导航栏

### CupertinoTabScaffold

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoTabScaffold-class.html)

标签式iOS应用程序的结构。将选项卡栏放在内容选项卡之上

### CupertinoTabView

[API文档](https://docs.flutter.io/flutter/cupertino/CupertinoTabView-class.html)

支持选项卡间并行导航项卡的根内容。通常与CupertinoTabScaffolde一起使用







## 布局 Widget

### Container

[API文档](https://docs.flutter.io/flutter/widgets/Container-class.html)

`Container` 可让您创建矩形视觉元素。container 可以装饰为一个 [`BoxDecoration`](https://docs.flutter.io/flutter/painting/BoxDecoration-class.html), 
如 background、一个边框、或者一个阴影。 `Container` 也可以具有边距（margins）、填充(padding)和应用于其大小的约束(constraints)。
另外， `Container`可以使用矩阵在三维空间中对其进行变换。


### Row 

[API文档](https://docs.flutter.io/flutter/widgets/Row-class.html)

在水平方向上排列子widget的列表。`Row` & `Column` ，这些具有弹性空间的布局类Widget可让您在水平（Row）和垂直（Column）方向上创建灵活的
布局。其设计是基于web开发中的Flexbox布局模型。

### Column

[API文档](https://docs.flutter.io/flutter/widgets/Column-class.html)

在垂直方向上排列子widget的列表。

### Stack

[API文档](https://docs.flutter.io/flutter/widgets/Stack-class.html)

取代线性布局 (译者语：和Android中的LinearLayout相似)，`Stack` 允许子 widget 堆叠， 你可以使用 [`Positioned`](https://docs.flutter.io/flutter/widgets/Positioned-class.html)
来定位他们相对于Stack的上下左右四条边的位置。Stacks 是基于Web开发中的绝度定位（absolute positioning )布局模型设计的。 

### IndexedStack

[API文档](https://docs.flutter.io/flutter/widgets/IndexedStack-class.html)

从一个子widget列表中显示单个孩子的Stack

### Flow

[API文档](https://docs.flutter.io/flutter/widgets/Flow-class.html)

一个实现流式布局算法的widget

### Table

[API文档](https://docs.flutter.io/flutter/widgets/Table-class.html)

为其子widget使用表格布局算法的widget

### Wrap

[API文档](https://docs.flutter.io/flutter/widgets/Wrap-class.html)

可以在水平或垂直方向多行显示其子widget。

### ListBody

[API文档](https://docs.flutter.io/flutter/widgets/ListBody-class.html)

一个widget，它沿着一个给定的轴，顺序排列它的子元素

### ListView

[API文档](https://docs.flutter.io/flutter/widgets/ListView-class.html)

可滚动的列表控件。ListView是最常用的滚动widget，它在滚动方向上一个接一个地显示它的孩子。在纵轴上，孩子们被要求填充ListView。
对于少量固定的子元素，可以直接一次性全部渲染，对于大量子元素的list，则需要使用build方法：

```dart

// 少量的List元素可以一次性渲染
new ListView(
  children: <Widget>[
    new ListTile(
      leading: new Icon(Icons.map),
      title: new Text('Map'),
    ),
    new ListTile(
      leading: new Icon(Icons.photo),
      title: new Text('Album'),
    ),
    new ListTile(
      leading: new Icon(Icons.phone),
      title: new Text('Phone'),
    ),
  ],
),

// 大量子元素的时候要使用build方法：
new ListView.builder(
  // item数量
  itemCount: widget.items.length,
  itemBuilder: (context, index) {
    return new ListTile(
      title: new Text('${widget.items[index]}'),
    );
  },
),
```


### CustomMultiChildLayout

[API文档](https://docs.flutter.io/flutter/widgets/CustomMultiChildLayout-class.html)

使用一个委托来对多个孩子进行设置大小和定位的小部件

### Padding

[API文档](https://docs.flutter.io/flutter/widgets/Padding-class.html)

一个widget, 会给其子widget添加指定的填充

### Center

[API文档](https://docs.flutter.io/flutter/widgets/Center-class.html)

将其子widget居中显示在自身内部的widget

### Align

[API文档](https://docs.flutter.io/flutter/widgets/Align-class.html)

一个widget，它可以将其子widget对齐，并可以根据子widget的大小自动调整大小。

### Baseline

[API文档](https://docs.flutter.io/flutter/widgets/Baseline-class.html)

根据子项的基线对它们的位置进行定位的widget。

### FittedBox

[API文档](https://docs.flutter.io/flutter/widgets/FittedBox-class.html)

按自己的大小调整其子widget的大小和位置。

### AspectRatio

[API文档](https://docs.flutter.io/flutter/widgets/AspectRatio-class.html)

一个widget，试图将子widget的大小指定为某个特定的长宽比

### ConstrainedBox

[API文档](https://docs.flutter.io/flutter/widgets/ConstrainedBox-class.html)

对其子项施加附加约束的widget

### FractionallySizedBox

[API文档](https://docs.flutter.io/flutter/widgets/FractionallySizedBox-class.html)

一个widget，它把它的子项放在可用空间的一小部分。关于布局算法的更多细节，见RenderFractionallySizedOverflowBox

### IntrinsicHeight

[API文档](https://docs.flutter.io/flutter/widgets/IntrinsicHeight-class.html)

一个widget，它将它的子widget的高度调整其本身实际的高度

### IntrinsicWidth

[API文档](https://docs.flutter.io/flutter/widgets/IntrinsicWidth-class.html)

一个widget，它将它的子widget的宽度调整其本身实际的宽度

### LimitedBox

[API文档](https://docs.flutter.io/flutter/widgets/LimitedBox-class.html)

一个当其自身不受约束时才限制其大小的盒子

### Offstage

[API文档](https://docs.flutter.io/flutter/widgets/Offstage-class.html)

一个布局widget，可以控制其子widget的显示和隐藏。

### OverflowBox

[API文档](https://docs.flutter.io/flutter/widgets/OverflowBox-class.html)

对其子项施加不同约束的widget，它可能允许子项溢出父级。

### SizedBox

[API文档](https://docs.flutter.io/flutter/widgets/SizedBox-class.html)

一个特定大小的盒子。这个widget强制它的孩子有一个特定的宽度和高度。如果宽度或高度为NULL，则此widget将调整自身大小以匹配该维度中的孩子的大小。

### SizedOverflowBox

[API文档](https://docs.flutter.io/flutter/widgets/SizedOverflowBox-class.html)

一个特定大小的widget，但是会将它的原始约束传递给它的孩子，它可能会溢出。

### Transform

[API文档](https://docs.flutter.io/flutter/widgets/Transform-class.html)

在绘制子widget之前应用转换的widget。

### CustomSingleChildLayout

[API文档](https://docs.flutter.io/flutter/widgets/CustomSingleChildLayout-class.html)

一个自定义的拥有单个子widget的布局widget

### Theme

[API文档](https://docs.flutter.io/flutter/material/Theme-class.html)

将主题应用于子widget。主题描述了应用选择的颜色和字体。

### MediaQuery

[API文档](https://docs.flutter.io/flutter/widgets/MediaQuery-class.html)

建立一个子树，在树中媒体查询解析不同的给定数据







## 可滚动的Widget

滚动一个拥有多个子组件的父组件

### ListView

参见 [布局 Widget - ListView](#listview)

### NestedScrollView

[API文档](https://docs.flutter.io/flutter/widgets/NestedScrollView-class.html)

一个可以嵌套其它可滚动widget的widget

### GridView

[API文档](https://docs.flutter.io/flutter/widgets/GridView-class.html)

一个可滚动的二维空间数组

### SingleChildScrollView

[API文档](https://docs.flutter.io/flutter/widgets/SingleChildScrollView-class.html)

有一个子widget的可滚动的widget，子内容超过父容器时可以滚动。

### Scrollable

[API文档](https://docs.flutter.io/flutter/widgets/Scrollable-class.html)

实现了可滚动widget的交互模型，但不包含UI显示相关的逻辑

### Scrollbar

[API文档](https://docs.flutter.io/flutter/material/Scrollbar-class.html)

一个Material Design 滚动条，表示当前滚动到了什么位置

### CustomScrollView

[API文档](https://docs.flutter.io/flutter/widgets/CustomScrollView-class.html)

一个使用slivers创建自定义的滚动效果的ScrollView

### NotificationListener

[API文档](https://docs.flutter.io/flutter/widgets/NotificationListener-class.html)

一个用来监听树上冒泡通知的widget。

### ScrollConfiguration

[API文档](https://docs.flutter.io/flutter/widgets/ScrollConfiguration-class.html)

控制可滚动组件在子树中的表现行为

### RefreshIndicator

[API文档](https://docs.flutter.io/flutter/material/RefreshIndicator-class.html)

Material Design下拉刷新指示器，包装一个可滚动widget






## 基本元素组件

### Text

[API文档](https://docs.flutter.io/flutter/widgets/Text-class.html)

该 widget 可让创建一个带格式的文本。

### RichText

[API文档](https://docs.flutter.io/flutter/widgets/RichText-class.html)

一个富文本Text，可以显示多种样式的text。

### DefaultTextStyle

[API文档](https://docs.flutter.io/flutter/widgets/DefaultTextStyle-class.html)

文字样式，用于指定Text widget的文字样式

### Image

[API文档](https://docs.flutter.io/flutter/widgets/Image-class.html)

一个显示图片的widget

* 显示网络图片

  - 可以是静态图片
    ```dart
    new Image.network(
      'https://raw.githubusercontent.com/NorthFacing/adolphor/gh-pages{{ site.baseurl }}/image/post/2018/09/09/demo_senlin.jpg',
    ),
    ```
  - 也可以是gif动态图片
    ```dart
    new Image.network(
      'https://raw.githubusercontent.com/NorthFacing/adolphor/gh-pages{{ site.baseurl }}/image/post/2018/09/09/demo_coffee.gif',
    ),
    ``` 

### RawImage

[API文档](https://docs.flutter.io/flutter/widgets/RawImage-class.html)

一个直接显示dart:ui.Image的widget

### 3th - FadeInImage

需要增加依赖包，在添加如下依赖：

```yaml
transparent_image: ^0.1.0
```

之后运行如下指令：
```shell
flutter packages get
```
按照如下方式使用即可：
```dart
new FadeInImage.memoryNetwork(
  placeholder: kTransparentImage,
  image: 'https://raw.githubusercontent.com/NorthFacing/adolphor/gh-pages{{ site.baseurl }}/image/post/2018/09/09/demo_senlin.jpg',
),
```

### Icon
[API文档](https://docs.flutter.io/flutter/widgets/Icon-class.html)

A Material Design icon.

### AssetBundle

[API文档](https://docs.flutter.io/flutter/services/AssetBundle-class.html)

包含应用程序可以使用的资源，如图像和字符串。对这些资源的访问是异步，所以他们可以来自网络（例如，从NetworkAssetBundle）或从本地文件系统，
这并不会挂起用户界面。

### FlutterLogo

[API文档](https://docs.flutter.io/flutter/material/FlutterLogo-class.html)

Flutter logo, 以widget形式. 这个widget遵从IconTheme。

### Form

[API文档](https://docs.flutter.io/flutter/widgets/Form-class.html)

一个可选的、用于给多个TextField分组的widget

### FormField

[API文档](https://docs.flutter.io/flutter/widgets/FormField-class.html)

一个单独的表单字段。此widget维护表单字段的当前状态，以便在UI中直观地反映更新和验证错误。

### RawKeyboardListener

[API文档](https://docs.flutter.io/flutter/widgets/RawKeyboardListener-class.html)

每当用户按下或释放键盘上的键时调用回调的widget。

### Placeholder

[API文档](https://docs.flutter.io/flutter/widgets/Placeholder-class.html)

一个绘制了一个盒子的的widget，代表日后有widget将会被添加到该盒子中

### TextField

[API文档](https://docs.flutter.io/flutter/material/TextField-class.html)

文本输入框

### Checkbox

[API文档](https://docs.flutter.io/flutter/material/Checkbox-class.html)

复选框，允许用户从一组中选择多个选项。

### Radio

[API文档](https://docs.flutter.io/flutter/material/Radio-class.html)

单选框，允许用户从一组中选择一个选项。

### showDatePicker

[API文档](https://docs.flutter.io/flutter/material/showDatePicker.html)

日期&时间选择器

### Card

[API文档](https://docs.flutter.io/flutter/material/Card-class.html)

一个 Material Design 卡片。拥有一个圆角和阴影

### DataTable

[API文档](https://docs.flutter.io/flutter/material/DataTable-class.html)

数据表显示原始数据集。它们通常出现在桌面企业产品中。DataTable Widget实现这个组件







## 交互模型Widget



### LongPressDraggable

[API文档](https://docs.flutter.io/flutter/widgets/LongPressDraggable-class.html)

可以使其子widget在长按时可拖动

### GestureDetector

[API文档](https://docs.flutter.io/flutter/widgets/GestureDetector-class.html)

一个检测手势的widget


### DragTarget

[API文档](https://docs.flutter.io/flutter/widgets/DragTarget-class.html)

一个拖动的目标widget，在完成拖动时它可以接收数据

### Dismissible

[API文档](https://docs.flutter.io/flutter/widgets/Dismissible-class.html)

可以在拖动时隐藏的widget


### IgnorePointer

[API文档](https://docs.flutter.io/flutter/widgets/IgnorePointer-class.html)

在hit test中不可见的widget。当ignoring为true时，此widget及其子树不响应事件。但它在布局过程中仍然消耗空间，并像往常一样绘制它的孩子。
它是无法捕获事件对象、因为它在RenderBox.hitTest中返回false

### AbsorbPointer

[API文档](https://docs.flutter.io/flutter/widgets/AbsorbPointer-class.html)

在hit test期间吸收(拦截)事件。当absorbing为true时，此小部件阻止其子树通过终止命中测试来接收指针事件。它在布局过程中仍然消耗空间，
并像往常一样绘制它的孩子。它只是防止其孩子成为事件命中目标，因为它从RenderBox.hitTest返回true。

### Navigator

[API文档](https://docs.flutter.io/flutter/widgets/Navigator-class.html)

导航器，可以在多个页面(路由)栈之间跳转。

### Scrollable

参考[可滚动的Widget - Scrollable](#scrollable)


## 动画&Motion Widget

在您的应用中使用动画。查看Flutter中的[动画总览](https://flutterchina.club/animations/)

[TODO](https://flutterchina.club/widgets/animation/)

## 绘制和视觉效果Widget

Widget将视觉效果应用到其子组件，而不改变它们的布局、大小和位置。

[TODO](https://flutterchina.club/widgets/painting/)

## Async

Flutter应用的异步模型

### FutureBuilder

[API文档](https://docs.flutter.io/flutter/widgets/FutureBuilder-class.html)

基于与Future交互的最新快照来构建自身的widget

### StreamBuilder

[API文档](https://docs.flutter.io/flutter/widgets/StreamBuilder-class.html)

基于与流交互的最新快照构建自身的widget




## 参考资料

* [Flutter中文网 - 基础 Widgets](https://flutterchina.club/widgets/)
* [Flutter样式和布局控件简析(一)](https://segmentfault.com/a/1190000011949751)
* [Flutter样式和布局控件简析(二)](https://segmentfault.com/a/1190000015086603)
* [Flutter中文开发者论坛](http://flutter-dev.cn/)
