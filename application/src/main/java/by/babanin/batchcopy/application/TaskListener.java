package by.babanin.batchcopy.application;

public interface TaskListener<R> {

    void doBefore();

    void doAfter(R result);
}
