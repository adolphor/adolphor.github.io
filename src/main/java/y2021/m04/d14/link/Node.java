package y2021.m04.d14.link;

/**
 * @author adolphor
 */
public class Node {
  public int data;
  public Node next;
  public Node(int data) {
    this.data = data;
  }
  public void display() {
    System.out.println(data);
  }
}
