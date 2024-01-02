package y2020.m01.d23.listener.v3;

/**
 * @author adolphor
 */
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    ConcreteSubject subject = new ConcreteSubject();
    subject.addListener(new Listener() {
      @Override
      public void onClick(MyMouseEvent event) {
        System.out.println(event);
      }
    });
    MyMouseEvent event = new MyMouseEvent();
    subject.click(event);
  }
}
