package y2020.m01.d23.listener.v5;

/**
 * @author adolphor
 */
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    // 初始化主题
    ConcreteSubject subject = new ConcreteSubject();
    // 将监听者添加到通知列表
    subject.attach(new Observer() {
      @Override
      public void update(Object msg) {
        System.out.println(msg);
      }
    });
    // 当主题发生变化或者需要通知监听者的时候进行全量通知
    subject.changeState("I'm changed, will notify all observers");
    subject.notifyObservers("I'm changed, will notify all observers");
  }
}
