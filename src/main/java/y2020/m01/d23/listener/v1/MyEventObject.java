package y2020.m01.d23.listener.v1;

import java.util.EventObject;

/**
 * 事件对象
 * @author adolphor
 */
public class MyEventObject extends EventObject {
  public MyEventObject(Object source) {
    super(source);
  }
  public void doEvent(){
    System.out.println("通知一个事件源 source :"+ this.getSource());
  }
}
