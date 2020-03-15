package y2020.m01.d23.publish.subscribe.v5;

/**
 * @author adolphor
 */
public class ConcreteObserver implements Observer {
  @Override
  public void update() {
    System.out.println("I'm called to update");
  }

  @Override
  public void subscribe(Subject subject) {
    subject.addObserver(this);
  }
}
