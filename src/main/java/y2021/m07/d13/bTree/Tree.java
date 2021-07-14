package y2021.m07.d13.bTree;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/13 17:29
 */
public class Tree {

  private Node root;

  public Node find(int key) {
    Node current = root;
    while (current.iData != key) {
      if (key < current.iData) {
        current = current.leftChild;
      } else {
        current = current.rightChild;
      }
      // ？这个和直接返回 current 有什么区别？
      if (current == null) {
        return null;
      }
      return current;
    }
    return null;
  }

  public void insert(int id, double dd) {

  }

  public void deleted(int d) {

  }

}
