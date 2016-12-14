package Y2016.M12.D14_proxy.staticProxy;

/**
 * Created by Bob on 2016/12/14.
 */
public class BookFacadeStatic implements BookFacade {

  private BookFacade facade;

  public BookFacadeStatic(BookFacade bookFacade) {
    this.facade = bookFacade;
  }

  @Override
  public void addBook() {
    System.out.println("The start of something");
    //Execution method
    facade.addBook();
    System.out.println("End of things");
  }
}