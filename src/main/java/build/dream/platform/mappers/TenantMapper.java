package build.dream.platform.mappers;

import build.dream.common.saas.domains.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

@Mapper
public interface TenantMapper {
    int insert(Tenant tenant);
    int update(Tenant tenant);
    Tenant findById(@Param("tenantId") BigInteger tenantId);
}
