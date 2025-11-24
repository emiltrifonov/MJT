package bg.sofia.uni.fmi.mjt.pipeline;

import bg.sofia.uni.fmi.mjt.pipeline.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PipelineTest {
    @Test
    void testStartWhenInitialStageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Pipeline.start(null));
    }

    @Test
    void testStartCreatesPipelineWithInitialStage() {
        Stage<Integer, Integer> stage = mock();
        Pipeline<Integer, Integer> pipeline = Pipeline.start(stage);

        when(stage.execute(0)).thenReturn(1);

        assertEquals(1, pipeline.execute(0));
    }

    @Test
    void testAddStageCorrectlyAddsNewStage() {
        Stage<Integer, String> first = mock();
        Stage<String, Integer> second = mock();

        Pipeline<Integer, String> pipeline = Pipeline.start(first);
        pipeline.addStage(second);

        when(first.execute(0)).thenReturn("txt");
        when(second.execute("txt")).thenReturn(1);

        assertEquals(1, pipeline.execute(0));
    }

    @Test
    void testExecuteReturnsCachedValueWhenAvailable() {
        Stage<Integer, Integer> stage = mock();
        when(stage.execute(0)).thenReturn(1);

        Pipeline<Integer, Integer> pipeline = Pipeline.start(stage);

        assertEquals(1, pipeline.execute(0));

        assertEquals(1, pipeline.execute(0));

        verify(stage, atMostOnce()).execute(0);
    }

}