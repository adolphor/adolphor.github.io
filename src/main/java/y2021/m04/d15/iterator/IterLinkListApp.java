package y2021.m04.d15.iterator;

/**
 * @author adolphor
 */
public class IterLinkListApp {
  public static void main(String[] args) {
    IterLinkList theList = new IterLinkList();
    ListIterator iterator = theList.getIterator();
    iterator.insertAfter(20);
    iterator.insertAfter(40);
    iterator.insertAfter(80);
    iterator.insertAfter(60);

    theList.display();
    iterator.nextNode();
    iterator.getCurrent().display();
    iterator.insertBefore(10);
    iterator.insertBefore(30);
    theList.display();
    iterator.deleteCurrent();
    theList.display();
  }
}
