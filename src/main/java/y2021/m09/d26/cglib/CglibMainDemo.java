package y2021.m09.d26.cglib;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/26 22:39
 * @Email: 0haizhu0@gmail.com
 */
public class CglibMainDemo {
  public static void main(String[] args) {
    AliSmsService aliSmsService = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
    aliSmsService.send("java");
  }
}
