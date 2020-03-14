package y2020.m01.d23.listener.v2;

import java.util.EventListener;

/**
 * 监听器接口
 * @author adolphor
 */
public interface MyEventListener extends EventListener {
  void handleEvent(MyEventObject event);
}
