package build.dream.platform.mappers;

import build.dream.common.saas.domains.Agent;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper {
    long insert(Agent agent);
    Agent find(SearchModel searchModel);
}
