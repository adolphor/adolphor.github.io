package Y2016.M12.D14_proxy.dynamicProxy;

/**
 * Created by Bob on 2016/12/14.
 */
public class TestProxy {
  public static void main(String[] args) {
    BookFacadeProxy proxy = new BookFacadeProxy();
    BookFacade bookProxy = (BookFacade) proxy.newInstance(new BookFacadeImpl());
    bookProxy.addBook();
  }
}