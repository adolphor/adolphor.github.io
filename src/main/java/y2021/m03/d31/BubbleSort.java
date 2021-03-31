package y2021.m03.d31;


/**
 * @author adolphor
 */
public class BubbleSort extends AbstractSort{

  public static void main(String[] args) {
    BubbleSort bubbleSort = new BubbleSort();
    bubbleSort.init();
    bubbleSort.display();
    bubbleSort.sort();
    bubbleSort.display();
  }

  /**
   * 冒泡核心算法
   */
  @Override
  public void sort() {
    int out, in;
    for (out = data.length - 1; out > 1; out--) {    // outer loop (backward)
      for (in = 0; in < out; in++) {                 // inner loop (forward)
        if (data[in] > data[in + 1]) {               // out of order ?
          swap(in, in + 1);                        // swap them
        }
      }
    }
  }

}
