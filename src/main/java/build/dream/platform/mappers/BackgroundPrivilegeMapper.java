package build.dream.platform.mappers;

import build.dream.common.domains.saas.BackgroundPrivilege;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface BackgroundPrivilegeMapper {
    List<BackgroundPrivilege> findAllBackgroundPrivileges(BigInteger userId);
}
