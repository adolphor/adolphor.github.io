package generate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 2016/8/2.
 */
public class Generator {

  private static String postTitle = "Java并发包学习汇总目录";
  private static String urlTitle = "java-util-concurrent-menus";
  private static String categories = "[concurrent]";
  private static String tags = "[concurrent]";
  private static Configuration cfg;

  public static void main(String[] args) throws IOException, TemplateException {

    Map map = new HashMap();
    map.put("postTitle", postTitle);
    map.put("postDate", Utils.getPostDate());
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
    String url = System.getProperty("user.dir") + File.separator + "_posts"
        + File.separator + Utils.getPostTitle(urlTitle) + ".md";

    File file = new File(url);
    createFile(file);
    Writer out = new FileWriter(file);
    temp.process(map, out);
    temp.process(map, new OutputStreamWriter(System.out));
    out.flush();

    File sourse = new File(Utils.getSourcePath());
    makeDir(sourse);
    File img = new File(Utils.getImgPath());
    makeDir(img);

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
