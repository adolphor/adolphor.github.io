package Y2017.M02.D22_Decorator.demo02;

/**
 * Created by Bob on 2017/2/23.
 */
public class Test {
  public static void main(String[] args) {
    Coffee coffee = new Decaf();
    coffee.addMocha();
    coffee.addWhip();
    //2.6
    System.out.println(coffee.cost());
  }
}
