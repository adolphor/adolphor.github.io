package Y2016.M12.D14_proxy.cglibProxy;

public class TicketSeller {
  public void sell(int price) {
    System.out.println("TicketSeller 以价格 $" + price + " 卖了一张票 ...");
  }

  public void back(int price) {
    System.out.println("TicketSeller 退回了价值 $" + price + " 的车票 ...");
  }
}
