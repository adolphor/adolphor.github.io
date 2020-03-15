package y2020.m01.d23.publish.subscribe.v3;

import java.util.Observable;
import java.util.Observer;

public class ConcreteObserver implements Observer {
  private Object newState;
  @Override
  public void update(Observable o, Object state) {
    newState = state;
    System.out.println(state);
  }
}
