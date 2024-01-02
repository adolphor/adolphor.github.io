package y2020.m01.d23.listener.v2;

/**
 * @author adolphor
 */
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    ConcreteSubject subject = new ConcreteSubject();
    subject.addListener(new Listener() {
      @Override
      public void onClick(Object msg) {
        System.out.println(msg);
      }
    });
    subject.click("I'm changed, will notify all observers");
  }
}
