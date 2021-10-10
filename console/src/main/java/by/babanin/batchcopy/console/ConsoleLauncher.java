package by.babanin.batchcopy.console;

import java.io.Console;

import by.babanin.batchcopy.application.CopyFilesTask;
import by.babanin.batchcopy.application.exception.TaskException;

public class ConsoleLauncher {

    public static void main(String[] args) {
        Console console = System.console();
        if(console == null) {
            System.err.println("No console available");
            return;
        }
        ArgumentHelper helper = new ArgumentHelper(args);
        if(helper.isValid()) {
            CopyFilesTask task = new CopyFilesTask(helper.createConfiguration());
            task.addListener(new CopyFilesTaskListener(console));
            task.addSubTaskListener(new CopyFileSubTaskListener(console));
            try {
                task.run();
            }
            catch(TaskException e) {
                // Exceptions are handled in listeners
            }
        }
        else if(helper.hasHelpArgument()) {
            helper.showHelp(console);
        }
        else {
            console.writer().println("Try command with '-help' argument for more information.");
        }
    }
}
