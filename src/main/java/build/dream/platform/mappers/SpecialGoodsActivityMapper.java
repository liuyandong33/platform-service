package build.dream.platform.mappers;

import build.dream.common.saas.domains.SpecialGoodsActivity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface SpecialGoodsActivityMapper {
    long insertAll(List<SpecialGoodsActivity> specialGoodsActivities);
    List<Map<String, Object>> findSpecialGoodsActivityInfos(BigInteger activityId);
}
