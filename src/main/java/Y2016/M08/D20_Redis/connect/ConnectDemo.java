package Y2016.M08.D20_Redis.connect;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.Set;

/**
 * Created by Bob on 2016/8/23.
 */
public class ConnectDemo {
  public static void main(String[] args) {
    JedisShardInfo shardInfo_6379 = new JedisShardInfo("192.168.31.130", 6379);
    Jedis jedis_6379 = new Jedis(shardInfo_6379);
    Set<String> keys = jedis_6379.keys("*");
    for (String key : keys) {
      System.out.println(key);
    }
  }
}
