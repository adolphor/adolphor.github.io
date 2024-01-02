package y2021.m08.d22.s03;

import y2021.m08.d22.s03.util.Debug;
import y2021.m08.d22.s03.util.Tools;

import java.util.Random;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/22 23:05
 */
public class AlarmAgent {
  // 保存该类的唯一实例
  private final static AlarmAgent INSTANCE = new AlarmAgent();
  // 心跳线程，用于检测告警代理与告警服务器的网络连接是否正常
  private final HeartbeatThread heartbeatThread = new HeartbeatThread();
  // 是否连接上告警服务器
  private boolean connectedToServer = false;

  private AlarmAgent() {
    // 什么也不做
  }

  public static AlarmAgent getInstance() {
    return INSTANCE;
  }

  public void init() {
    connectToServer();
    heartbeatThread.setDaemon(true);
    heartbeatThread.start();
  }

  private void connectToServer() {
    // 创建并启动网络连接线程，在该线程中与告警服务器建立连接
    new Thread(() -> doConnect()).start();
  }

  private void doConnect() {
    // 模拟实际操作耗时
    Tools.randomPause(100);
    synchronized (this) {
      connectedToServer = true;
      // 连接已经建立完毕，通知以唤醒告警发送线程
      notify();
    }
  }

  public void sendAlarm(String message) throws InterruptedException {
    synchronized (this) {
      // 使当前线程等待直到告警代理与告警服务器的连接建立完毕或者恢复
      while (!connectedToServer) {
        Debug.info("Alarm agent was not connected to server.");
        wait();
      }
      // 真正将告警消息上报到告警服务器
      doSendAlarm(message);
    }
  }

  private void doSendAlarm(String message) {
    // ...
    Debug.info("Alarm sent:%s", message);
  }

  // 心跳线程
  class HeartbeatThread extends Thread {
    @Override
    public void run() {
      try {
        // 留一定的时间给网络连接线程与告警服务器建立连接
        Thread.sleep(1000);
        while (true) {
          if (checkConnection()) {
            connectedToServer = true;
          } else {
            connectedToServer = false;
            Debug.info("Alarm agent was disconnected from server.");

            // 检测到连接中断，重新建立连接
            connectToServer();
          }
          Thread.sleep(2000);
        }
      } catch (InterruptedException e) {
        // 什么也不做;
      }
    }

    // 检测与告警服务器的网络连接情况
    private boolean checkConnection() {
      boolean isConnected = true;
      final Random random = new Random();

      // 模拟随机性的网络断链
      int rand = random.nextInt(1000);
      if (rand <= 500) {
        isConnected = false;
      }
      return isConnected;
    }
  }
}
