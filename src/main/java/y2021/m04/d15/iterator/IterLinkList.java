package y2021.m04.d15.iterator;


import y2021.m04.d14.link.Node;

/**
 * @author adolphor
 */
public class IterLinkList {

  private Node first;

  public IterLinkList() {
    first = null;
  }

  public Node getFirst() {
    return first;
  }

  public void setFirst(Node node) {
    first = node;
  }

  public boolean isEmpty() {
    return (first == null);
  }

  public ListIterator getIterator() {
    return new ListIterator(this);
  }

  public void display() {
    Node current = first;
    while (current != null) {
      current.display();
      current = current.next;
    }
  }

}
