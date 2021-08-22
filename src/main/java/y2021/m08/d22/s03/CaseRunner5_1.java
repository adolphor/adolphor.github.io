package y2021.m08.d22.s03;

import y2021.m08.d22.s03.util.Tools;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/22 23:53
 */
public class CaseRunner5_1 {
  final static AlarmAgent alarmAgent;

  static {
    alarmAgent = AlarmAgent.getInstance();
    alarmAgent.init();
  }

  public static void main(String[] args) throws InterruptedException {

    alarmAgent.sendAlarm("Database offline!");
    Tools.randomPause(12000);
    alarmAgent.sendAlarm("XXX service unreachable!");
  }
}
