package y2020.m01.d23.publishSubscribe.v2;

import y2020.m01.d23.publishSubscribe.Observer;

import java.util.Enumeration;
import java.util.Vector;

public abstract class Subject {

  private Vector<Observer> observersVector = new Vector<>();

  /** 注册登记一个新的观察者对象 */
  public void attach(Observer observer) {
    observersVector.add(observer);
  }

  /** 删除已注册登记的对象 */
  public void detach(Observer observer) {
    observersVector.remove(observer);
  }

  /** 通知所有已注册登记对象 */
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
