package y2021.m03.d31;

import java.util.Random;

/**
 * @author adolphor
 */
public abstract class AbstractSort {

  protected int[] data;

  /**
   * 具体普通排序算法需要各自实现
   */
  abstract public void sort();

  public void init() {
    init(1000);
  }

  public void init(int max) {
    data = new int[10];
    for (int i = 0, length = data.length; i < length; i++) {
      data[i] = getNextInt(max);
    }
  }

  private int getNextInt(int max) {
    Random random = new Random();
    int nextInt = random.nextInt(max);
    if (check(nextInt)) {
      return getNextInt(max);
    } else {
      return nextInt;
    }
  }

  private boolean check(int nextInt) {
    for (int dd : data) {
      if (dd == nextInt) {
        return true;
      }
    }
    return false;
  }

  /**
   * 交换位置
   * @param i
   * @param j
   */
  public void swap(int i, int j) {
    int temp = data[i];
    data[i] = data[j];
    data[j] = temp;
  }

  /**
   * 遍历打印数组内容
   */
  public void display() {
    for (int i = 0, len = data.length; i < len; i++) {
      if (i == len - 1) {
        System.out.println(data[i]);
      } else {
        System.out.print(data[i] + ",\t");
      }
    }
  }

}
