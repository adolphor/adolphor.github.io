package y2020.m01.d23.listener.v2;

/**
 * @author adolphor
 */
public class EventListenerDemo {

  public static void main(String[] args) {

    WindowEventSource windows = new WindowEventSource();
    // 关注关闭事件，实现回调接口
    windows.onCloseWindows(new MyEventListener() {
      @Override
      public void handleEvent(MyEventObject event) {
        event.doEvent();
        if (event.getSource().equals("closeWindows")) {
          System.out.println("通过onCloseWindows来关注关闭窗口事件：并执行成功。 closeWindows");
        }
      }
    });

    //窗口关闭动作，触发监听器
    windows.doCloseWindows();

  }
}
