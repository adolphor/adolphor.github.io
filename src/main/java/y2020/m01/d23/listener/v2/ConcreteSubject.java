package y2020.m01.d23.listener.v2;

/**
 * @author adolphor
 */
public class ConcreteSubject extends Subject {
  private Object state;

  public void click(Object newState) {
    state = newState;
    notifyObservers(state);
  }
}
