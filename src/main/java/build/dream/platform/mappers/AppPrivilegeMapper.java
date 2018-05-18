package build.dream.platform.mappers;

import build.dream.common.saas.domains.AppPrivilege;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface AppPrivilegeMapper {
    List<AppPrivilege> findAllAppPrivileges(BigInteger userId);
}
