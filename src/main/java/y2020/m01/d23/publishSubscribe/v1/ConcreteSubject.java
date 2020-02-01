package y2020.m01.d23.publishSubscribe.v1;

import y2020.m01.d23.publishSubscribe.Observer;

import java.util.Enumeration;
import java.util.Vector;

public class ConcreteSubject implements Subject {

  private Vector<Observer> observersVector = new Vector<>();

  @Override
  public void attach(Observer observer) {
    observersVector.add(observer);
  }

  @Override
  public void detach(Observer observer) {
    observersVector.remove(observer);
  }

  @Override
  public void notifyObservers() {
    Enumeration observers = observers();
    while (observers.hasMoreElements()) {
      ((Observer) observers.nextElement()).update();
    }
  }

  /** 使用拷贝，从而使外界不能修改主题自己所使用的拷贝（TODO 为什么不让修改？） */
  private Enumeration observers() {
    return ((Vector) observersVector.clone()).elements();
  }

}