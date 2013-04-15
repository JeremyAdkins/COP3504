package project.gui.util;

import javax.swing.*;
import java.text.ParseException;

public final class IntegerFormatter extends JFormattedTextField.AbstractFormatter {
    public static final class Factory extends JFormattedTextField.AbstractFormatterFactory {
        @Override
        public IntegerFormatter getFormatter(JFormattedTextField tf) {
            return new IntegerFormatter();
        }
    }

    public IntegerFormatter() {
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException nfx) {
            throw new ParseException(nfx.getMessage(), 0);
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof Integer) {
            return Integer.toString((Integer) value);
        } else {
            throw new ParseException("value is not an Integer", 0);
        }
    }
}
