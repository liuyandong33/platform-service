package build.dream.platform.mappers;

import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TenantSecretKeyMapper {
    long insert(TenantSecretKey tenantSecretKey);
    long update(TenantSecretKey tenantSecretKey);
    TenantSecretKey find(SearchModel searchModel);
    List<TenantSecretKey> findAll(SearchModel searchModel);
}
