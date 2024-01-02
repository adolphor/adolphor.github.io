package Y2016.M12.D17_strategy;

public class Test {
  public static void main(String[] args) {
    TicketService internetService = new TicketService(new InternetSeller());
    internetService.buyTicket();
    TicketService windowServer = new TicketService(new WindowSeller());
    windowServer.buyTicket();
  }
}
