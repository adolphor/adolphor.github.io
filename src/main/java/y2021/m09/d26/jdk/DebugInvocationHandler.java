package y2021.m09.d26.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/26 17:07
 * @Email: 0haizhu0@gmail.com
 */
public class DebugInvocationHandler<T> implements InvocationHandler {

  /**
   * 代理类中的真实对象
   */
  private final T target;

  public DebugInvocationHandler(T target) {
    this.target = target;
  }

  /**
   * @param proxy  动态生成的代理类
   * @param method 与代理类对象调用的方法相对应
   * @param args   当前 method 方法的参数
   * @return 原方法返回值
   * @throws Throwable
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //调用方法之前，可以添加自己的操作
    System.out.println("before method " + method.getName());
    Object result = method.invoke(target, args);
    //调用方法之后，同样可以添加自己的操作
    System.out.println("after method " + method.getName());
    return result;
  }
}
