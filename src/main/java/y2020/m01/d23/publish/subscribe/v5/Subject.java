package y2020.m01.d23.publish.subscribe.v5;

import java.util.Vector;

/**
 * @author adolphor
 */
public abstract class Subject {
  // 存储所有观察者对象的实例
  private Vector<Observer> observersVector = new Vector<Observer>();

  public void attach(Observer observer) {
    observersVector.add(observer);
  }

  public void notifyObservers(Object msg) {
    for (Observer observer : observersVector) {
      observer.update(msg);
    }
  }

}
