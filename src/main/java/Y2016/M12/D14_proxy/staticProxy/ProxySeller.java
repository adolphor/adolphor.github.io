package Y2016.M12.D14_proxy.staticProxy;

public class ProxySeller {
  private Seller seller;

  public ProxySeller(Seller seller) {
    this.seller = seller;
  }

  public void sell(int price) {
    // 代理扣除手续费（手续费设为 $5）
    beforeSell(5);
    // 代理以扣除手续费之后的价格从原渠道购物
    seller.sell(price - 5);
    // 代理把物品给客户
    afterSell();
  }

  private void beforeSell(int price) {
    System.out.println("ProxySeller 代理扣除手续费 $5");
  }

  private void afterSell() {
    System.out.println("ProxySeller 代理把物品给客户，代购完成.");
  }
}