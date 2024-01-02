package y2023.m10.d10.skeleton.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Bob on 2016/1/1.
 */
public class StringUtil {

  public static String subLastComma(String str) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    str = str.trim();
    Integer len = str.length();
    if (len > 0 && str.lastIndexOf(",") == len - 1) {
      str = str.substring(0, len - 1);
    }
    return str;
  }

  public static String subLastComma(StringBuffer sb) {
    String str = sb.toString();
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    Integer len = str.length();
    if (len > 0 && str.lastIndexOf(",") == len - 1) {
      str = str.substring(0, len - 1);
    }
    return str;
  }

  public static void main(String[] args) {
    System.out.println(subLastComma(" asd,ww"));
    System.out.println(subLastComma("asd,ww,"));
    System.out.println(subLastComma("asd,ww, "));
    System.out.println(subLastComma("asd,ww ,"));
  }
}
