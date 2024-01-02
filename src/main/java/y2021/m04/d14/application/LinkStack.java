package y2021.m04.d14.application;

import y2021.m04.d14.single.LinkList;

/**
 * @author adolphor
 */
public class LinkStack {

  private LinkList thisList;

  public LinkStack() {
    thisList = new LinkList();
  }

  public void push(int data) {
    thisList.insertFirst(data);
  }

  public int pop() {
    return thisList.deleteFirst().data;
  }

  public boolean isEmpty() {
    return thisList.isEmpty();
  }

  public void display() {
    thisList.display();
  }

}
