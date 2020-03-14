package y2020.m01.d23.publish.subscribe.v3;

/**
 * @author adolphor
 */
public interface Observer {
  void update(Object msg);
  void subscribe(Subject subject);
}
