package by.babanin.batchcopy.console;

import java.io.Console;
import java.nio.file.Path;
import java.nio.file.Paths;

import by.babanin.batchcopy.domain.Configuration;

public class ArgumentHelper {

    private static final String HELP_ARGUMENT = "-help";
    private static final int SOURCE_DIRECTORY_INDEX = 0;
    private static final int TARGET_DIRECTORY_INDEX = 1;
    private static final int FILE_LIST_INDEX = 2;

    private final String[] args;

    public ArgumentHelper(String[] args) {
        this.args = args;
    }

    public boolean isValid() {
        return args.length == 3;
    }

    public boolean hasHelpArgument() {
        return args.length == 1 && args[0].equals(HELP_ARGUMENT);
    }

    public void showHelp(Console console) {
        StringBuilder builder = new StringBuilder();
        builder.append("Arguments: [SOURCE DIRECTORY] [TARGET DIRECTORY] [FILE LIST PATH]\n")
                .append("Utility for batch copying files to the specified path.");
        console.writer().append(builder).flush();
    }

    public Configuration createConfiguration() {
        return new Configuration(getSourceDirectory(), getTargetDirectory(), getFileListPath());
    }

    public Path getSourceDirectory() {
        return getAbsolutePath(args, SOURCE_DIRECTORY_INDEX);
    }

    public Path getTargetDirectory() {
        return getAbsolutePath(args, TARGET_DIRECTORY_INDEX);
    }

    public Path getFileListPath() {
        return getAbsolutePath(args, FILE_LIST_INDEX);
    }

    private Path getAbsolutePath(String[] args, int index) {
        return Paths.get(args[index]).toAbsolutePath();
    }
}
