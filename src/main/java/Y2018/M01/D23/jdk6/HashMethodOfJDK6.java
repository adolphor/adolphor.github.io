package Y2018.M01.D23.jdk6;

public class HashMethodOfJDK6 {

  public static void main(String[] args) {
    int h = 0x0000000f;
    int h1 = h >>> 20;
    int h2 = h >>> 12;
    int h3 = h1 ^ h2;
    int h4 = h ^ h3;
    int h5 = h4 >>> 7;
    int h6 = h4 >>> 4;
    int h7 = h5 ^ h6;
    int h8 = h4 ^ h7;

    printBin(h);
    printBin(h1);
    printBin(h2);
    printBin(h3);
    printBin(h4);
    printBin(h5);
    printBin(h6);
    printBin(h7);
    printBin(h8);

  }

  static void printBin(int h) {
    System.out.println(String.format("%32s", Integer.toBinaryString(h)).replace(' ', '0'));
  }

}
