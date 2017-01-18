package Y2016.M09.D20_effectiv_java.tip02;

/**
 * Created by Bob on 2017/1/18.
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

    public Builder(int servingSize, int servings) {
      this.servingSize = servingSize;
      this.servings = servings;
    }
    public Builder calories(int val) {
      calories = val;
      return this;
    }
    public Builder fat(int val) {
      this.fat = val;
      return this;
    }

    public NutritionFacts build() {
      return new NutritionFacts(this);
    }
  }
  public NutritionFacts(Builder builder) {
    servingSize = builder.servingSize;
    servings = builder.servings;
    calories = builder.calories;
    fat = builder.fat;
  }
}
