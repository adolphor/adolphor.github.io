package y2023.m12.d12;

public enum ExamStatusEnum {

  RUNNING("进行中",  1),
  END("已结束",  2),
  NO_START("未开始",  3);
  ;

  private int code;
  private String name;

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  ExamStatusEnum(String name, int code) {
    this.code = code;
    this.name = name;
  }

}
