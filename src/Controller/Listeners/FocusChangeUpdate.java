package Controller.Listeners;

import View.Observer.Observed;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusChangeUpdate implements FocusListener {
    private final Observed observed;

    public FocusChangeUpdate(Observed observed) {
        this.observed = observed;
    }

    @Override
    public void focusGained(FocusEvent e) {
        observed.notifyObservers();
    }

    @Override
    public void focusLost(FocusEvent e) {
          observed.notifyObservers();
    }
}
