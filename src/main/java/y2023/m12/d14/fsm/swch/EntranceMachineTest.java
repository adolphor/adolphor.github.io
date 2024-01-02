package y2023.m12.d14.fsm.swch;


import cn.hutool.core.lang.Assert;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class EntranceMachineTest {

    @Test
    public void should_be_unlocked_when_insert_coin_given_a_entrance_machine_with_locked_state() {
        EntranceMachine entranceMachine = new EntranceMachine(EntranceMachineState.LOCKED);
        String result = entranceMachine.execute(Action.INSERT_COIN);
        Assert.equals(result, "opened");
        Assert.equals(entranceMachine.getState(), EntranceMachineState.UNLOCKED);
    }

    @Test
    public void should_be_locked_and_alarm_when_pass_given_a_entrance_machine_with_locked_state() {
        EntranceMachine entranceMachine = new EntranceMachine(EntranceMachineState.LOCKED);
        String result = entranceMachine.execute(Action.PASS);
        Assert.equals(result, "alarm");
        Assert.equals(entranceMachine.getState(), EntranceMachineState.LOCKED);
    }

    @Test
    public void should_fail_when_execute_invalid_action_given_a_entrance_with_locked_state() {
        EntranceMachine entranceMachine = new EntranceMachine(EntranceMachineState.LOCKED);
        InvalidActionException invalidActionException = assertThrows(InvalidActionException.class, () -> entranceMachine.execute(null));
        String expectedMessage = "action can not be null";
        String actualMessage = invalidActionException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void should_locked_when_pass_given_a_entrance_machine_with_unlocked_state() {
        EntranceMachine entranceMachine = new EntranceMachine(EntranceMachineState.UNLOCKED);
        String result = entranceMachine.execute(Action.PASS);
        Assert.equals(result, "closed");
        Assert.equals(entranceMachine.getState(), EntranceMachineState.LOCKED);
    }

    @Test
    public void should_refund_and_unlocked_when_insert_coin_given_a_entrance_machine_with_unlocked_state() {
        EntranceMachine entranceMachine = new EntranceMachine(EntranceMachineState.UNLOCKED);
        String result = entranceMachine.execute(Action.INSERT_COIN);
        Assert.equals(result, "refund");
        Assert.equals(entranceMachine.getState(), EntranceMachineState.UNLOCKED);
    }

}
