package demo;

public class Demo {
  public static void main(String[] args) {
    long init = 2000000;
    long percent = 8;

    for (int i = 0; i < 10; i++) {
      System.out.println(i + " => " + (init * percent) / 10000);
      init = init * (100 + percent) / 100;
    }
  }
}

