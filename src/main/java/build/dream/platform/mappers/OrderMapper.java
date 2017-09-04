package build.dream.platform.mappers;

import build.dream.common.saas.domains.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    long insert(Order order);
    long update(Order order);
}
