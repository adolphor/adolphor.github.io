package y2021.m09.d26.jdk;

import java.lang.reflect.Proxy;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/26 17:49
 * @Email: 0haizhu0@gmail.com
 */
public class JdkProxyFactory {
  public static Object getProxy(Object target) {
    return Proxy.newProxyInstance(
      target.getClass().getClassLoader(), // 目标类的类加载
      target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
      new DebugInvocationHandler(target)   // 代理对象对应的自定义 InvocationHandler
    );
  }
}
