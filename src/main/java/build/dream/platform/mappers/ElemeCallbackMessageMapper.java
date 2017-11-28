package build.dream.platform.mappers;

import build.dream.common.saas.domains.ElemeCallbackMessage;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ElemeCallbackMessageMapper {
    long insert(ElemeCallbackMessage elemeCallbackMessage);
    ElemeCallbackMessage find(SearchModel searchModel);
    List<ElemeCallbackMessage> findAll(SearchModel searchModel);
    long update(ElemeCallbackMessage elemeCallbackMessage);
}
