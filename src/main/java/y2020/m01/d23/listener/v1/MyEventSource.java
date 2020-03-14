package y2020.m01.d23.listener.v1;

import java.util.Vector;

/**
 * 事件源：是事件对象的入口，包含监听器的注册、撤销、通知
 * @author adolphor
 */
public class MyEventSource {

  /** 监听器列表，监听器的注册则加入此列表 */
  private Vector<MyEventListener> ListenerList = new Vector<MyEventListener>();

  /** 注册监听器 */
  public void addListener(MyEventListener eventListener) {
    ListenerList.add(eventListener);
  }

  /** 撤销注册 */
  public void removeListener(MyEventListener eventListener) {
    ListenerList.remove(eventListener);
  }

  /** 接受外部事件 */
  public void notifyListenerEvents(MyEventObject event) {
    for (MyEventListener eventListener : ListenerList) {
      eventListener.handleEvent(event);
    }
  }
}
