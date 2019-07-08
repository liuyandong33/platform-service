package build.dream.platform.mappers;

import build.dream.common.beans.AlipayAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface AlipayMapper {
    List<AlipayAccount> obtainAllAlipayAccounts();

    AlipayAccount obtainAlipayAccount(@Param("tenantId") BigInteger tenantId, @Param("branchId") BigInteger branchId);
}
