package Y2016.M08.D01_UML.Dependence;

/**
 * Created by Bob on 2016/8/1.
 */
public class Demo {
  public static void main(String[] args) {
    Car car = new Car();
    Driver driver = new Driver();
    driver.drive(car);
  }
}
