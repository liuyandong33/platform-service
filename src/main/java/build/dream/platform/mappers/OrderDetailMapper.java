package build.dream.platform.mappers;

import build.dream.common.saas.domains.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    long insertAll(List<OrderDetail> orderDetails);
    long deleteAllByOrderIds(@Param("orderIds") List<BigInteger> orderIds, @Param("lastUpdateRemark") String lastUpdateRemark);
}
