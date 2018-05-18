package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TenantMapper {
    long insertAllBranchInfos(@Param("partitionCode") String partitionCode, @Param("branchInfos") List<Map<String, Object>> branchInfos);
}
