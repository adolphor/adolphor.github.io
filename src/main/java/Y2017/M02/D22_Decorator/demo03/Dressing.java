package Y2017.M02.D22_Decorator.demo03;

/**
 * Created by Bob on 2017/2/23.
 */
public class Dressing implements Coffee {

  private Coffee coffee;

  public Dressing(Coffee coffee) {
    this.coffee = coffee;
  }

  @Override
  public double cost() {
    return coffee.cost();
  }
}