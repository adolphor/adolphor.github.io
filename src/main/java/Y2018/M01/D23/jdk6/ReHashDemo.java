package Y2018.M01.D23.jdk6;

public class ReHashDemo {
  public static void main(String[] args) {
    int key1 = 0x0f470000;
    int key2 = 0x14bc0000;

    int length = 0x0000000f;

    int rk1 = hash(key1);
    int rk2 = hash(key2);

    printBin(rk1);
    printBin(rk2);

    int index1 = indexFor(rk1, length);
    int index2 = indexFor(rk2, length);

    System.out.println(index1);
    System.out.println(index2);
  }

  static int hash(int h) {
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }

  // 调用方式
  static int indexFor(int h, int length) {
    return h & (length - 1);
  }

  static void printBin(int h) {
    System.out.println(String.format("%32s", Integer.toBinaryString(h)).replace(' ', '0'));
  }
}

