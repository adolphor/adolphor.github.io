package Y2016.M12.D14_proxy.staticProxy;

public class ProxySeller implements Seller {
  private Seller seller;

  public ProxySeller(Seller seller) {
    this.seller = seller;
  }

  public void sell(int price) {
    before();
    // 代理以扣除手续费之后的价格从原渠道购物
    seller.sell(price - 5);
    after();
  }

  public void back(int price) {
    before();
    // 代理把物品退给原渠道
    seller.back(price - 5);
    after();
  }

  private void before() {
    System.out.println("ProxySeller 代理收取手续费 $5");
  }

  private void after() {
    System.out.println("ProxySeller 代理完成");
  }
}
