package build.dream.platform.mappers;

import build.dream.common.saas.domains.SaleFlow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleFlowMapper {
    long insertAll(List<SaleFlow> saleFlows);
}
