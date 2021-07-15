

* [Jupyter Notebook](https://zhuanlan.zhihu.com/p/131007870)
* [AI Studio快速使用教程](https://aistudio.baidu.com/aistudio/projectdetail/41385)

## Jupyter Notebook

百度的AIStudio教学使用了 Jupyter Notebook，所以必须了解其相关使用方法。

Jupyter Notebook（此前被称为 IPython notebook）是一个交互式笔记本，支持运行 40 多种编程语言。
Jupyter Notebook 的本质是一个 Web 应用程序，便于创建和共享文学化程序文档，支持实时代码，数学方程，可视化和 markdown。 用途包括：数据清理和转换，数值模拟，统计建模，机器学习等等。
在AI Studio平台使用时，接触到的Notebook作为主要编辑器。因此在AI Studio上操作时，notebook的熟练使用尤为关键。

* Jupyter Notebook 的笔记可以同时包含 `code` 和 `markdown`，其中 code 可以直接运行，相当于在线IDE
* 

## 运行第一个项目：波士顿房价预测任务

![深度学习流程](image度学习流程.png)

#### 数据处理
数据处理包含五个部分：
* 数据导入：
    - 使用到的python库：numpy，json
        - [NumPy 教程](https://www.runoob.com/numpy/numpy-tutorial.html)
    - 数据地址：./work/housing.data
    - ？数据格式是怎样的，如果自己准备数据，如何抽取为data文件？
    - 除了 data，还支持怎样的原始数据
* 数据形状变换
    - 调用numpy库的reshape方法，改为二维的数据结构
* 数据集划分
    - 将80%的数据用作训练集，20%用作测试集
* 数据归一化处理
    - 对每个特征进行归一化处理，使得每个特征的取值缩放到0~1之间。这样做有两个好处：
        - 一是模型训练更高效；
        - 二是特征前的权重大小可以代表该变量对预测结果的贡献度（因为每个特征值本身的范围相同）。
* 封装load data函数
    - 将上面所有的操作封装为 load_data() 函数，直接调用
* 模型设计
    - 模型设计是深度学习模型关键要素之一，也称为网络结构设计，相当于模型的假设空间，即实现模型“前向计算”（从输入到输出）的过程。
    






