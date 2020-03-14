package y2020.m01.d23.publish.subscribe.v4;

import java.util.Observable;
import java.util.Observer;

public class ConcreteObserver implements Observer {
  @Override
  public void update(Observable o, Object arg) {
    System.out.println(arg);
  }
  public void subscribe(Observable subject) {
    subject.addObserver(this);
  }
}
