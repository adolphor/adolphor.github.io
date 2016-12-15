package Y2016.M12.D14_proxy.simpelProxy;

public class Test {
  public static void main(String[] args) {
    // 买票服务
    ProxySeller tickeProxy = new ProxySeller(new TicketSeller());
    tickeProxy.sell(35);
  }
}
