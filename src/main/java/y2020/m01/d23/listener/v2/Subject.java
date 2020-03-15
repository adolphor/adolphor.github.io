package y2020.m01.d23.listener.v2;

import java.util.Vector;

/**
 * @author adolphor
 */
public abstract class Subject {

  private Vector<Listener> observersVector = new Vector<Listener>();

  public void addListener(Listener observer) {
    observersVector.add(observer);
  }

  public void notifyObservers(Object msg) {
    for (Listener observer : observersVector) {
      observer.onClick(msg);
    }
  }

}
