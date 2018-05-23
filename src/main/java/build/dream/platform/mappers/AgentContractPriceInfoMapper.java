package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface AgentContractPriceInfoMapper {
    List<Map<String, Object>> findAllAgentContractPriceInfos(@Param("agentContractId") BigInteger agentContractId);
}
