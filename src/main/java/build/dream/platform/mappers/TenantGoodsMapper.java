package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface TenantGoodsMapper {
    List<Map<String, Object>> findAllGoodsInfos(@Param("tenantId") Long tenantId, @Param("branchId") Long branchId);

    Map<String, Object> findGoodsInfo(@Param("tenantId") Long tenantId, @Param("branchId") Long branchId, @Param("goodsId") Long goodsId);

    List<Map<String, Object>> findAllExpiredBranches(@Param("expireTime") Date expireTime);
}
