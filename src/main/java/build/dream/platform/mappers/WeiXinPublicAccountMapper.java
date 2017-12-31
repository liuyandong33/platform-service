package build.dream.platform.mappers;

import build.dream.common.saas.domains.WeiXinPublicAccount;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeiXinPublicAccountMapper {
    long insert(WeiXinPublicAccount weiXinPublicAccount);
    long update(WeiXinPublicAccount weiXinPublicAccount);
    WeiXinPublicAccount find(SearchModel searchModel);
}
