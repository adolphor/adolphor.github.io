package y2021.m04.d15.sorted;

import y2021.m04.d14.link.Node;

/**
 * @author adolphor
 */
public class SortedLinkList {
  private Node first;

  public SortedLinkList() {
    first = null;
  }

  public void insert(int data) {
    Node newNode = new Node(data);
    Node previous = null;
    Node current = first;
    while (current != null && data > current.data) {
      previous = current;
      current = current.next;
    }
    if (previous == null) {
      first = newNode;
    } else {
      previous.next = newNode;
    }
    newNode.next = current;
  }

  public Node remove(){
    Node temp =first;
    first=first.next;
    return temp;
  }

  public boolean isEmpty() {
    return (first == null);
  }

  public void display() {
    Node current = first;
    while (current != null) {
      current.display();
      current = current.next;
    }
  }

}
