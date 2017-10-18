package build.dream.platform.mappers;

import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface TenantMapper {
    int insert(Tenant tenant);
    int update(Tenant tenant);
    Tenant find(SearchModel searchModel);
    List<Tenant> findAll(SearchModel searchModel);
    List<Tenant> findAllPaged(SearchModel searchModel);
    long insertAllBranchInfos(@Param("partitionCode") String partitionCode, @Param("branchInfos") List<Map<String, Object>> branchInfos);
}
