package y2023.m12.d12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LambdaCvDemo {
  public static void main(String[] args) {
    List<BusExamPaperDTO> dtoList = new ArrayList<>();
    HashMap<String, ArrayList<BusExamSessionDTO>> sessionMap = new HashMap<>();

    for (BusExamPaperDTO u : dtoList) {
      Optional.ofNullable(sessionMap.get(u.getPaperId())).ifPresentOrElse(
          sessionList -> sessionList.stream()
              .filter(t -> t.getExamStatus() == ExamStatusEnum.RUNNING.getCode())
              .findFirst()
              .ifPresentOrElse(
                  t -> {
                    u.setExamStatus((byte) ExamStatusEnum.RUNNING.getCode());
                    u.setBusExamSessionDTO(t);
                  },
                  () -> u.setExamStatus((byte) ExamStatusEnum.END.getCode())
              ),
          () -> u.setExamStatus((byte) ExamStatusEnum.NO_START.getCode())
      );
    }
  }
}