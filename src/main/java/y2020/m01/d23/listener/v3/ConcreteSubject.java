package y2020.m01.d23.listener.v3;

/**
 * @author adolphor
 */
public class ConcreteSubject extends Subject {
  public void click(MyMouseEvent event) {
    notifyObservers(event);
  }
}
