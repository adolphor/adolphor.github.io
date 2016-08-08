package Y2016.M08.D04_collection.hashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2016/8/8.
 */
public class HashCodeDemo {
  public static void main(String[] args) {

    List<String> list = new ArrayList<>();
    int hashCode = list.hashCode();
    System.out.println(hashCode);

    list.add("A");
    hashCode = list.hashCode();
    System.out.println(hashCode);

  }
}
