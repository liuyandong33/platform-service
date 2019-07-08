package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface JDDJMapper {
    long countJDDJCodes(@Param("searchString") String searchString);

    List<Map<String, Object>> listJDDJCodes(@Param("searchString") String searchString, @Param("offset") int offset, @Param("maxResult") int maxResult);
}
