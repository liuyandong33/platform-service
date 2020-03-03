package build.dream.platform.mappers;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SpecialGoodsActivityMapper {
    List<Map<String, Object>> findSpecialGoodsActivityInfos(Long activityId);
}
