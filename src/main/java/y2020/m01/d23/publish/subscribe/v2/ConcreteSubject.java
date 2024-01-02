package y2020.m01.d23.publish.subscribe.v2;

/**
 * @author adolphor
 */
public class ConcreteSubject extends Subject {
  private Object state;

  public void changeState(Object newState) {
    state = newState;
    notifyObservers(state);
  }
}
