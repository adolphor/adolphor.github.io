package y2021.m04.d14.single;

/**
 * @author adolphor
 */
public class LinkList {
  private Node first;

  public LinkList() {
    first = null;
  }

  public boolean isEmpty() {
    return (first == null);
  }

  public void insertFirst(int data) {
    Node newNode = new Node(data);
    newNode.next = first;  // 将原首节点挂在新节点上
    first = newNode;       // 将新节点设置为首节点
  }

  public Node deleteFirst() {
    Node temp = first;
    first = first.next;    // 不需要删除temp对next的引用吗？是否影响垃圾回收？
    return temp;
  }

  public Node find(int key) {
    Node current = first;
    while (current.data != key) {
      if (current.next == null) {
        return null;
      } else {
        current = current.next;
      }
    }
    return current;
  }

  public Node delete(int key) {
    Node current = first;
    Node previous = first;
    while (current.data != key) {
      if (current.next == null) {
        current = null;
      } else {
        previous = current;
        current = current.next;
      }
    }
    if (current == first) {
      first = first.next;
    } else {
      previous.next = current.next;
    }
    return current;
  }

  public void display() {
    Node current = first;
    while (current != null) {
      current.display();
      current = current.next;
    }
  }

}
