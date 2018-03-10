package build.dream.platform.mappers;

import build.dream.common.saas.domains.AgentContract;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentContractMapper {
    long insert(AgentContract agentContract);
    long update(AgentContract agentContract);
    AgentContract find(SearchModel searchModel);
    long count(SearchModel searchModel);
    List<AgentContract> findAllPaged(SearchModel searchModel);
}
