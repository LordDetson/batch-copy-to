package by.babanin.batchcopy.application;

public interface ProgressListener<T> {

    void saveProgressEnd(T progressEnd);

    void updateProgress(T progress);
}
