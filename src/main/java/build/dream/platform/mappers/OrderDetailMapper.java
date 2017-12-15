package build.dream.platform.mappers;

import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    long insert(OrderDetail orderDetail);
    long insertAll(List<OrderDetail> orderDetails);
    OrderDetail find(SearchModel searchModel);
    List<OrderDetail> findAll(SearchModel searchModel);
    List<OrderDetail> findAllPaged(SearchModel searchModel);
    long deleteAllByOrderIds(@Param("orderIds") List<BigInteger> orderIds, @Param("lastUpdateRemark") String lastUpdateRemark);
}
