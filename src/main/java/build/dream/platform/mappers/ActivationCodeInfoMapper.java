package build.dream.platform.mappers;

import build.dream.common.saas.domains.ActivationCodeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivationCodeInfoMapper {
    long insert(ActivationCodeInfo activationCodeInfo);
    long insertAll(List<ActivationCodeInfo> activationCodeInfos);
}
