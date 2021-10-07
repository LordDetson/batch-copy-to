package by.babanin.batchcopy.gui.concurrent;

import by.babanin.batchcopy.application.ProgressListener;
import javafx.application.Platform;

public abstract class ProgressConcurrentListener<T> implements ProgressListener<T> {

    @Override
    public void saveProgressEnd(T progressEnd) {
        Platform.runLater(() -> storeProgressEnd(progressEnd));
    }

    protected abstract void storeProgressEnd(T progressEnd);

    @Override
    public void updateProgress(T progress) {
        Platform.runLater(() -> applyProgress(progress));
    }

    protected abstract void applyProgress(T progress);
}
