package y2020.m01.d23.publish.subscribe.v3;

/**
 * @author adolphor
 */
public class ConcreteObserver implements Observer {
  @Override
  public void update(Object msg) {
    System.out.println(msg);
  }

  @Override
  public void subscribe(Subject subject) {
    subject.attach(this);
  }
}
