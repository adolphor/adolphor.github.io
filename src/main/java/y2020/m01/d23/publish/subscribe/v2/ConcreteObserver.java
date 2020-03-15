package y2020.m01.d23.publish.subscribe.v2;

/**
 * @author adolphor
 */
public class ConcreteObserver implements Observer {

  private Object newState;

  @Override
  public void update(Object state) {
    newState = state;
    System.out.println(newState);
  }

}
