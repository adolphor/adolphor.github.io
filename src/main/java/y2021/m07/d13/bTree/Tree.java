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

  public void display(int traverseType) {
    switch (traverseType) {
      case 1:
        System.out.println("\nPreOrder traversal:");
        preOrder(root);
        break;
      case 2:
        System.out.println("\nInOrder traversal:");
        inOrder(root);
        break;
      case 3:
        System.out.println("\nPostOrder traversal:");
        postOrder(root);
        break;
      default:
        System.err.println("error");
    }
  }

  /**
   * 中序遍历：A*(B+C)
   */
  private void inOrder(Node localRoot) {
    if (localRoot != null) {
      inOrder(localRoot.leftChild);
      System.out.println(localRoot);
      inOrder(localRoot.rightChild);
    }
  }

  /**
   * 前序遍历：*A+BC
   */
  private void preOrder(Node localRoot) {
    if (localRoot != null) {
      System.out.println(localRoot);
      inOrder(localRoot.leftChild);
      inOrder(localRoot.rightChild);
    }
  }

  /**
   * 后序遍历：ABC+*
   */
  private void postOrder(Node localRoot) {
    if (localRoot != null) {
      inOrder(localRoot.leftChild);
      inOrder(localRoot.rightChild);
      System.out.println(localRoot);
    }
  }

  /**
   * 删除有三种场景：
   * 1. 叶结点：没有子节点
   * 2. 一个子节点
   * ├── 2.1 左子节点
   * ├── 2.2 右子节点
   * 3. 两个子节点
   */
  public boolean deleted(int key) {
    Node current = root;
    Node parent = root;
    boolean isLeftChild = true;
    // 0. 先找需要删除的节点
    // ├── 要么找到
    // ├── 要么返回false
    while (current.iData != key) {
      parent = current;
      if (key < current.iData) {
        isLeftChild = true;
        current = current.leftChild;
      } else {
        isLeftChild = false;
        current = current.rightChild;
      }
      if (current == null) {
        return false;
      }
    }
    // 1. 没有子节点
    if (current.leftChild == null && current.rightChild == null) {
      if (current == root) {
        root = null;
      } else if (isLeftChild) {
        parent.leftChild = null;
      } else {
        parent.rightChild = null;
      }
    }
    // 2. 一个子节点：子节点左边的肯定也在父节点左边，子节点右边的肯定也在父节点右边
    // ├── 2.1 左子节点
    else if (current.rightChild == null) {
      if (current == root) {
        root = current.leftChild;
      }
      // 把仅有的一个子节点挂载到父节点上
      else if (isLeftChild) {
        parent.leftChild = current.leftChild;
      } else {
        parent.rightChild = current.leftChild;
      }
    }
    // ├── 2.2 右子节点
    else if (current.leftChild == null) {
      if (current == root) {
        root = current.rightChild;
      } else if (isLeftChild) {
        parent.leftChild = current.rightChild;
      } else {
        parent.rightChild = current.rightChild;
      }
    }
    // 3. 两个子节点
    // 需要找到需要删除的节点的后继节点(successor)：右子节点的左子节点的左子节点一直找下去
    else {
      Node successor = getSuccessor(current);
      if (current == root) {
        root = successor;
      } else if (isLeftChild) {
        parent.leftChild = successor;
      } else {
        parent.rightChild = successor;
      }
      successor.leftChild = current.leftChild;
    }
    return true;
  }

  public Node getSuccessor(Node delNode) {
    Node successorParent = delNode;
    Node successor = delNode;
    Node current = delNode.rightChild;
    // go to right child until no more left children
    while (current != null) {
      successorParent = successor;
      successor = current;
      current = current.leftChild;
    }
    // if successor not right child, make connections
    if (successor != delNode.rightChild) {
      successorParent.leftChild = successor.rightChild;
      successor.rightChild = delNode.rightChild;
    }
    return successor;
  }

}
