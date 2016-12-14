package Y2016.M12.D14_proxy.staticProxy;

/**
 * Created by Bob on 2016/12/14.
 */
public class TestProxy {
  public static void main(String[] args) {
    BookFacadeStatic proxy = new BookFacadeStatic(new BookFacadeImpl());
    proxy.addBook();
  }
}