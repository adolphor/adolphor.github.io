package y2023.m12.d12;

public class BusExamPaperDTO {
  private String paperId;
  private byte examStatus;
  private BusExamSessionDTO busExamSessionDTO;

  public BusExamSessionDTO getBusExamSessionDTO() {
    return busExamSessionDTO;
  }

  public void setBusExamSessionDTO(BusExamSessionDTO busExamSessionDTO) {
    this.busExamSessionDTO = busExamSessionDTO;
  }

  public void setExamStatus(byte examStatus) {
    this.examStatus = examStatus;
  }

  public byte getExamStatus() {
    return examStatus;
  }

  public String getPaperId() {
    return paperId;
  }

  public void setPaperId(String paperId) {
    this.paperId = paperId;
  }
}
