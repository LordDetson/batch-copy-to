package by.babanin.batchcopy.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import by.babanin.batchcopy.application.exception.TaskException;

class CopyFileTaskTest {

    private static final String SOURCE_DIRECTORY_PATH = "src/test/resources/source";
    private static final String TARGET_DIRECTORY_PATH = "src/test/resources/target";
    private static final String FILE_LIST_TO_COPY_FILE = "src/test/resources/file list to copy.txt";
    private static final String FILE_X_TXT = "file%s.txt";
    private static final String DIRECTORY_X = "directory%s";
    private static final String SOME_TEXT_X = "some text %s";

    private static Path sourceDirectory;
    private static Path targetDirectory;
    private static Path fileListToCopyFile;

    @BeforeAll
    public static void setUp() throws IOException {
        sourceDirectory = Paths.get(SOURCE_DIRECTORY_PATH);
        targetDirectory = Paths.get(TARGET_DIRECTORY_PATH);
        fileListToCopyFile = Paths.get(FILE_LIST_TO_COPY_FILE);
        Files.createDirectory(sourceDirectory);
        Files.createDirectory(targetDirectory);
        Files.createFile(fileListToCopyFile);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        Files.delete(sourceDirectory);
        Files.delete(targetDirectory);
        Files.delete(fileListToCopyFile);
    }

    @Test
    void copyFile() throws TaskException, IOException {
        // Given
        String fileName = String.format(FILE_X_TXT, 1);
        Path sourceFile = createFile(fileName, String.format(SOME_TEXT_X, 1));
        Path targetFile = targetDirectory.resolve(fileName);

        CopyFileTask task = new CopyFileTask(sourceFile, targetFile);

        // When
        task.run();

        // Check
        compareFiles(sourceFile, targetFile);

        // Clean up
        Files.delete(sourceFile);
        Files.delete(targetFile);
        Files.write(fileListToCopyFile, "".getBytes());
    }

    @Test
    void copyFileWithDirectory() throws TaskException, IOException {
        // Given
        String fileName = String.format(FILE_X_TXT, 1);
        String directory = String.format(DIRECTORY_X, 1);
        StringJoiner joiner = new StringJoiner(FileSystems.getDefault().getSeparator());
        String fileNameWithDirectory = joiner.add(directory).add(fileName).toString();
        Path sourceFile = createFile(fileNameWithDirectory, String.format(SOME_TEXT_X, 1));
        Path targetFile = targetDirectory.resolve(fileNameWithDirectory);

        CopyFileTask task = new CopyFileTask(sourceFile, targetFile);

        // When
        task.run();

        // Check
        compareFiles(sourceFile, targetFile);

        // Clean up
        Files.delete(sourceFile);
        Files.delete(sourceFile.getParent());
        Files.delete(targetFile);
        Files.delete(targetFile.getParent());
        Files.write(fileListToCopyFile, "".getBytes());
    }

    private Path createFile(String fileName, String... lines) throws IOException {
        Files.write(fileListToCopyFile, fileName.getBytes());
        Path file1Path = Paths.get(fileName);
        Path sourceFile = sourceDirectory.resolve(file1Path);
        Path parent = sourceFile.getParent();
        if(!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.createFile(sourceFile);
        Files.write(sourceFile, Arrays.asList(lines));
        return sourceFile;
    }

    private void compareFiles(Path sourceFile, Path targetFile) throws IOException {
        assertTrue(Files.exists(targetFile));
        List<String> sourceContent = Files.readAllLines(sourceFile);
        List<String> targetContent = Files.readAllLines(targetFile);
        assertEquals(sourceContent.size(), targetContent.size());
        assertEquals(sourceContent, targetContent);
    }
}