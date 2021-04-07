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
      int temp = data[out];                     // remove marked item
      in = out;                                 // start shifts at out
      while (in > 0 && data[in - 1] >= temp) {  // until one is smaller
        data[in] = data[in - 1];                // shift item right
        --in;                                   // go left one position
      }
      data[in] = temp;                          // insert marked item
    }
  }

}
