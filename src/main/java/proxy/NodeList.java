package proxy;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NodeList {
    static List<String> ignoreNodes = new ArrayList<>() {{
        // 一元机场
//        add("刷新订阅");
//        add("一元机场");
        // efcloud
//        add("剩余流量");
//        add("套餐到期");
//        add("香港HKT 防失联");
    }};

    static Map<String, String> configFiles = new HashMap<>() {{
        put("一元机场.yaml", "nodes-yy.list");
        put("一分机场.yaml", "nodes-yf.list");
        put("EFCloud.yaml", "nodes-ef.list");
    }};

    private static boolean ignore(String node) {
        for (String ignoreNode : ignoreNodes) {
            if (node.contains(ignoreNode)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
        pool.scheduleAtFixedRate(() -> {
            String formatTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm => ");
            try {
                updateNodeList();
                System.out.println(formatTime + "update proxy node list success...");
            } catch (IOException e) {
                System.out.println(formatTime + "update proxy node list failed...");
                throw new RuntimeException(e);
            }
        }, 0, 12, TimeUnit.HOURS);
    }

    private static void updateNodeList() throws IOException {
        // 解析clash配置文件
        Yaml yaml = new Yaml();
        String filePath = "/Users/adolphor/.config/clash/";
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File f : files) {
            String fileName = f.getName();
            if (!configFiles.keySet().contains(fileName)) {
                continue;
            }
            FileInputStream fileInputStream = new FileInputStream(f);
            Map<String, Object> obj = yaml.load(fileInputStream);
            ArrayList proxies = (ArrayList) obj.get("proxies");
            ArrayList<String> nodes = new ArrayList<>();
            for (Object proxy : proxies) {
                HashMap map = (HashMap) proxy;
                String type = (String) map.get("type");
                if (!Objects.equals(type, "trojan")) {
                    continue;
                }
                Object name = shortName(map.get("name"));
                Object server = map.get("server");
                Object port = map.get("port");
                Object password = map.get("password");
                Object sni = map.get("sni");
                String template = "%s = trojan, %s, %s, password=%s, skip-cert-verify=true, sni=%s\n";
                String node = String.format(template, name, server, port, password, sni);
                System.out.println(node);
                nodes.add(node);
            }
            // 写入surge配置文件
            String nodeListFile = configFiles.get(fileName);
            String fileConf = "/Users/adolphor/IdeaProjects/my-private/proxy/config/surge/node-list/" + nodeListFile;
            if (nodes.size() > 3) {
                File conf = new File(fileConf);
                conf.delete();
                conf.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(conf));
                for (String node : nodes) {
                    if (ignore(node)) {
                        continue;
                    }
                    out.write(node);
                }
                out.flush();
                out.close();
            }
        }
    }

    private static String shortName(Object name){
        return name.toString().replace(" ","")
//                .replace("\uD83C\uDDF8\uD83C\uDDEC","")
//                .replace("\uD83C\uDDF9\uD83C\uDDFC","")
//                .replace("\uD83C\uDDED\uD83C\uDDF0","")
//                .replace("\uD83C\uDDEF\uD83C\uDDF5","")
//                .replace("\uD83C\uDDF9\uD83C\uDDF7","")
//                .replace("\uD83C\uDDFA\uD83C\uDDF8","")
                .replace("","")
                .replace("|专线","")
                .replace("倍率","")
                .replace("香港","港")
                .replace("台湾","台")
                .replace("美国","美")
                .replace("日本","日")
                .replace("韩国","韩")
                .replace("印度","印")
                .replace("加拿大","加")
                .replace("新加坡","新")
                .replace("阿根廷","阿")
                .replace("土耳其","土");
    }

}
