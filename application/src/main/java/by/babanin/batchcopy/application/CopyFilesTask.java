package by.babanin.batchcopy.application;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import by.babanin.batchcopy.domain.Configuration;

public class CopyFilesTask extends MultiTask<CopyTaskResult> {

    public CopyFilesTask(Configuration configuration) {
        this(ConfigurationToPathsMapConverter.convert(configuration));
    }

    public CopyFilesTask(Map<Path, Path> sourceTargetMap) {
        super(sourceTargetMap.entrySet().stream()
                .map(entry -> new CopyFileTask(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
    }
}
