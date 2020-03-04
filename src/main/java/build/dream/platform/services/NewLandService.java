package build.dream.platform.services;

import build.dream.common.domains.saas.NewLandAccount;
import build.dream.common.utils.CommonRedisUtils;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.JacksonUtils;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.RedisKeys;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewLandService {
    /***
     * 缓存新大陆账号
     */
    @Transactional(readOnly = true)
    public void cacheNewLandAccounts() {
        SearchModel searchModel = new SearchModel(true);
        List<NewLandAccount> newLandAccounts = DatabaseHelper.findAll(NewLandAccount.class, searchModel);

        Map<String, String> newLandAccountMap = new HashMap<String, String>();
        for (NewLandAccount newLandAccount : newLandAccounts) {
            newLandAccountMap.put(newLandAccount.getTenantId() + "_" + newLandAccount.getBranchId(), JacksonUtils.writeValueAsString(newLandAccount));
        }
        CommonRedisUtils.del(RedisKeys.KEY_NEW_LAND_ACCOUNTS);
        if (MapUtils.isNotEmpty(newLandAccountMap)) {
            CommonRedisUtils.hmset(RedisKeys.KEY_NEW_LAND_ACCOUNTS, newLandAccountMap);
        }
    }
}
