package y2023.m12.d12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LambdaCvRefactor {
    public static void main(String[] args) {
        List<BusExamPaperDTO> dtoList = new ArrayList<>();
        HashMap<String, ArrayList<BusExamSessionDTO>> sessionMap = new HashMap<>();

        for (BusExamPaperDTO u : dtoList) {
            Optional.ofNullable(sessionMap.get(u.getPaperId()))
                    .map(sessionList -> sessionList.stream()
                            .filter(t -> t.getExamStatus() == ExamStatusEnum.RUNNING.getCode())
                            .findFirst()
                            .map(t -> LambdaPair.of(ExamStatusEnum.RUNNING, Optional.of(t)))
                            .orElseGet(() -> LambdaPair.of(ExamStatusEnum.END, Optional.empty())))
                    .orElseGet(() -> LambdaPair.of(ExamStatusEnum.NO_START, Optional.empty()))
                    .apply((status, sessionDTO) -> {
                        u.setExamStatus((byte) status.getCode());
                        sessionDTO.ifPresent(u::setBusExamSessionDTO);
                    });
        }

    }
}