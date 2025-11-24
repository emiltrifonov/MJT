package bg.sofia.uni.fmi.mjt.pipeline.stage;

import bg.sofia.uni.fmi.mjt.pipeline.step.Step;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class StageTest {

    @Test
    void testStartThrowsIllegalArgumentExceptionWhenInitialStepIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Stage.start(null));
    }

    @Test
    void testStartReturnsCorrectlyInitializedStage() {
        Step<Integer, Integer> step = mock();
        when(step.process(1)).thenReturn(2);

        Stage<Integer, Integer> stage = Stage.start(step);

        assertEquals(2, stage.execute(1));

        verify(step).process(1);

        verifyNoMoreInteractions(step);
    }

    @Test
    void testAddStepThrowsIllegalArgumentExceptionWhenStepIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Stage.start(mock()).addStep(null));
    }

    @Test
    void testAddStepCorrectlyAddsNonNullStep() {
        ///  ???????
    }

    @Test
    void testExecuteProcessesInCorrectOrder() {
        Step<Integer, String> first = mock();
        when(first.process(1)).thenReturn("str");

        Step<String, Integer> second = mock();
        when(second.process("str")).thenReturn(2);

        Stage<Integer, String> stage = Stage.start(first);
        stage.addStep(second);

        assertEquals(2, stage.execute(1));
    }
}