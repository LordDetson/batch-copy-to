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

class CopyFilesTaskTest {

    private static final String SOURCE_DIRECTORY_PATH = "src/test/resources/source";
    private static final String TARGET_DIRECTORY_PATH = "src/test/resources/target";
    private static final String FILE_LIST_TO_COPY_PATH = "src/test/resources/file list to copy.txt";
    private static final String FILE_AND_DIRECTORY_LIST_TO_COPY_PATH = "src/test/resources/file and directory list to copy.txt";

    private Path sourceDirectory;
    private Path targetDirectory;

    @BeforeEach
    void setUp() {
        sourceDirectory = Paths.get(SOURCE_DIRECTORY_PATH);
        targetDirectory = Paths.get(TARGET_DIRECTORY_PATH);
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
        Set<SourceTargetItem> items = createSourceTargetItems(FILE_LIST_TO_COPY_PATH);
        CopyFilesTask task = new CopyFilesTask(items);

        task.run();

        checkAndDeleteCopiedFile(items);
    }

    @Test
    void copyFilesWithDirectory() throws ApplicationException, IOException {
        Set<SourceTargetItem> items = createSourceTargetItems(FILE_AND_DIRECTORY_LIST_TO_COPY_PATH);
        CopyFilesTask task = new CopyFilesTask(items);

        task.run();

        checkAndDeleteCopiedFile(items);
    }

    private Set<SourceTargetItem> createSourceTargetItems(String fileListToCopyPath) {
        Path fileListToCopy = Paths.get(fileListToCopyPath);
        Set<Path> files = readFiles(fileListToCopy);
        Configuration configuration = new Configuration(sourceDirectory, targetDirectory, files);
        return ConfigurationToSourceTargetItemConverter.convert(configuration);
    }

    private void checkAndDeleteCopiedFile(Set<SourceTargetItem> items) throws IOException {
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