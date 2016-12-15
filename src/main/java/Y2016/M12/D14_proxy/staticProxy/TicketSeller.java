package Y2016.M12.D14_proxy.staticProxy;

public class TicketSeller implements Seller {
  public void sell(int price) {
    System.out.println("TicketSeller 以价格 $" + price + " 卖了一张票 ...");
  }
}
