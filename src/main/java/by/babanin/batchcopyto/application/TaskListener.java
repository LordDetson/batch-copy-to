package by.babanin.batchcopyto.application;

public interface TaskListener<R> {

    void doBefore();

    void doAfter(R result);
}
