package Y2016.M12.D14_proxy.cglibProxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Bob on 2016/12/14.
 */
public class BookFacadeCglib implements MethodInterceptor {

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
  // The callback method
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    System.out.println("The start of something");
    Object result = proxy.invokeSuper(obj, args);
    System.out.println("End of things");
    return result;
  }
}