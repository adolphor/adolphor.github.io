package y2021.m03.d31;

/**
 * @author adolphor
 */
public class InsertSort extends AbstractSort {

  public static void main(String[] args) {
    InsertSort selectSort = new InsertSort();
    selectSort.init();
    selectSort.display();
    selectSort.sort();
    selectSort.display();
  }

  /**
   * 选择排序核心算法
   */
  @Override
  public void sort() {
    int out, in;
    for (out = 1; out < data.length; out++) {   // out is dividing line
      int temp = data[out];                     // remove marked item    临时存储当前操作的需要插入的值
      in = out;                                 // start shifts at out   内部循环开始位置
      while (in > 0 && data[in - 1] >= temp) {  // until one is smaller  判断是否需要移动
        data[in] = data[in - 1];                // shift item right      将左边的值右移腾出插入的位置，被覆盖的值已经存储在temp或者已经右移到新位置了
        --in;                                   // go left one position  迭代操作位置左移
      }
      data[in] = temp;                          // insert marked item    将需要插入的值放在插入的位置
    }
  }

}
