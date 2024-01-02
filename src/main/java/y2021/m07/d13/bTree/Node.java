package y2021.m07.d13.bTree;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/13 11:25
 */
public class Node {
  int iData;
  double fData;

  Node leftChild;
  Node rightChild;

  @Override
  public String toString() {
    return "Node{" +
      "iData=" + iData +
      ", fData=" + fData +
      '}';
  }
}
