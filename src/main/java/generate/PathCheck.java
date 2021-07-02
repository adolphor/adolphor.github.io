package generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/7/2 11:26
 */
public class PathCheck {
  static final String rootPath = "/Users/adolphor/IdeaProjects/bob/adolphor/_posts";
  static final String preFlag = "]({% post_url ";
  static final String endFlag = " %}";
  static final HashMap<String, String> map = new HashMap<>();

  public static void main(String[] args) {
    File rootFile = new File(rootPath);
    prepareMap(rootFile);
    System.out.println(map.size());
    checkUrl(rootFile);
  }

  private static void prepareMap(File rootFile) {
    File[] files = rootFile.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        String name = file.getName();
        if (name.endsWith(".md")) {
          name = name.substring(0, name.length() - 3);
          String absolutePath = file.getAbsolutePath();
          String pathName = absolutePath.substring(rootPath.length());
          String urlName = pathName.substring(1, pathName.length() - 3);
          map.put(name, urlName);
        }
      } else {
        prepareMap(file);
      }
    }
  }


  private static void checkUrl(File rootFile) {
    File[] files = rootFile.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        String name = file.getName();
        if (name.endsWith(".md")) {
          try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuffer buf = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
              int index = line.indexOf(preFlag);
              if (index > 0) {
                System.out.println(line);
                int preIndex = index + preFlag.length();
                String preUrl = line.substring(0, preIndex);
                int endIndex = line.indexOf(endFlag);
                String endUrl = line.substring(endIndex);
                String oldPathFile = line.substring(preIndex, endIndex);
                String[] split = oldPathFile.split("/");
                String fileName = split[split.length-1];
                String newUrl = preUrl + map.get(fileName) + endUrl;
                System.out.println(newUrl);
                System.out.println("===================================================");
                buf.append(newUrl);
              } else {
                buf.append(line);
              }
              buf.append(System.getProperty("line.separator"));
            }
            writeUrl(file,buf);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        checkUrl(file);
      }
    }
  }

  private static void writeUrl(File file, StringBuffer sb){
    try(BufferedWriter bfw = new BufferedWriter(new FileWriter(file))) {
      bfw.write(sb.toString());
      bfw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
