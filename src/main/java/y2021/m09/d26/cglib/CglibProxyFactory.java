package y2021.m09.d26.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/26 22:39
 * @Email: 0haizhu0@gmail.com
 */
public class CglibProxyFactory {
  public static Object getProxy(Class<?> clazz) {
    // 创建动态代理增强类
    Enhancer enhancer = new Enhancer();
    // 设置类加载器
    enhancer.setClassLoader(clazz.getClassLoader());
    // 设置被代理类
    enhancer.setSuperclass(clazz);
    // 设置方法拦截器
    enhancer.setCallback(new DebugMethodInterceptor());
    // 创建代理类
    return enhancer.create();
  }
}
