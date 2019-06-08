package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AlipayAccount;
import build.dream.common.saas.domains.AlipayAuthorizerInfo;
import build.dream.common.utils.CommonRedisUtils;
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
        String appPublicKey = saveAlipayAccountModel.getAppPublicKey();
        String appPrivateKey = saveAlipayAccountModel.getAppPrivateKey();
        String signType = saveAlipayAccountModel.getSignType();
        BigInteger userId = saveAlipayAccountModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(AlipayAccount.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition(AlipayAccount.ColumnName.BRANCH_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        AlipayAccount alipayAccount = DatabaseHelper.find(AlipayAccount.class, searchModel);
        if (alipayAccount == null) {
            alipayAccount = AlipayAccount.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .account(account)
                    .appId(appId)
                    .partnerId(partnerId)
                    .storeId(storeId)
                    .alipayPublicKey(alipayPublicKey)
                    .appPublicKey(appPublicKey)
                    .appPrivateKey(appPrivateKey)
                    .signType(signType)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增支付宝账号！")
                    .build();
            DatabaseHelper.insert(alipayAccount);
        } else {
            alipayAccount.setAccount(account);
            alipayAccount.setAppId(appId);
            alipayAccount.setPartnerId(partnerId);
            alipayAccount.setStoreId(StringUtils.isNotBlank(storeId) ? storeId : Constants.VARCHAR_DEFAULT_VALUE);
            alipayAccount.setAlipayPublicKey(alipayPublicKey);
            alipayAccount.setAppPublicKey(appPublicKey);
            alipayAccount.setAppPrivateKey(appPrivateKey);
            alipayAccount.setSignType(signType);
            alipayAccount.setUpdatedUserId(userId);
            alipayAccount.setUpdatedRemark("修改支付宝账号！");
            DatabaseHelper.update(alipayAccount);
        }

        CommonRedisUtils.hset(Constants.KEY_ALIPAY_ACCOUNTS, tenantId + "_" + branchId, GsonUtils.toJson(alipayAccount));

        return ApiRest.builder().message("保存支付宝账号成功！").successful(true).build();
    }

    @Transactional(readOnly = true)
    public List<AlipayAccount> findAllAlipayAccounts() {
        SearchModel searchModel = new SearchModel(true);
        List<AlipayAccount> alipayAccounts = DatabaseHelper.findAll(AlipayAccount.class, searchModel);
        return alipayAccounts;
    }

    @Transactional(readOnly = true)
    public List<AlipayAuthorizerInfo> findAllAlipayAuthorizerInfos() {
        SearchModel searchModel = new SearchModel(true);
        List<AlipayAuthorizerInfo> alipayAuthorizerInfos = DatabaseHelper.findAll(AlipayAuthorizerInfo.class, searchModel);
        return alipayAuthorizerInfos;
    }
}
