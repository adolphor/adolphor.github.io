package y2021.m04.d23;

import y2021.m03.d31.AbstractSort;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/7 15:38
 */
public class ShellSort extends AbstractSort {

  public static void main(String[] args) {
    ShellSort shellSort = new ShellSort();
    shellSort.init();
    shellSort.display();
    shellSort.sort();
    shellSort.display();
  }

  @Override
  public void init() {
    data = new int[10];
    for (int i = 0; i < 10; i++) {
      data[i] = 10 - i;
    }
  }

  @Override
  public void sort() {
    int in, out;
    int temp;

    int h = 1;
    while (h <= data.length / 3) {
      h = h * 3 + 1;
    }
    System.out.println("初始间隔：" + h);
    while (h > 0) {
      for (out = h; out < data.length; out++) {
        temp = data[out];
        in = out;
        while (in > h - 1 && data[in - h] >= temp) {
          data[in] = data[in - h];
          in -= h;
        }
        data[in] = temp;
        display();
      }
      h = (h - 1) / 3;
      System.out.println("变更间隔：" + h);
    }
  }

}
