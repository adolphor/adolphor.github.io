package y2021.m04.d14.application;

import y2021.m04.d14.first_last.FirstLastLinkList;

/**
 * @author adolphor
 */
public class LinkQueue {
  private FirstLastLinkList theList;

  public LinkQueue() {
    theList = new FirstLastLinkList();
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
