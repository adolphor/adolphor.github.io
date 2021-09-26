package y2021.m09.d26.jdk;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/26 17:50
 * @Email: 0haizhu0@gmail.com
 */
public class SmsServiceImpl implements SmsService {
  public String send(String message) {
    System.out.println("send message: " + message);
    return message;
  }
}