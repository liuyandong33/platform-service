package build.dream.platform.mappers;

import build.dream.common.saas.domains.Agent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper {
    long insert(Agent agent);
}
