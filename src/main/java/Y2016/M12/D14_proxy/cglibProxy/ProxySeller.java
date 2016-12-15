package Y2016.M12.D14_proxy.cglibProxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxySeller implements MethodInterceptor {

  private Object target;

  public Object getInstance(Object target) {
    this.target = target;
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(this.target.getClass());
    // The callback method
    enhancer.setCallback(this);
    // Create a proxy object
    Object o = enhancer.create();
    return o;
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    before();
    int price = (int) args[0];
    Object result = proxy.invokeSuper(obj, new Object[]{price - 5});
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


