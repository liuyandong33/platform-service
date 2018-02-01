package build.dream.platform.mappers;

import build.dream.common.saas.domains.SpecialGoodsActivity;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpecialGoodsActivityMapper {
    long insert(SpecialGoodsActivity specialGoodsActivity);
    long update(SpecialGoodsActivity specialGoodsActivity);
    SpecialGoodsActivity find(SearchModel searchModel);
    List<SpecialGoodsActivity> findAll(SearchModel searchModel);
}
