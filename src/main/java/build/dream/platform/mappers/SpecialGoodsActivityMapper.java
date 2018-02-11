package build.dream.platform.mappers;

import build.dream.common.saas.domains.SpecialGoodsActivity;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface SpecialGoodsActivityMapper {
    long insert(SpecialGoodsActivity specialGoodsActivity);
    long insertAll(List<SpecialGoodsActivity> specialGoodsActivities);
    long update(SpecialGoodsActivity specialGoodsActivity);
    SpecialGoodsActivity find(SearchModel searchModel);
    List<SpecialGoodsActivity> findAll(SearchModel searchModel);
    List<Map<String, Object>> findSpecialGoodsActivityInfos(BigInteger activityId);
}
