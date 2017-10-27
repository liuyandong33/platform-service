package build.dream.platform.mappers;

import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WeiXinOpenPlatformApplicationMapper {
    long insert(WeiXinOpenPlatformApplication weiXinOpenPlatformApplication);
    long update(WeiXinOpenPlatformApplication weiXinOpenPlatformApplication);
    WeiXinOpenPlatformApplication find(SearchModel searchModel);
    List<WeiXinOpenPlatformApplication> findAll(SearchModel searchModel);
}
