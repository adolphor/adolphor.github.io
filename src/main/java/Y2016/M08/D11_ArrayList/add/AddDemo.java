package Y2016.M08.D11_ArrayList.add;

import java.util.ArrayList;

/**
 * Created by Bob on 2016/8/11.
 */
public class AddDemo {
  public static void main(String[] args) {
    // 扩容测试
    ArrayList<String> defaultSize = new ArrayList<>();
    for (int i = 0; i < 35; i++) {
      defaultSize.add("defaultSize => " + i);
    }

    ArrayList<String> samllSize = new ArrayList<>(8);
    for (int i = 0; i < 35; i++) {
      samllSize.add("samllSize => " + i);
    }

    ArrayList<String> bigSize = new ArrayList<>(12);
    for (int i = 0; i < 35; i++) {
      bigSize.add("bigSize => " + i);
    }

    // 定点插入测试
    ArrayList<String> addIndex = new ArrayList<>(12);
    for (int i = 0; i < 8; i++) {
      addIndex.add("addIndex => " + i);
    }
    addIndex.add(4, "insert");


  }
}
