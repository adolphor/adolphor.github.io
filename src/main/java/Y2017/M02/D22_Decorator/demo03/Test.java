package Y2017.M02.D22_Decorator.demo03;

/**
 * Created by Bob on 2017/2/23.
 */
public class Test {
  public static void main(String[] args) {
    Coffee coffee = new Espresso(); // 2.5
    coffee = new Mocha(coffee); // 0.5
    coffee = new Mocha(coffee); // 0.5 => 加双份 Mocha
    coffee = new Whip(coffee); // 0.1
    // 2.5 + 0.5 + 0.5 + 0.1 = 3.6
    System.out.println(coffee.cost());
  }
}
