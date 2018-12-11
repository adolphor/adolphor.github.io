package demo;

import java.util.ArrayList;

public class Demo {
  public static void main(String[] args) {
    String s = "Hello var in java!";

    ArrayList<String> list = new ArrayList<String>();
    list.add("A");
    list.add("B");
    for (String str : list) {
      String test = str;
      System.out.println(test);
    }

    System.out.println(s);
  }
}

