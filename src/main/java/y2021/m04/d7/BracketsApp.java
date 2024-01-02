package y2021.m04.d7;

public class BracketsApp {

  public static void main(String[] args) {
    BracketChecker checker = new BracketChecker("test{in[out(abc)dd]}");
    checker.check();
  }


}
