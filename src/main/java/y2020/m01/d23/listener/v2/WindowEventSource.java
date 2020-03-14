package y2020.m01.d23.listener.v2;

public class WindowEventSource extends MyEventSource {

  /** 重命名 注册监听器：addListener */
  public void onCloseWindows(MyEventListener eventListener){
    System.out.println("关注关闭窗口事件");
    ListenerList.add(eventListener);
  }

  /** 重命名 事件通知：notifyListenerEvents */
  public void doCloseWindows(){
    this.notifyListenerEvents(new MyEventObject("closeWindows"));
  }

}
