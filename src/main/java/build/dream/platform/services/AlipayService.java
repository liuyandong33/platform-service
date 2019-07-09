package build.dream.platform.services;

import build.dream.common.beans.AlipayAccount;
import build.dream.common.saas.domains.AlipayDeveloperAccount;
import build.dream.common.utils.CommonRedisUtils;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.JacksonUtils;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AlipayMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlipayService {
    @Autowired
    private AlipayMapper alipayMapper;

    /**
     * 缓存支付宝账号
     */
    @Transactional(readOnly = true)
    public void cacheAlipayAccounts() {
        List<AlipayAccount> alipayAccounts = alipayMapper.obtainAllAlipayAccounts();
        Map<String, String> alipayAccountMap = new HashMap<String, String>();
        for (AlipayAccount alipayAccount : alipayAccounts) {
            alipayAccountMap.put(alipayAccount.getAppId(), JacksonUtils.writeValueAsString(alipayAccount));
        }
        CommonRedisUtils.del(Constants.KEY_ALIPAY_ACCOUNTS);
        if (MapUtils.isNotEmpty(alipayAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_ALIPAY_ACCOUNTS, alipayAccountMap);
        }
    }

    /**
     * 缓存支付宝开发者账号
     *
     * @return
     */
    @Transactional(readOnly = true)
    public void cacheAlipayDeveloperAccounts() {
        SearchModel searchModel = new SearchModel(true);
        List<AlipayDeveloperAccount> alipayDeveloperAccounts = DatabaseHelper.findAll(AlipayDeveloperAccount.class, searchModel);
        Map<String, String> alipayDeveloperAccountMap = new HashMap<String, String>();
        for (AlipayDeveloperAccount alipayDeveloperAccount : alipayDeveloperAccounts) {
            alipayDeveloperAccountMap.put(alipayDeveloperAccount.getAppId(), JacksonUtils.writeValueAsString(alipayDeveloperAccount));
        }
        CommonRedisUtils.del(Constants.KEY_ALIPAY_DEVELOPER_ACCOUNTS);
        if (MapUtils.isNotEmpty(alipayDeveloperAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_ALIPAY_DEVELOPER_ACCOUNTS, alipayDeveloperAccountMap);
        }
    }
}
