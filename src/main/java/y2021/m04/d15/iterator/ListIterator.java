package y2021.m04.d15.iterator;


import y2021.m04.d14.link.Node;

/**
 * @author adolphor
 */
public class ListIterator {
  private Node current;
  private Node previous;
  private IterLinkList ourList;

  public ListIterator(IterLinkList list) {
    ourList = list;
    reset();
  }

  public void reset() {
    current = ourList.getFirst();
    previous = null;
  }

  public boolean atEnd() {
    return (current.next == null);
  }

  public void nextNode() {
    previous = current;
    current = current.next;
  }

  public Node getCurrent() {
    return current;
  }

  public void insertAfter(int data) {
    Node newNode = new Node(data);
    if (ourList.isEmpty()) {
      ourList.setFirst(newNode);
      current = newNode;
    } else {
      newNode.next = current.next;
      current.next = newNode;
      nextNode();
    }
  }

  public void insertBefore(int data) {
    Node newNode = new Node(data);
    if (previous == null) {
      newNode.next = ourList.getFirst();
      ourList.setFirst(newNode);
      reset();
    } else {
      newNode.next = previous.next;
      previous.next = newNode;
      current = newNode;
    }
  }

  public int deleteCurrent() {
    int data = current.data;
    if (previous == null) {
      ourList.setFirst(current.next);
      reset();
    } else {
      previous.next = current.next;
      if (atEnd()) {
        reset();
      } else {
        current = current.next;
      }
    }
    return data;
  }

}
