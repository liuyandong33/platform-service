package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SequenceMapper {
    Integer nextValue(@Param("sequenceName") String sequenceName);
    Integer currentValue(@Param("sequenceName") String sequenceName);
}
