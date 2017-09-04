package build.dream.platform.mappers;

import build.dream.common.saas.domains.Goods;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    long insert(Goods goods);
    List<Goods> findAll(SearchModel searchModel);
}
