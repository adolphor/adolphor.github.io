package y2021.m04.d14.application;

import y2021.m04.d14.double_.DoubleEndedLink;

/**
 * @author adolphor
 */
public class LinkQueue {
  private DoubleEndedLink theList;

  public LinkQueue() {
    theList = new DoubleEndedLink();
  }

  public void insert(int data) {
    theList.insertLast(data);
  }

  public int remove() {
    return theList.deleteFirst().data;
  }

  public boolean isEmpty() {
    return theList.isEmpty();
  }

}
