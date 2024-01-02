package Y2016.M12.D17_strategy;

public class TicketService {

  private TicketSeller seller;

  public TicketService(TicketSeller seller) {
    this.seller = seller;
  }

  public void buyTicket() {
    seller.sell();
  }

}
