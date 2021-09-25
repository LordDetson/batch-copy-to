package by.babanin.batchcopy.application;

import by.babanin.batchcopy.application.exception.TaskException;

public abstract class ValidatebleTask<R> extends AbstractTask<R>{

    public enum TaskMode {
        ACTION(ValidatebleTask::doAction), VALIDATION(ValidatebleTask::doValidation);

        private final ApplicationFunction<ValidatebleTask<?>, ?> action;

        TaskMode(ApplicationFunction<ValidatebleTask<?>, ?> action) {
            this.action = action;
        }

        @SuppressWarnings("unchecked")
        public <R> R execut(ValidatebleTask<? extends R> task) throws TaskException {
            return (R) action.apply(task);
        }
    }

    private TaskMode mode;

    @Override
    protected R body() throws TaskException {
        return mode.execut(this);
    }

    protected abstract R doAction() throws TaskException;

    protected abstract R doValidation() throws TaskException;

    public void setMode(TaskMode mode) {
        this.mode = mode;
    }

    public TaskMode getMode() {
        return mode;
    }
}
