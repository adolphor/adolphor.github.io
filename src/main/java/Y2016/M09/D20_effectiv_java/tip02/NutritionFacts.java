package Y2016.M09.D20_effectiv_java.tip02;

/**
 * Created by Bob on 2017/1/18.
 * 引用地址：
 * http://adolphor.com/blog/2016/09/20/effective-java-second-edition.html#tips2
 */
public class NutritionFacts {
  private final int servingSize;
  private final int servings;
  private final int calories;
  private final int fat;

  public static class Builder {
    // 必填属性
    private final int servingSize;
    private final int servings;
    // 可选属性
    private int calories = 0;
    private int fat = 0;

    public Builder(int servingSize, int servings) { // 必填属性构造器
      this.servingSize = servingSize;
      this.servings = servings;
    }

    public Builder calories(int val) { // 可选属性set方法
      this.calories = val;
      return this;
    }

    public Builder fat(int val) {
      this.fat = val;
      return this;
    }

    public NutritionFacts build() { // 实例化目标对象
      return new NutritionFacts(this);
    }
  }

  public NutritionFacts(Builder builder) { // 供内部类构造方法调用
    servingSize = builder.servingSize;
    servings = builder.servings;
    calories = builder.calories;
    fat = builder.fat;
  }
}