package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface TenantGoodsMapper {
    List<Map<String, Object>> findAllGoodsInfos(@Param("tenantId") BigInteger tenantId, @Param("branchId") BigInteger branchId);
    Map<String, Object> findGoodsInfo(@Param("tenantId") BigInteger tenantId, @Param("branchId") BigInteger branchId, @Param("goodsId") BigInteger goodsId);
    List<Map<String, Object>> findAllExpiredBranches(@Param("expireTime") Date expireTime);
}
