package y2021.m03.d31;

/**
 * @author adolphor
 */
public class SelectSort extends AbstractSort {

  public static void main(String[] args) {
    SelectSort selectSort = new SelectSort();
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
    int min, out, in;
    for (out = 0; out < data.length - 1; out++) {        // outer loop
      min = out;                                         // minimum
      for (in = out + 1; in < data.length; in++) {       // inner loop
        if (data[in] < data[min]) {                      // if min greater
          min = in;                                      // we have a new min
        }
      }
      swap(out, min);                                    // swap them
    }
  }

}
