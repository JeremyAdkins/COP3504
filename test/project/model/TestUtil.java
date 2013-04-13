package project.model;

import org.junit.Assert;

import java.math.BigDecimal;

public final class TestUtil {
    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        BigDecimal expectedScaled = expected.round(Bank.MATH_CONTEXT);
        BigDecimal actualScaled = actual.round(Bank.MATH_CONTEXT);
        Assert.assertEquals(expectedScaled.doubleValue(), actualScaled.doubleValue(), 0.0001);
    }

    public static void assertEquals(double expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }

    private TestUtil() {
    }
}
