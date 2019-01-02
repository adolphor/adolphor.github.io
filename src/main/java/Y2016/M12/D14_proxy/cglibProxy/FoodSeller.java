package Y2016.M12.D14_proxy.cglibProxy;

public class FoodSeller {
  public void sell(int price) {
    System.out.println("FoodSeller 以价格 $" + price + " 卖了一盒饭 ...");
  }

  public void back(int price) {
    System.out.println("FoodSeller 退回了价值 $" + price + " 的盒饭 ...");
  }
}
