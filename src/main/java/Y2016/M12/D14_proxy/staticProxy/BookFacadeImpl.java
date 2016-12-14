package Y2016.M12.D14_proxy.staticProxy;

/**
 * Created by Bob on 2016/12/14.
 */
public class BookFacadeImpl implements BookFacade {
  @Override
  public void addBook() {
    System.out.println("Books added method. . . ");
  }
}
