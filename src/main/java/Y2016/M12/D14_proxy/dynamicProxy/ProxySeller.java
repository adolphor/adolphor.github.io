package Y2016.M12.D14_proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxySeller implements InvocationHandler {

  private Object target;

  public Object getInstance(Object target) {
    this.target = target;
    //Get the proxy object
    return Proxy.newProxyInstance(
      target.getClass().getClassLoader(),
      target.getClass().getInterfaces(),
      this
    );//To bind interface (this is a defect, cglib made up for this shortcoming)
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    before();
    int price = (int) args[0];
    Object result = method.invoke(target, price - 5);
    after();
    return result;
  }

  private void before() {
    System.out.println("ProxySeller 代理收取手续费 $5");
  }

  private void after() {
    System.out.println("ProxySeller 代理完成");
  }
}


