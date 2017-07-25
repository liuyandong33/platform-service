package build.dream.platform.mappers;

import build.dream.common.saas.domains.Tenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper {
    int insert(Tenant tenant);
}
