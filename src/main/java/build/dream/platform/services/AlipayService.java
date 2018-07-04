package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AlipayAccount;
import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.alipay.SaveAlipayAccountModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class AlipayService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveAlipayAccount(SaveAlipayAccountModel saveAlipayAccountModel) {
        BigInteger tenantId = saveAlipayAccountModel.getTenantId();
        BigInteger branchId = saveAlipayAccountModel.getBranchId();
        String account = saveAlipayAccountModel.getAccount();
        String appId = saveAlipayAccountModel.getAppId();
        String partnerId = saveAlipayAccountModel.getPartnerId();
        String storeId = saveAlipayAccountModel.getStoreId();
        String alipayPublicKey = saveAlipayAccountModel.getAlipayPublicKey();
        String applicationPublicKey = saveAlipayAccountModel.getApplicationPublicKey();
        String applicationPrivateKey = saveAlipayAccountModel.getApplicationPrivateKey();
        String signType = saveAlipayAccountModel.getSignType();
        BigInteger userId = saveAlipayAccountModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        AlipayAccount alipayAccount = DatabaseHelper.find(AlipayAccount.class, searchModel);
        if (alipayAccount == null) {
            alipayAccount = new AlipayAccount();
            alipayAccount.setTenantId(tenantId);
            alipayAccount.setBranchId(branchId);
            alipayAccount.setAccount(account);
            alipayAccount.setAppId(appId);
            alipayAccount.setPartnerId(partnerId);
            alipayAccount.setStoreId(storeId);
            alipayAccount.setAlipayPublicKey(alipayPublicKey);
            alipayAccount.setApplicationPublicKey(applicationPublicKey);
            alipayAccount.setApplicationPrivateKey(applicationPrivateKey);
            alipayAccount.setSignType(signType);
            alipayAccount.setCreateUserId(userId);
            alipayAccount.setLastUpdateUserId(userId);
            alipayAccount.setLastUpdateRemark("新增支付宝账号！");
            DatabaseHelper.insert(alipayAccount);
        } else {
            alipayAccount.setAccount(account);
            alipayAccount.setAppId(appId);
            alipayAccount.setPartnerId(partnerId);
            alipayAccount.setStoreId(StringUtils.isNotBlank(storeId) ? storeId : Constants.VARCHAR_DEFAULT_VALUE);
            alipayAccount.setAlipayPublicKey(alipayPublicKey);
            alipayAccount.setApplicationPublicKey(applicationPublicKey);
            alipayAccount.setApplicationPrivateKey(applicationPrivateKey);
            alipayAccount.setSignType(signType);
            alipayAccount.setLastUpdateUserId(userId);
            alipayAccount.setLastUpdateRemark("修改支付宝账号！");
            DatabaseHelper.update(alipayAccount);
        }

        CacheUtils.hset(Constants.KEY_ALIPAY_ACCOUNTS, tenantId + "_" + branchId, GsonUtils.toJson(alipayAccount));

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("保存支付宝账号成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public List<AlipayAccount> findAllAlipayAccounts() {
        SearchModel searchModel = new SearchModel(true);
        List<AlipayAccount> alipayAccounts = DatabaseHelper.findAll(AlipayAccount.class, searchModel);
        return alipayAccounts;
    }
}
