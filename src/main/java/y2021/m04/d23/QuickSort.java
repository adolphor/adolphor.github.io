package y2021.m04.d23;

import y2021.m03.d31.AbstractSort;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/12 14:50
 */
public class QuickSort extends AbstractSort {

  public static void main(String[] args) {
    int size = 20;
    int maxValue = 199;

    QuickSort quickSort = new QuickSort();
    quickSort.init(size, maxValue);
    quickSort.display();
    size = quickSort.data.length;
    quickSort.reQuickSort(0, size - 1);
    quickSort.display();
  }

  @Override
  public void sort() {
    reQuickSort(0, data.length - 1);
  }

  public void reQuickSort(int left, int right) {
    if (right - left <= 0) {
      return;
    } else {
      int pivot = data[right];
      int partition = partitionIt(left, right, pivot);
      reQuickSort(left, partition - 1);
      reQuickSort(partition + 1, right);
    }
  }

  public int partitionIt(int left, int right, int pivot) {
    int leftPtr = left - 1;
    int rightPtr = right;
    while (true) {
      while (leftPtr < data.length && data[++leftPtr] < pivot) {
      }
      while (rightPtr > 0 && data[--rightPtr] > pivot) {
      }
      if (leftPtr >= rightPtr) {
        break;
      } else {
        swap(leftPtr, rightPtr);
      }
    }
    swap(leftPtr, right);
    return leftPtr;
  }

}
