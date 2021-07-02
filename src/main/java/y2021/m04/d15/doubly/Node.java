package y2021.m04.d15.doubly;

/**
 * @author adolphor
 */
public class Node {
  public int data;
  public Node previous;
  public Node next;

  public Node(int data) {
    this.data = data;
  }

  public void display() {
    System.out.println(data);
  }
}
