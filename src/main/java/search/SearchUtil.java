package search;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SearchUtil {

    private final static String parent = "/opt/homebrew/lib/node_modules/";
    private final static String keyWord = "skywalking-agent";

    public static void main(String[] args) throws IOException {
//        searchJavaName(parent);
//        searchJavaContent(parent);
        searchPathName(parent);
//        searchAllContent(parent);
//        searchDockerfileContent(parent);
    }

    private static void searchPathName(String path) throws IOException {
        recursion(new File(path), "node_modules", "pathName");
    }

    private static void searchJavaName(String path) throws IOException {
        recursion(new File(path), ".java", "fileName");
    }

    private static void searchJavaContent(String path) throws IOException {
        recursion(new File(path), ".java", "fileContent");
    }

    private static void searchAllContent(String path) throws IOException {
        recursion(new File(path), ".", "fileContent");
    }

    private static void searchDockerfileContent(String path) throws IOException {
        recursion(new File(path), "Dockerfile", "fileContent");
    }

    private static void searchYamlContent(String path) throws IOException {
        recursion(new File(path), "yaml", "fileContent");
    }

    private static void recursion(File file, String suffix, String type) throws IOException {
        if (file.isFile()) {
            if (!file.getName().contains(suffix)) {
                return;
            }
            if (type.equalsIgnoreCase("fileName")) {
                searchByName(file);
            }
            if (type.equalsIgnoreCase("fileContent")) {
                searchByContent(file);
            }
        } else if (file.isDirectory()) {
            if (type.equalsIgnoreCase("pathName")) {
                if (file.getName().contains(suffix)) {
                    System.out.println("["+ Files.getLastModifiedTime(Paths.get(file.getAbsolutePath()))+"]"+file.getAbsolutePath());;
                }
            }
            File[] files = file.listFiles();
            for (File child : files) {
                recursion(child, suffix, type);
            }
        }
    }

    private static void searchByName(File file) {
        String name = file.getName();
        if (check(keyWord, name)) {
            System.out.println(file.getAbsolutePath());
        }
    }

    private static boolean check(String keyWord, String value) {
        String[] split = keyWord.split(",");
        boolean contain = true;
        for (String key : split) {
            contain = contain && value.contains(key);
            if (!contain) {
                break;
            }
        }
        return contain;
    }

    private static void searchByContent(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (check(keyWord, line)) {
                    System.out.println(file.getAbsolutePath() + " => " + line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
