package y2020.m01.d23.publishSubscribe;

public class ConcreteObserver implements Observer {
  @Override
  public void update() {
    System.out.println("I'm notified!");
  }
}