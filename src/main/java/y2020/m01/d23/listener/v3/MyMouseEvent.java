package y2020.m01.d23.listener.v3;

import java.util.Random;

public class MyMouseEvent {
  int x;
  int y;
  public MyMouseEvent() {
    x = new Random().nextInt();
    y = new Random().nextInt();
  }
  public void move(int x, int y) {
    this.x = x;
    this.y = y;
  }
  @Override
  public String toString() {
    return "MyMouseEvent{" + "x=" + x + ", y=" + y + '}';
  }
}
