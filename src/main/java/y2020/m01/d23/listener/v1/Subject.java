package y2020.m01.d23.listener.v1;

import java.util.Vector;

/**
 * @author adolphor
 */
public abstract class Subject {
  private Vector<Observer> observersVector = new Vector<Observer>();

  public void addObserver(Observer observer) {
    observersVector.add(observer);
  }

  public void notifyObservers(Object msg) {
    for (Observer observer : observersVector) {
      observer.update(msg);
    }
  }
}
