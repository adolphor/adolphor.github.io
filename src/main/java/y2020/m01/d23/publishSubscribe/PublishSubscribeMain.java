package y2020.m01.d23.publishSubscribe;

import y2020.m01.d23.publishSubscribe.v2.ConcreteSubject;

/**
 * @author adolphor
 */
public class PublishSubscribeMain {

  public static void main(String[] args) {
    ConcreteSubject subject = new ConcreteSubject();
    Observer observer = new ConcreteObserver();
    subject.attach(observer);
    subject.changeState("new state!");
    subject.detach(observer);
  }

}
