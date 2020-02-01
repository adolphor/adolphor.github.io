package y2020.m01.d23.publishSubscribe.v1;

import y2020.m01.d23.publishSubscribe.Observer;

public interface Subject {

  /** 注册登记一个新的观察者对象 */
  void attach(Observer observer);

  /** 删除已注册登记的对象 */
  void detach(Observer observer);

  /** 通知所有已注册登记对象 */
  void notifyObservers();

}
