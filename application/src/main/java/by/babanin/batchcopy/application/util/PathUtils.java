package by.babanin.batchcopy.application.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class PathUtils {

    private PathUtils() {
    }

    public static List<String> validateDirectory(Path directory) {
        List<String> messages = validatePath(directory);
        if(messages.isEmpty() && !Files.isDirectory(directory)) {
            messages.add(String.format("The \"%s\" path isn't directory.", directory));
        }
        return messages;
    }

    public static List<String> validateReadableFile(Path file) {
        List<String> messages = validateFile(file);
        if(messages.isEmpty() && !Files.isReadable(file)) {
            messages.add(String.format("The \"%s\" path couldn't be read.", file));
        }
        return messages;
    }

    public static List<String> validateFile(Path file) {
        List<String> messages = validatePath(file);
        if(messages.isEmpty() && !Files.isRegularFile(file)) {
            messages.add(String.format("The \"%s\" path should be file.", file));
        }
        return messages;
    }

    public static List<String> validatePath(Path path) {
        List<String> messages = new ArrayList<>();
        if(path != null && !path.toString().isEmpty()) {
            if(Files.notExists(path)) {
                messages.add(String.format("The \"%s\" path isn't found.", path));
            }
        }
        else {
            messages.add("The path is null.");
        }
        return messages;
    }

    public static List<String> validateAlreadyExistFile(Path file) {
        List<String> messages = new ArrayList<>();
        if(validatePath(file).isEmpty()) {
            messages.add(String.format("The \"%s\" file is already exist.", file));
        }
        return messages;
    }

    public static Set<Path> resolveFileList(Path pathToResolve, List<String> fileNames) {
        return fileNames.stream()
                .filter(Objects::nonNull)
                .map(pathToResolve::resolve)
                .collect(Collectors.toSet());
    }
}
