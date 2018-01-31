package build.dream.platform.mappers;

import build.dream.common.saas.domains.OrderInfo;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderInfoMapper {
    long insert(OrderInfo orderInfo);
    long update(OrderInfo orderInfo);
    OrderInfo find(SearchModel searchModel);
    List<OrderInfo> findAll(SearchModel searchModel);
    List<OrderInfo> findAllPaged(SearchModel searchModel);
    long count(SearchModel searchModel);
}
