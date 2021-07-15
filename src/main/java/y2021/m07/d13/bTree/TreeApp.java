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

    Node found = theTree.find(30);
    theTree.display();
    if (found != null) {
      System.out.println("Found the node with key 25");
    } else {
      System.out.println("Could not find node with key 25");
    }

  }
}
