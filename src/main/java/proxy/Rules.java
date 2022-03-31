package proxy;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/11/30 14:01
 * @Email: 0haizhu0@gmail.com
 */
public class Rules {

  private static final Set<String> domainSet = new HashSet<>();
  private static final Set<String> domainSuffixSet = new HashSet<>();
  private static final Set<String> domainKeywordSet = new HashSet<>();
  private static final Set<String> ipCidrSet = new HashSet<>();
  private static final Set<String> urlRegexSet = new HashSet<>();
  private static final Set<String> otherSet = new HashSet<>();
  private static final Set<String> userAgentSet = new HashSet<>();

  public static void main(String[] args) throws IOException {

    String fileName = "/Users/adolphor/IdeaProjects/bob/adolphor/src/main/java/proxy/block.list";
    readUrlListToSet(fileName);

    ipCidrSet.stream().sorted().forEach(System.out::println);
    otherSet.stream().sorted().forEach(System.out::println);
    userAgentSet.stream().sorted().forEach(System.out::println);
    urlRegexSet.stream().sorted().forEach(System.out::println);
    domainKeywordSet.stream().sorted().forEach(System.out::println);
    sort(domainSet, "DOMAIN,");
    sort(domainSuffixSet, "DOMAIN-SUFFIX,");

  }

  private static void sort(Set<String> domainSet, String type) {

    ArrayList<String> tempList = new ArrayList<>(domainSet.size());

    for (String domain : domainSet) {
      domain = domain.substring(type.length());
      String[] split = domain.split("\\.");
      StringBuffer temp = new StringBuffer();
      int len = split.length;
      while (len > 0) {
        temp.append(split[--len]);
        temp.append(".");
      }
      tempList.add(temp.toString().substring(0, temp.toString().length() - 1));
    }
    tempList.stream().sorted().forEach(sb -> {
      StringBuffer domain = new StringBuffer();
      String[] split = sb.split("\\.");
      int len = split.length;
      while (len > 0) {
        domain.append(split[--len]);
        domain.append(".");
      }
      System.out.println(type + domain.substring(0, domain.toString().length() - 1));
    });
  }

  /**
   * 读取配置文件具体内容
   * @param fileName
   */
  private static void readUrlListToSet(String fileName) throws IOException {
    File file = new File(fileName);
    FileInputStream inputStream = new FileInputStream(file);
    InputStreamReader streamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(streamReader);
    String str;

    while ((str = bufferedReader.readLine()) != null) {
      if (StringUtils.isEmpty(str.trim())) {
        continue;
      }
      if (str.startsWith("DOMAIN,")) {
        domainSet.add(str);
      } else if (str.startsWith("DOMAIN-SUFFIX,")) {
        domainSuffixSet.add(str);
      } else if (str.startsWith("DOMAIN-KEYWORD,")) {
        domainKeywordSet.add(str);
      } else if (str.startsWith("URL-REGEX,")) {
        urlRegexSet.add(str);
      } else if (str.startsWith("USER-AGENT,")) {
        userAgentSet.add(str);
      } else if (str.startsWith("IP-CIDR")) {
        ipCidrSet.add(str);
      } else if (str.startsWith("AND") || str.startsWith("OR") || str.startsWith("PROCESS-NAME")) {
        otherSet.add(str);
      } else if (str.startsWith("#")) {
      } else if (str.startsWith("//")) {
      } else {
        throw new RuntimeException("地址错误：" + str);
      }
    }
    inputStream.close();
    bufferedReader.close();
  }
}