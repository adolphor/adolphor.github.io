package y2021.m10.d01;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/2 00:15
 * @Email: 0haizhu0@gmail.com
 */
public class MyClassLoaderMainDemo {
  public static void main(String[] args) throws ClassNotFoundException {
    MyClassLoader myClassLoader = new MyClassLoader("src/main/java/y2021/m10/d01/");
    Class<?> aClass = myClassLoader.findClass("MyClassLoader");
    System.out.println(aClass.getName());
    System.out.println(myClassLoader.getParent());
  }
}
