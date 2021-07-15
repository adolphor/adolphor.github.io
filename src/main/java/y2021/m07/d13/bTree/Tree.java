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
    Node newNode = new Node();
    newNode.iData = id;
    newNode.fData = dd;
    if (root == null) {
      root = newNode;
    } else {
      Node current = root;
      Node parent;
      while (true) {
        parent = current;
        if (id < current.iData) {
          current = current.leftChild;
          if (current == null) {
            parent.leftChild = newNode;
            return;
          }
        } else {
          current = current.rightChild;
          if (current == null) {
            parent.rightChild = newNode;
            return;
          }
        }
      }
    }
  }

  public void deleted(int d) {

  }

  public void display() {
    inOrder(root);
  }

  /**
   * 中序遍历：A*(B+C)
   */
  private void inOrder(Node localRoot){
    if (localRoot!=null){
      inOrder(localRoot.leftChild);
      System.out.println(localRoot);
      inOrder(localRoot.rightChild);
    }
  }

  /**
   * 前序遍历：*A+BC
   */
  private void preOrder(Node localRoot){
    if (localRoot!=null){
      System.out.println(localRoot);
      inOrder(localRoot.leftChild);
      inOrder(localRoot.rightChild);
    }
  }

  /**
   * 后序遍历：ABC+*
   */
  private void postOrder(Node localRoot){
    if (localRoot!=null){
      inOrder(localRoot.leftChild);
      inOrder(localRoot.rightChild);
      System.out.println(localRoot);
    }
  }

}
