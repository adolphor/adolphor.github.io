package generate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bob
 * @date 2016/8/2
 */
public class Generator {

  private static String postTitle = "Java基础知识目录";
  private static String urlTitle = "static-keyword";
  private static String categories = "[Java]";
  private static String tags = "[Java]";
  private static String folder = "java" + File.separator + "basic" + File.separator + "content";

  private static Configuration cfg;

  public static void main(String[] args) throws TemplateException, IOException {
    build(postTitle,urlTitle,categories,tags,folder);
  }

  protected static void build(String postTitle,
                       String urlTitle,
                       String categories,
                       String tags,
                       String folder
                       ) throws IOException, TemplateException {

    String number = getSeqNum();

    Map map = new HashMap();
    map.put("postTitle", postTitle);
    map.put("postDate", Utils.getPostDate());
    map.put("year", Utils.getYear());
    map.put("month", Utils.getMonth());
    map.put("date", Utils.getDate());
    map.put("number", number);
    map.put("postId", Utils.getPostId());
    map.put("categories", categories);
    map.put("tags", tags);

    StringBuffer stringBuffer = new StringBuffer(System.getProperty("user.dir"));
    String templatePath = stringBuffer.append(File.separator)
      .append("src")
      .append(File.separator)
      .append("main")
      .append(File.separator)
      .append("resources")
      .toString();
    Configuration configuration = getConfiguration(templatePath);
    Template temp = configuration.getTemplate("post.ftl");
    String dateTitle = Utils.getDateTitle(number, urlTitle);
    String url = System.getProperty("user.dir")
      + File.separator + "_posts"
      + File.separator + folder
      + File.separator + dateTitle + ".md";

    String postUrl = folder + File.separator + dateTitle;
    String postLink = "[" + postTitle + "]({% post_url " + postUrl + " %})";
    System.out.println(postLink);
    map.put("postLink", postLink);

    File file = new File(url);
    createFile(file);
    Writer out = new FileWriter(file);
    temp.process(map, out);

    temp.process(map, new OutputStreamWriter(System.out));
    out.flush();

    File source = new File(Utils.getSourcePath());
    makeDir(source);
    File img = new File(Utils.getImgPath(number));
    makeDir(img);
  }

  protected static String getSeqNum() throws IOException {
    String format = DateFormatUtils.format(new Date(), "yyyyMMdd");
    String seq;
    File file = new File("./num_temp.md");
    if (file.exists()) {
      FileReader reader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(reader);
      String dateNum = bufferedReader.readLine();
      if (format.equals(dateNum)) {
        String num = bufferedReader.readLine();
        seq = wrapperNum(Integer.valueOf(num) + 1);
      } else {
        seq = wrapperNum(1);
      }
    } else {
      seq = wrapperNum(1);
      file.createNewFile();
    }
    FileWriter fileWriter = new FileWriter(file);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write(format);
    bufferedWriter.write("\n");
    bufferedWriter.write(seq);
    bufferedWriter.flush();
    return seq;
  }

  private static String wrapperNum(Integer num) {
    String seqNum = String.valueOf(num);
    while (seqNum.length() < 2) {
      seqNum = "0" + seqNum;
    }
    return seqNum;
  }

  public static Configuration getConfiguration(String templatePath) {
    if (cfg == null) {
      cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
      File file = new File(templatePath);
      try {
        cfg.setDirectoryForTemplateLoading(file);
        cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(new Version("2.3.23")).build());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return cfg;
  }

  public static boolean createFile(File file) throws IOException {
    if (!file.exists()) {
      makeDir(file.getParentFile());
    }
    return file.createNewFile();
  }

  public static void makeDir(File dir) {
    if (!dir.getParentFile().exists()) {
      makeDir(dir.getParentFile());
    }
    dir.mkdir();
  }


}
