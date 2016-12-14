package Y2016.M12.D14_proxy.cglibProxy;

/**
 * Created by Bob on 2016/12/14.
 */
public class TestCglib {
  public static void main(String[] args) {
    BookFacadeCglib cglib=new BookFacadeCglib();
    BookFacadeImpl bookCglib=(BookFacadeImpl)cglib.getInstance(new BookFacadeImpl());
    bookCglib.addBook();
  }
}