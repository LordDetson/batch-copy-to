package by.babanin.batchcopyto.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import by.babanin.batchcopyto.domain.Configuration;
import by.babanin.batchcopyto.domain.SourceTargetItem;
import by.babanin.batchcopyto.exception.ApplicationException;

import static org.junit.jupiter.api.Assertions.*;

class FilesCopyTaskTest {

    private static final String SOURCE_DIRECTORY_PATH = "src/test/resources/source";
    private static final String TARGET_DIRECTORY_PATH = "src/test/resources/target";
    private static final String FILE_LIST_TO_COPY_PATH = "src/test/resources/file list to copy.txt";

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        Path sourceDirectory = Paths.get(SOURCE_DIRECTORY_PATH);
        Path targetDirectory = Paths.get(TARGET_DIRECTORY_PATH);
        Path fileListToCopy = Paths.get(FILE_LIST_TO_COPY_PATH);
        Set<Path> files = readFiles(fileListToCopy);
        configuration = new Configuration(sourceDirectory, targetDirectory, files);
    }

    private Set<Path> readFiles(Path fileListToCopy) {
        Set<Path> files = Collections.emptySet();
        try {
            List<String> fileStrings = Files.readAllLines(fileListToCopy);
            files = fileStrings.stream()
                    .map(Paths::get)
                    .collect(Collectors.toSet());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    @Test
    void copyFiles() throws ApplicationException, IOException {
        FilesCopyTask task = new FilesCopyTask(configuration);

        task.run();

        Set<SourceTargetItem> items = ConfigurationToSourceTargetItemConverter.convert(configuration);

        for(SourceTargetItem item : items) {
            Path sourceFile = item.getSourceFile();
            Path targetFile = item.getTargetFile();
            assertTrue(Files.exists(targetFile));
            List<String> sourceContent = Files.readAllLines(sourceFile);
            List<String> targetContent = Files.readAllLines(targetFile);
            assertEquals(sourceContent.size(), targetContent.size());
            assertEquals(sourceContent, targetContent);
            Files.delete(targetFile);
        }
    }
}