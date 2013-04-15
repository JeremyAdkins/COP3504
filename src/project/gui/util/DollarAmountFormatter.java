package project.gui.util;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.ParseException;

public final class DollarAmountFormatter extends JFormattedTextField.AbstractFormatter {
    public static final class Factory extends JFormattedTextField.AbstractFormatterFactory {
        private final boolean acceptsNull;

        public Factory() {
            this(false);
        }

        public Factory(boolean acceptsNull) {
            this.acceptsNull = acceptsNull;
        }

        @Override
        public DollarAmountFormatter getFormatter(JFormattedTextField tf) {
            return new DollarAmountFormatter(acceptsNull);
        }
    }

    private final boolean acceptsNull;

    public DollarAmountFormatter() {
        this(false);
    }

    public DollarAmountFormatter(boolean acceptsNull) {
        this.acceptsNull = acceptsNull;
    }

    @Override
    public BigDecimal stringToValue(String text) throws ParseException {
        if (acceptsNull && (text == null || text.isEmpty())) {
            return null;
        } else {
            if (text.startsWith("$")) {
                text = text.substring(1);
            }
            try {
                return new BigDecimal(text);
            } catch (NumberFormatException nfx) {
                throw new ParseException(nfx.getMessage(), 0);
            }
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null || value instanceof BigDecimal) {
            return valueToString((BigDecimal) value);
        } else {
            throw new ParseException("value is not a BigDecimal", 0);
        }
    }

    public String valueToString(BigDecimal value) {
        if (acceptsNull && value == null) {
            return "";
        } else {
            return String.format("$%.2f", value);
        }
    }
}
