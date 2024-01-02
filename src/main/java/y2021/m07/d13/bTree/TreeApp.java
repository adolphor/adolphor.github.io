package y2021.m07.d13.bTree;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/13 17:30
 */
public class TreeApp {
  public static void main(String[] args) {
    Tree theTree = new Tree();
    theTree.insert(50, 1.5);
    theTree.insert(25, 1.7);
    theTree.insert(75, 1.9);
    theTree.insert(10, 1.0);
    theTree.insert(99, 1.8);

    Node found = theTree.find(30);
    if (found != null) {
      System.out.println("Found the node with key 25");
    } else {
      System.out.println("Could not find node with key 25");
    }

    theTree.display(1);
    theTree.display(2);
    theTree.display(3);

    boolean deleted = theTree.deleted(25);
    System.out.println("delete:" + deleted);

    theTree.display(1);
    theTree.display(2);
    theTree.display(3);

  }
}
