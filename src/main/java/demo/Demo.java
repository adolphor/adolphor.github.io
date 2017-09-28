package demo;/* Block comment */

import java.sql.Timestamp;

public class Demo {
  public static void main(String[] args) {

    long starttime = System.currentTimeMillis();

    int st = (int)starttime;

    System.out.println(st);

    Timestamp timestamp = new Timestamp(st);

    System.out.println(timestamp);
  }
}

