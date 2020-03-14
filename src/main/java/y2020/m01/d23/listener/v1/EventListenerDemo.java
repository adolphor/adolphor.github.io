package y2020.m01.d23.listener.v1;

/**
 * @author adolphor
 */
public class EventListenerDemo {

  public static void main(String[] args) {
    MyEventSource eventSource = new MyEventSource();
    eventSource.addListener(new MyEventListener() {
      @Override
      public void handleEvent(MyEventObject event) {
        event.doEvent();
        if (event.getSource().equals("closeWindows")) {
          System.out.println("doClose something...");
        } else if (event.getSource().equals("openWindows")) {
          System.out.println("doOpen something...");
        }
      }
    });
    // 传入openWindows事件，通知listener，事件监听器，对open事件感兴趣的listener将会执行
    eventSource.notifyListenerEvents(new MyEventObject("openWindows"));
  }
}
