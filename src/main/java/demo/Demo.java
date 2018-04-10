package demo;

import java.util.ArrayList;

public class Demo {
  public static void main(String[] args) {
    var s = "Hello var in java!";

    var list = new ArrayList<String>();
    list.add("A");
    list.add("B");
    for (String str : list) {
      var test = str;
      System.out.println(test);
    }

    System.out.println(s);
  }
}

