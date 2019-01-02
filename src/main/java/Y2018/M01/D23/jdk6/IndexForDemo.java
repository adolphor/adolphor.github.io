package Y2018.M01.D23.jdk6;

public class IndexForDemo {
  public static void main(String[] args) {
    int key1 = 0x0f470000;  // 随便两个高位不同低位相同的int数据作为key的原始hash值
    int key2 = 0x14bc0000;
    printBin(key1);
    printBin(key2);

    int lenght = 0x0000000f; // 16 的 16进制表示

    int hk1 = indexFor(key1, lenght);
    int hk2 = indexFor(key2, lenght);

    printBin(hk1);
    printBin(hk2);

  }

  static int indexFor(int h, int length) {
    return h & (length - 1);
  }

  static void printBin(int h) {
    System.out.println(String.format("%32s", Integer.toBinaryString(h)).replace(' ', '0'));
  }

}
