package by.babanin.batchcopy.gui.action;

import javafx.event.Event;
import javafx.event.EventHandler;

public interface ActionHandler<T extends Event> extends EventHandler<T> {

    void addListener(ActionListener actionListener);

    void removeListener(ActionListener actionListener);
}
