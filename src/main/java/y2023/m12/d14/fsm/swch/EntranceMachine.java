package y2023.m12.d14.fsm.swch;

import lombok.Data;

import java.util.Objects;

@Data
public class EntranceMachine {

    private EntranceMachineState state;

    public EntranceMachine(EntranceMachineState state) {
        this.state = state;
    }

    public String execute(Action action) {
        if (Objects.isNull(action)) {
            throw new InvalidActionException("action can not be null");
        }

        if (EntranceMachineState.LOCKED.equals(state)) {
            switch (action) {
                case INSERT_COIN:
                    setState(EntranceMachineState.UNLOCKED);
                    return open();
                case PASS:
                    return alarm();
            }
        }

        if (EntranceMachineState.UNLOCKED.equals(state)) {
            switch (action) {
                case PASS:
                    setState(EntranceMachineState.LOCKED);
                    return close();
                case INSERT_COIN:
                    return refund();
            }
        }
        return null;
    }

    private String refund() {
        return "refund";
    }

    private String close() {
        return "closed";
    }

    private String alarm() {
        return "alarm";
    }

    private String open() {
        return "opened";
    }

}
