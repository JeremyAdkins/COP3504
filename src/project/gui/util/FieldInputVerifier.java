package project.gui.util;

import project.gui.AbstractUserWindow;
import project.model.InvalidInputException;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.ParseException;

public abstract class FieldInputVerifier extends InputVerifier {
    private final AbstractUserWindow window;

    public FieldInputVerifier(AbstractUserWindow window) {
        this.window = window;
    }

    @Override
    public boolean verify(JComponent input) {
        try {
            ((JFormattedTextField) input).commitEdit();
            setField((BigDecimal) ((JFormattedTextField) input).getValue());
            return true;
        } catch (InvalidInputException iix) {
            window.controller.handleException(window, iix);
            return false;
        } catch (ParseException px) {
            return false;
        }
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        return verify(input);
    }

    protected abstract void setField(BigDecimal value) throws InvalidInputException;
}
