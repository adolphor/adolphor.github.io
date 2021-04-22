package y2021.m04.d15.binary;

/**
 * @author adolphor
 */
public class BinarySearch {
  private int[] a; // ref to array a
  private int nElem;  // number for data items

  public BinarySearch(int max) {
    a = new int[max];
    nElem = 0;
  }

  public int size() {
    return nElem;
  }

  public int find(int key) {
    return recFind(key, 0, nElem - 1);
  }

  public int recFind(int key, int lowerBound, int upperBound) {
    int curIn = (lowerBound + upperBound) / 2;
    if (a[curIn] == key) {
      return curIn; // found it
    } else if (lowerBound > upperBound) {
      return nElem; // can't find it
    } else { // divide range
      if (a[curIn] < key) {
        return recFind(key, curIn + 1, upperBound);
      } else {
        return recFind(key, lowerBound, curIn - 1);
      }
    }
  }

  public void insert(int val) {
    int j;
    for (j = 0; j < nElem; j++) {
      if (a[j] > val) {
        break;
      }
    }
    for (int k = nElem; k > j; k--) {
      a[k] = a[k - 1];
    }
    a[j] = val;
    nElem++;
  }

  public void display() {
    for (int j = 0; j < nElem; j++) {
      System.out.print(a[j] + " ");
    }
    System.out.println();
  }

}
