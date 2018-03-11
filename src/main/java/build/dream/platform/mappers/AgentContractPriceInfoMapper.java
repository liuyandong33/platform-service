package build.dream.platform.mappers;

import build.dream.common.saas.domains.AgentContractPriceInfo;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentContractPriceInfoMapper {
    long insertAll(List<AgentContractPriceInfo> agentContractPriceInfos);
    List<AgentContractPriceInfo> findAll(SearchModel searchModel);
}
