package y2021.m04.d23;

import y2021.m03.d31.AbstractSort;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/9 17:23
 */
public class Partition extends AbstractSort {

  public static void main(String[] args) {
    int pivot = 99;
    int maxValue = 199;

    Partition partition = new Partition();
    partition.init(maxValue);
    partition.display();
    int size = partition.data.length;

    System.out.print("Pivot is " + pivot);
    int partDex = partition.partitionIt(0, size - 1, pivot);
    System.out.println(", Partition is at index " + partDex);

    partition.display();

  }

  public int partitionIt(int left, int right, long pivot) {
    int leftPtr = left - 1;
    int rightPtr = right + 1;
    while (true) {
      while (leftPtr < right && data[++leftPtr] < pivot) {
      }
      while (rightPtr > left && data[--rightPtr] > pivot) {
      }
      if (leftPtr >= rightPtr) {
        break;
      } else {
        swap(leftPtr, rightPtr);
      }
    }
    return leftPtr;
  }

  @Override
  public void sort() {
    System.err.println("not implement ... ");
  }

}
