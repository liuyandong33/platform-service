package build.dream.platform.utils;

import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.mappers.SequenceMapper;

public class SequenceUtils {
    private static SequenceMapper sequenceMapper = null;

    private static SequenceMapper obtainSequenceMapper() {
        if (sequenceMapper == null) {
            sequenceMapper = ApplicationHandler.getBean(SequenceMapper.class);
        }
        return sequenceMapper;
    }

    public static Integer nextValue(String sequenceName) {
        return obtainSequenceMapper().nextValue(sequenceName);
    }

    public static Integer currentValue(String sequenceName) {
        return obtainSequenceMapper().currentValue(sequenceName);
    }
}
