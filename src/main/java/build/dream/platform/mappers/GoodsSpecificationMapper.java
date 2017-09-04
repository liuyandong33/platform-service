package build.dream.platform.mappers;

import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsSpecificationMapper {
    List<GoodsSpecification> findAll(SearchModel searchModel);
}
