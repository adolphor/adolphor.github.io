package Y2016.M12.D14_proxy.simpelProxy;

public class ProxySeller {
  private TicketSeller seller;

  public ProxySeller(TicketSeller seller) {
    this.seller = seller;
  }

  public void sell(int price) {
    before();
    // 代理以扣除手续费之后的价格从原渠道购物
    seller.sell(price - 5);
    after();
  }

  private void before() {
    System.out.println("ProxySeller 代理收取手续费 $5");
  }

  private void after() {
    System.out.println("ProxySeller 代理完成");
  }
}
