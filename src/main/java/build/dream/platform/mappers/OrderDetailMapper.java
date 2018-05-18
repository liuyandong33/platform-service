package build.dream.platform.mappers;

import build.dream.common.saas.domains.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    long insertAll(List<OrderDetail> orderDetails);
}
