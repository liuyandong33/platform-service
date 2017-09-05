package build.dream.platform.mappers;

import build.dream.common.saas.domains.Order;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    long insert(Order order);
    long update(Order order);
    Order find(SearchModel searchModel);
    List<Map<String, Object>> findOrderInfos(@Param("orderId") BigInteger orderId);
}
