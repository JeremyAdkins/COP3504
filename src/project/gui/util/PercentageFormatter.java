package project.gui.util;

import project.model.Bank;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.ParseException;

public final class PercentageFormatter extends JFormattedTextField.AbstractFormatter {
    public static final class Factory extends JFormattedTextField.AbstractFormatterFactory {
        @Override
        public PercentageFormatter getFormatter(JFormattedTextField tf) {
            return new PercentageFormatter();
        }
    }

    public PercentageFormatter() {
    }

    @Override
    public BigDecimal stringToValue(String text) throws ParseException {
        if (text.endsWith("%")) {
            text = text.substring(0, text.length() - 1);
        }
        try {
            return new BigDecimal(text).divide(new BigDecimal(100), Bank.MATH_CONTEXT);
        } catch (NumberFormatException nfx) {
            throw new ParseException(nfx.getMessage(), 0);
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof BigDecimal) {
            return valueToString((BigDecimal) value);
        } else {
            throw new ParseException("value is not a BigDecimal", 0);
        }
    }

    public String valueToString(BigDecimal value) {
        return String.format("%.2f%%", value.multiply(new BigDecimal("100")));
    }
}
