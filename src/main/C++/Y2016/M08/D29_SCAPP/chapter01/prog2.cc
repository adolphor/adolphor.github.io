#include <iostream>     // 文件头，引用iostream库
int main() {
    std::cout << "Enter two numbers:" << std::endl; // 链式操作，等价于
    // std::cout << "Enter two numbers:";
    // std::cout << std::endl; // endl 叫做 manipulator，进行flush操作
    int v1 = 0, v2 = 0;
    std::cin >> v1 >> v2;   // 接收两个参数，分别赋值到 v1 和 v2，等价于
    // std::cin >> v1;
    // std::cin >> v2;
    std::cout << "The sum of " << v1 << " and " << v2
    << " is " << v1 + v2 << std::endl;
    return 0;
}
