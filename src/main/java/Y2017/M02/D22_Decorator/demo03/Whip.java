package Y2017.M02.D22_Decorator.demo03;

/**
 * Created by Bob on 2017/2/23.
 */
public class Whip extends Dressing {

  public Whip(Coffee coffee) {
    super(coffee);
  }

  @Override
  public double cost() {
    return super.cost() + 0.1;
  }
}