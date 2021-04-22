package y2021.m04.d15.anagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author adolphor
 */
public class AnagramApp {
  private static int size;
  private static int count;
  private static char[] arrChar = new char[100];

  public static void main(String[] args) throws IOException {
    System.out.println("Enter a word: ");
    String word = getString(); // get word
    size = word.length(); // find its size
    count = 0;
    for (int j = 0; j < size; j++) {
      arrChar[j] = word.charAt(j); // put it in array
    }
    doAnagram(size); // anagram it
  }

  /**
   * anagram [ˈænəɡræm] 相同字母异序词，易位构词，变位词
   * @param newSize
   */
  public static void doAnagram(int newSize) {
    if (newSize == 1) { // if innermost
      displayWord(); // display
      return;
    }
    for (int j = 0; j < newSize; j++) { // for each position
      doAnagram(newSize - 1); // anagram remaining
      rotate(newSize); // rotate word
    }
    System.out.print(""); // for debug
  }

  /**
   * rotate [ˈroʊteɪt] 旋转；循环
   */
  public static void rotate(int newSize) {
    int j;
    int position = size - newSize;
    char temp = arrChar[position]; // save first letter
    for (j = position + 1; j < size; j++) { // shift others left
      arrChar[j - 1] = arrChar[j]; // put first on right
    }
    arrChar[j - 1] = temp;
    System.out.print(""); // for debug
  }

  public static void displayWord() {
    if (count < 9) { // 对齐
      System.out.print(" ");
    }
    System.out.print(++count + " ");
    for (int j = 0; j < size; j++) {
      System.out.print(arrChar[j]);
    }
    System.out.flush();
    if (count % 6 == 0) {
      System.out.println();
    } else {
      System.out.print("   ");
    }
  }

  public static String getString() throws IOException {
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);
    String s = br.readLine();
    return s;
  }
}
