package y2020.m01.d23.listener.v1;

/**
 * @author adolphor
 */
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    ConcreteSubject subject = new ConcreteSubject();
    subject.addObserver(new Observer() {
      @Override
      public void update(Object state) {
        System.out.println(state);
      }
    });
    subject.changeState("I'm changed, will notify all observers");
    subject.notifyObservers("I'm changed, will notify all observers");
  }
}
