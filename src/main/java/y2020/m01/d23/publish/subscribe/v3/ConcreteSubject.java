package y2020.m01.d23.publish.subscribe.v3;

import java.util.Observable;

public class ConcreteSubject extends Observable {
  private String state;

  public void changeState(String newState) {
    state = newState;
    setChanged();
    notifyObservers(state);
  }
}
