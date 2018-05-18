package build.dream.platform.mappers;

import build.dream.common.saas.domains.AgentContractPriceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface AgentContractPriceInfoMapper {
    long insertAll(List<AgentContractPriceInfo> agentContractPriceInfos);
    List<Map<String, Object>> findAllAgentContractPriceInfos(@Param("agentContractId") BigInteger agentContractId);
}
