package build.dream.platform.mappers;

import build.dream.common.saas.domains.GoodsType;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsTypeMapper {
    long insert(GoodsType goodsType);
    long update(GoodsType goodsType);
    GoodsType find(SearchModel searchModel);
    List<GoodsType> findAll(SearchModel searchModel);
    long count(SearchModel searchModel);
    List<GoodsType> findAllPaged(SearchModel searchModel);
}
