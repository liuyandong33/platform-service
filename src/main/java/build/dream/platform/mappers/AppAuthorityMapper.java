package build.dream.platform.mappers;

import build.dream.common.saas.domains.AppAuthority;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface AppAuthorityMapper {
    AppAuthority find(SearchModel searchModel);
    List<AppAuthority> findAll(SearchModel searchModel);
    List<AppAuthority> findAllAppAuthorities(BigInteger userId);
}
