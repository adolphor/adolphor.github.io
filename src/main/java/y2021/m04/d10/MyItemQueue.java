package y2021.m04.d10;

/**
 * @author adolphor
 */
public class MyItemQueue {
  private int maxSize;
  private long[] queueArr;
  private int front;
  private int rear;
  private int nItems;

  public MyItemQueue(int size) {
    this.maxSize = size;
    this.queueArr = new long[maxSize];
    this.front = 0;
    this.rear = -1;
    this.nItems = 0;
  }

  public void insert(long val) throws Exception {
    if (isFull()){
      throw new Exception("queue is full");
    }
    if (rear == maxSize - 1) {
      rear = -1;
    }
    queueArr[++rear] = val;
    nItems++;
  }

  public long remove() throws Exception {
    if (isEmpty()){
      throw new Exception("queue is empty");
    }
    long temp = queueArr[front++];
    if (front == maxSize) {
      front = 0;
    }
    nItems--;
    return temp;
  }

  public long peekFront(){
    return queueArr[front];
  }

  public boolean isEmpty(){
    return nItems==0;
  }

  public boolean isFull(){
    return (nItems==maxSize);
  }

  public int size(){
    return nItems;
  }

}
