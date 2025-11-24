package bg.sofia.uni.fmi.mjt.pipeline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CacheTest {

    @Test
    void testCacheValueThrowsIllegalArgumentExceptionWhenKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Cache().cacheValue(null, null));
    }

    @Test
    void testCacheValueThrowsIllegalArgumentExceptionWhenValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Cache().cacheValue(1, null));
    }

    @Test
    void testGetCachedValueThrowsIllegalArgumentExceptionWhenKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Cache().getCachedValue(null));
    }

    @Test
    void testContainsKeyThrowsIllegalArgumentExceptionWhenKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Cache().containsKey(null));
    }
}