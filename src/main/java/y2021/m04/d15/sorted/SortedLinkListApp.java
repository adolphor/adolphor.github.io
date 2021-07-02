package y2021.m04.d15.sorted;

import y2021.m04.d14.link.Node;

/**
 * @author adolphor
 */
public class SortedLinkListApp {
  public static void main(String[] args) {
    SortedLinkList list = new SortedLinkList();
    list.insert(66);
    list.insert(22);
    list.insert(88);
    list.insert(44);
    list.display();
    Node node = list.remove();
    System.out.print("delete ");
    node.display();
    System.out.println("display left: ");
    list.display();
  }
}
