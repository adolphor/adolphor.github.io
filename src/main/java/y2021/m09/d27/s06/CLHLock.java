package y2021.m09.d27.s06;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/28 23:03
 * @Email: 0haizhu0@gmail.com
 */
public class CLHLock {

  private AtomicReference<CLHNode> tailNode = new AtomicReference<>();
  private ThreadLocal<CLHNode> currentThreadNode = new ThreadLocal<>();

  static final class CLHNode {

    private volatile boolean locked = true;

    public boolean isLocked() {
      return locked;
    }

    public void setLocked(boolean locked) {
      this.locked = locked;
    }

  }

  public void lock() {
    // 首先对当前节点进行初始化
    CLHNode currentNode = currentThreadNode.get();
    if (currentNode == null) {
      currentNode = new CLHNode();
      // 设置状态，标识当前节点正在加锁
      currentNode.setLocked(true);
      currentThreadNode.set(currentNode);
    }

    // 先判断当前尾节点，是否有其他线程节点
    CLHNode preNode = tailNode.getAndSet(currentNode);
    // 如果没有尾节点，则当前节点之前不存在其他线程竞争锁，直接加锁成功
    if (preNode == null) {
      return;
    }
    // 如果有尾节点，则说明前驱节点不为空，需要自选等待前驱节点的线程释放锁以后，再进行加锁
    while (preNode.isLocked()) {
    }
  }

  public void unlock() {
    // 获取当前线程节点
    CLHNode currentNode = currentThreadNode.get();
    if (currentNode == null || currentNode.isLocked() == false) {
      // 当前线程没有锁，所以不用释放直接返回，也可以抛出异常
      return;
    }
    // CAS 尝试将尾节点设置为null
    if (tailNode.compareAndSet(currentNode, null)) {
      // 成功，说明当前线程节点是尾节点，阻塞队列中没有其他线程在竞争锁，将尾节点设置为null，即可释放锁
    } else {
      // 失败，说明当前线程节点不是尾节点，有其他线程正在自旋当前线程的locked变量
      currentNode.setLocked(false);
    }
  }

}
