package generate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bob on 2016/8/2.
 */
public class Utils {

  public static String format(Date date, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }

  public static String getPostDate() {
    Date date = new Date();
    String pattern = "YYYY-MM-dd HH:mm:ss";
    String format = format(date, pattern);
    return format;
  }

  public static String getPostId() {
    Date date = new Date();
    String pattern = "YYYY-MM-dd-HH-mm-ss";
    String format = format(date, pattern);
    return format;
  }

  public static String getPostTitle(String title) {
    Date date = new Date();
    String pattern = "YYYY-MM-dd-";
    String format = format(date, pattern);
    return format + title;
  }

  public static String getSourcePath() {
    Date date = new Date();
    String YY = format(date, "YYYY");
    String MM = format(date, "MM");
    String src = System.getProperty("user.dir")
        + File.separator
        + "src"
        + File.separator
        + "main"
        + File.separator
        + "java"
        + File.separator
        + "Y" + YY
        + File.separator
        + "M" + MM;
    return src;
  }

  public static String getImgPath() {
    Date date = new Date();
    String YY = format(date, "YYYY");
    String MM = format(date, "MM");
    String src = System.getProperty("user.dir")
        + File.separator
        + "image"
        + File.separator
        + "post"
        + File.separator
        + YY
        + File.separator
        + MM;
    return src;
  }

  public static void main(String[] args) {
    System.out.println(getPostDate());
    System.out.println(getPostId());
  }
}
