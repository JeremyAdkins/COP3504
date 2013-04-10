package project.model;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public final class TestUtil {
    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        BigDecimal expectedScaled = expected.round(Bank.MATH_CONTEXT);
        BigDecimal actualScaled = actual.round(Bank.MATH_CONTEXT);
        assertTrue(expectedScaled.compareTo(actualScaled) == 0);
    }

    public static void assertEquals(double expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }

    private TestUtil() {
    }
}
