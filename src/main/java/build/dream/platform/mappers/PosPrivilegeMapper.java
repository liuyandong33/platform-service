package build.dream.platform.mappers;

import build.dream.common.saas.domains.PosPrivilege;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface PosPrivilegeMapper {
    List<PosPrivilege> findAllPosPrivileges(BigInteger userId);
}
