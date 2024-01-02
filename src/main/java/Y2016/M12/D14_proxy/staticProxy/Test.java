package Y2016.M12.D14_proxy.staticProxy;

public class Test {
  public static void main(String[] args) {
    // 买票服务
    ProxySeller tickeProxy = new ProxySeller(new TicketSeller());
    tickeProxy.sell(35);
    tickeProxy.back(35);

    System.out.println();

    // 外卖服务
    ProxySeller foodProxy = new ProxySeller(new FoodSeller());
    foodProxy.sell(20);
    foodProxy.back(20);

  }
}
