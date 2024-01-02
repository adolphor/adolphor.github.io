package y2021.m04.d14.single;

import y2021.m04.d14.link.Node;

/**
 * @author adolphor
 */
public class LinkListApp {
  public static void main(String[] args) {
    LinkList list = new LinkList();
    list.insertFirst(22);
    list.insertFirst(44);
    list.insertFirst(66);
    list.insertFirst(88);
    list.display();
    Node node = list.deleteFirst();
    System.out.print("delete first: ");
    node.display();
    node = list.find(44);
    System.out.print("find: ");
    node.display();
    node = list.delete(66);
    System.out.print("delete key: ");
    node.display();
    System.out.println("display left: ");
    list.display();
  }
}
