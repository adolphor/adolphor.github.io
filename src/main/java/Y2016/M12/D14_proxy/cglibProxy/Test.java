package Y2016.M12.D14_proxy.cglibProxy;

public class Test {
  public static void main(String[] args) {

    ProxySeller proxy = new ProxySeller();

    // 买票服务
    TicketSeller ticketSeller = (TicketSeller) proxy.getInstance(new TicketSeller());
    ticketSeller.sell(35);
    ticketSeller.back(35);

    // 外卖服务
    FoodSeller foodSeller = (FoodSeller) proxy.getInstance(new FoodSeller());
    foodSeller.sell(20);
    foodSeller.back(20);

  }
}
