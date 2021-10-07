package by.babanin.batchcopy.gui.action;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;

public abstract class AbstractActionHandler<T extends Event> implements ActionHandler<T>{

    private final List<ActionListener> listeners = new ArrayList<>();

    @Override
    public void addListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }

    @Override
    public void removeListener(ActionListener actionListener) {
        listeners.remove(actionListener);
    }

    @Override
    public void handle(T event) {
        body();
        listeners.forEach(ActionListener::finish);
    }

    public abstract void body();
}
