package generate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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

//  private static String postTitle = "JDK-source-code-Collection-Set-TreeSet";
  private static String postTitle = "redis-design-and-implementation";


  private static Configuration cfg;

  public static void main(String[] args) throws IOException, TemplateException {

    Map map = new HashMap();
    map.put("postTitle", postTitle);
    map.put("postDate", Utils.getPostDate());
    map.put("postId", Utils.getPostId());

    String templatePath = System.getProperty("user.dir") + "\\src\\main\\resources";
    Configuration configuration = getConfiguration(templatePath);
    Template temp = configuration.getTemplate("post-Collection.ftl");
    String url = System.getProperty("user.dir") + File.separator + "_posts"
            + File.separator + Utils.getPostTitle(postTitle)+".md";

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
        cfg.setObjectWrapper(new DefaultObjectWrapper());
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
