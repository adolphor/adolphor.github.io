package y2020.m01.d23.publish.subscribe.v5;

/**
 * @author adolphor
 */
public interface Observer {
  void update();

  void subscribe(Subject subject);
}
