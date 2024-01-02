package Y2016.M08.D16_LinkedList.iterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Spliterator;

/**
 * Created by Bob on 2016/8/18.
 */
public class IteratorDemo {
  public static void main(String[] args) {
    LinkedList<String> list = new LinkedList<>();
    list.add("A");
    list.add("B");
    list.add("C");
    list.add("D");

    ListIterator<String> listIterator = list.listIterator();
    System.out.println("listIterator: ");
    while (listIterator.hasNext())
      System.out.print(listIterator.next());

    System.out.println("\nlistIteratorIndex: ");
    ListIterator<String> listIteratorIndex = list.listIterator(2);
    while (listIteratorIndex.hasNext())
      System.out.print(listIteratorIndex.next());

    System.out.println("\ndescendingIterator: ");
    Iterator<String> descendingIterator = list.descendingIterator();
    while (descendingIterator.hasNext())
      System.out.print(descendingIterator.next());

    System.out.println("\nspliterator: ");
    Spliterator<String> spliterator = list.spliterator();

  }
}
