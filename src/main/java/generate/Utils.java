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

  private static String format(String format) {
    Date date = new Date();
    return format(date, format);
  }

  public static String getYear() {
    return format("YYYY");
  }

  public static String getMonth() {
    return format("MM");
  }

  public static String getDate() {
    return format("dd");
  }

  public static String getPostDate() {
    return format("YYYY-MM-dd HH:mm:ss");
  }

  public static String getPostId() {
    return format("YYYY-MM-dd-HH-mm-ss");
  }

  public static String getDateTitle(String number, String title) {
    return format("YYYY-MM-dd-") + number + "-" + title;
  }

  public static String getSourcePath() {
    String YY = format("YYYY");
    String MM = format("MM");
    String dd = format("dd");
    String src = System.getProperty("user.dir")
      + File.separator + "src"
      + File.separator + "main"
      + File.separator + "java"
      + File.separator + "y" + YY
      + File.separator + "m" + MM
      + File.separator + "d" + dd;
    return src;
  }

  public static String getImgPath(String number) {
    String YY = format("YYYY");
    String MM = format("MM");
    String dd = format("dd");
    String src = System.getProperty("user.dir")
      + File.separator + "image"
      + File.separator + "post"
      + File.separator + YY
      + File.separator + MM
      + File.separator + dd
      + File.separator + number;
    return src;
  }

  public static void main(String[] args) {
    System.out.println(getPostDate());
    System.out.println(getPostId());
  }
}
