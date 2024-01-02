package Y2017.M02.D22_Decorator.demo02;

/**
 * Created by Bob on 2017/2/23.
 */
public class Coffee {
  private boolean mocha;
  private boolean whip;

  public double cost() {
    double price = 0d;
    if (mocha) {
      price += 0.5;
    }
    if (whip) {
      price += 0.1;
    }
    return price;
  }

  public void addMocha() {
    this.mocha = true;
  }

  public void addWhip() {
    this.whip = true;
  }
}
