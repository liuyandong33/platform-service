package build.dream.platform.mappers;

import build.dream.common.beans.AlipayAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlipayMapper {
    List<AlipayAccount> obtainAllAlipayAccounts();
}
