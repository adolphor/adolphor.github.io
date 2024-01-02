package y2021.m09.d26.jdk;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/26 17:51
 * @Email: 0haizhu0@gmail.com
 */
public class JdkProxyMainDemo {
  public static void main(String[] args) {
    SmsService smsService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
    smsService.send("java");
  }
}
