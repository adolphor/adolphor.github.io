package demo;/* Block comment */

public class Demo {
  public static void main(String[] args) {
    byte[] bytes = new byte[3];
    bytes[0] = 0x34;
    bytes[1] = 0x34;
    bytes[2] = 0x34;
    System.out.println(new String(bytes));
  }
}