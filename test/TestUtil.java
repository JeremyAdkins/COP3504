import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertTrue;

public final class TestUtil {
    public static final MathContext MATH_CONTEXT = new MathContext(4, RoundingMode.HALF_EVEN);

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        BigDecimal expectedScaled = expected.round(MATH_CONTEXT);
        BigDecimal actualScaled = actual.round(MATH_CONTEXT);
        assertTrue(expectedScaled.compareTo(actualScaled) == 0);
    }

    public static void assertEquals(double expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }

    private TestUtil() {
    }
}
