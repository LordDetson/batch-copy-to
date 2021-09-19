package by.babanin.batchcopyto.application;

import java.util.Set;
import java.util.stream.Collectors;

import by.babanin.batchcopyto.domain.Configuration;
import by.babanin.batchcopyto.domain.SourceTargetItem;

public class CopyFilesTask extends MultiTask<CopyTaskResult> {

    public CopyFilesTask(Configuration configuration) {
        this(ConfigurationToSourceTargetItemConverter.convert(configuration));
    }

    public CopyFilesTask(Set<SourceTargetItem> sourceTargetItems) {
        super(sourceTargetItems.stream()
                .map(CopyFileTask::new)
                .collect(Collectors.toList()));
    }
}
