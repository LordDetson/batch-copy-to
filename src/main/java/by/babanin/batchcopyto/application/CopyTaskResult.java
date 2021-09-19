package by.babanin.batchcopyto.application;

import by.babanin.batchcopyto.domain.SourceTargetItem;
import by.babanin.batchcopyto.exception.ApplicationException;

public class CopyTaskResult {

    private final SourceTargetItem item;
    private final ApplicationException exception;

    public CopyTaskResult(SourceTargetItem item) {
        this(item, null);
    }

    public CopyTaskResult(SourceTargetItem item, ApplicationException exception) {
        this.item = item;
        this.exception = exception;
    }

    public SourceTargetItem getItem() {
        return item;
    }

    public ApplicationException getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }
}
