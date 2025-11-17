package bg.sofia.uni.fmi.mjt.pipeline.stage;

import bg.sofia.uni.fmi.mjt.pipeline.step.Step;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
    }

    @Test
    void testAddStepThrowsIllegalArgumentExceptionWhenStepIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Stage.start(mock()).addStep(null));
    }

    @Test
    void testProcessCorrectlyAddsNonNullStep() {
        Step<Integer, Integer> step = mock();
        when(step.process(1)).thenReturn(2);

        Step<Integer, String> stepToAdd = mock();
        when(stepToAdd.process(2)).thenReturn("3");

        Stage<Integer, Integer> stage = Stage.start(step);
        stage.addStep(stepToAdd);

        assertEquals("3", stage.execute(1));
    }

    @Test
    void execute() {
    }
}