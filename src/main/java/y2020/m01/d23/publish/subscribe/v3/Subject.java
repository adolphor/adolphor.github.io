package y2020.m01.d23.publish.subscribe.v3;

import java.util.Vector;

/**
 * @author adolphor
 */
public class Subject {
  private Vector<Observer> observersVector = new Vector<>();

  public void attach(Observer observer) {
    observersVector.add(observer);
  }

  public void notifyObservers(Object msg) {
    for (Observer observer : observersVector) {
      observer.update(msg);
    }
  }
}
