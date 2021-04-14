package y2021.m04.d14.double_;

import y2021.m04.d14.link.Node;

/**
 * @author adolphor
 */
public class DoubleEndedLink {
  private Node first;
  private Node last;

  public DoubleEndedLink() {
    first = null;
    last = null;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void insertFirst(int data) {
    Node newNode = new Node(data);
    if (isEmpty()) {
      last = newNode;
    }
    newNode.next = first;
    first = newNode;
  }

  public void insertLast(int data) {
    Node newNode = new Node(data);
    if (isEmpty()) {
      first = newNode;
    } else {
      last.next = newNode;
    }
    last = newNode;
  }

  public Node find(int data) {
    Node current = first;
    while (current.data != data) {
      if (current.next == null) {
        return null;
      } else {
        current = current.next;
      }
    }
    return current;
  }

  public Node deleteFirst() {
    Node temp = first;
    if (first.next == null) {
      last = null;
    }
    first = first.next;
    return temp;
  }

  public void display() {
    Node current = first;
    while (current != null) {
      current.display();
      current = current.next;
    }
  }

}
