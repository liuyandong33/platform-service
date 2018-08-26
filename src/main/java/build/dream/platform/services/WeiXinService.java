package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.WeiXinAuthorizerToken;
import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.saas.domains.WeiXinPayAccount;
import build.dream.common.saas.domains.WeiXinPublicAccount;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.weixin.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class WeiXinService {
    /**
     * 删除微信开放平台应用
     *
     * @param deleteWeiXinOpenPlatformApplicationModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest deleteWeiXinOpenPlatformApplication(DeleteWeiXinOpenPlatformApplicationModel deleteWeiXinOpenPlatformApplicationModel) {
        String appId = deleteWeiXinOpenPlatformApplicationModel.getAppId();
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, appId);
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, searchModel);
        Validate.notNull(weiXinOpenPlatformApplication, "微信开放平台应用不存在！");
        weiXinOpenPlatformApplication.setDeleted(true);
        DatabaseHelper.update(weiXinOpenPlatformApplication);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinOpenPlatformApplication);
        apiRest.setClassName(WeiXinOpenPlatformApplication.class.getName());
        apiRest.setMessage("删除微信开放平台应用成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 获取微信开放平台应用
     *
     * @param obtainWeiXinOpenPlatformApplicationModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainWeiXinOpenPlatformApplication(ObtainWeiXinOpenPlatformApplicationModel obtainWeiXinOpenPlatformApplicationModel) {
        String appId = obtainWeiXinOpenPlatformApplicationModel.getAppId();
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, appId);
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, searchModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinOpenPlatformApplication);
        apiRest.setClassName(WeiXinOpenPlatformApplication.class.getName());
        apiRest.setMessage("查询微信开放平台应用成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 查询微信开放平台应用
     *
     * @param appId
     * @return
     */
    public WeiXinOpenPlatformApplication findWeiXinOpenPlatformApplication(String appId) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, appId);
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, searchModel);
        return weiXinOpenPlatformApplication;
    }

    /**
     * 保存微信公众号
     *
     * @param saveWeiXinPublicAccountModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveWeiXinPublicAccount(SaveWeiXinPublicAccountModel saveWeiXinPublicAccountModel) {
        BigInteger tenantId = saveWeiXinPublicAccountModel.getTenantId();
        String name = saveWeiXinPublicAccountModel.getName();
        String appId = saveWeiXinPublicAccountModel.getAppId();
        String appSecret = saveWeiXinPublicAccountModel.getAppSecret();
        String originalId = saveWeiXinPublicAccountModel.getOriginalId();
        BigInteger userId = saveWeiXinPublicAccountModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        WeiXinPublicAccount weiXinPublicAccount = DatabaseHelper.find(WeiXinPublicAccount.class, searchModel);
        if (weiXinPublicAccount == null) {
            weiXinPublicAccount = new WeiXinPublicAccount();
            weiXinPublicAccount.setTenantId(tenantId);
            weiXinPublicAccount.setName(name);
            weiXinPublicAccount.setAppId(appId);
            weiXinPublicAccount.setAppSecret(appSecret);
            weiXinPublicAccount.setOriginalId(originalId);
            weiXinPublicAccount.setCreateUserId(userId);
            weiXinPublicAccount.setLastUpdateUserId(userId);
            weiXinPublicAccount.setLastUpdateRemark("新增微信公众号！");
            DatabaseHelper.insert(weiXinPublicAccount);
        } else {
            weiXinPublicAccount.setName(name);
            weiXinPublicAccount.setAppId(appId);
            weiXinPublicAccount.setAppSecret(appSecret);
            weiXinPublicAccount.setOriginalId(appSecret);
            weiXinPublicAccount.setLastUpdateUserId(userId);
            weiXinPublicAccount.setLastUpdateRemark("修改微信公众号！");
            DatabaseHelper.update(weiXinPublicAccount);
        }
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinPublicAccount);
        apiRest.setMessage("保存微信公众号成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 获取微信公众号
     *
     * @param obtainWeiXinPublicAccountModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainWeiXinPublicAccount(ObtainWeiXinPublicAccountModel obtainWeiXinPublicAccountModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, obtainWeiXinPublicAccountModel.getTenantId());
        WeiXinPublicAccount weiXinPublicAccount = DatabaseHelper.find(WeiXinPublicAccount.class, searchModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinPublicAccount);
        apiRest.setClassName(WeiXinPublicAccount.class.getName());
        apiRest.setMessage("查询微信公众号成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 保存微信支付账号
     *
     * @param saveWeiXinPayAccountModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveWeiXinPayAccount(SaveWeiXinPayAccountModel saveWeiXinPayAccountModel) {
        BigInteger tenantId = saveWeiXinPayAccountModel.getTenantId();
        BigInteger branchId = saveWeiXinPayAccountModel.getBranchId();
        String appId = saveWeiXinPayAccountModel.getAppId();
        String mchId = saveWeiXinPayAccountModel.getMchId();
        String apiSecretKey = saveWeiXinPayAccountModel.getApiSecretKey();
        String subPublicAccountAppId = saveWeiXinPayAccountModel.getSubPublicAccountAppId();
        String subOpenPlatformAppId = saveWeiXinPayAccountModel.getSubOpenPlatformAppId();
        String subMiniProgramAppId = saveWeiXinPayAccountModel.getSubMiniProgramAppId();
        String rsaPublicKey = saveWeiXinPayAccountModel.getRsaPublicKey();
        String subMchId = saveWeiXinPayAccountModel.getSubMchId();
        String operationCertificate = saveWeiXinPayAccountModel.getOperationCertificate();
        String operationCertificatePassword = saveWeiXinPayAccountModel.getOperationCertificatePassword();
        SearchModel searchModel = new SearchModel(true);
        boolean acceptanceModel = saveWeiXinPayAccountModel.getAcceptanceModel();
        BigInteger userId = saveWeiXinPayAccountModel.getUserId();

        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        WeiXinPayAccount weiXinPayAccount = DatabaseHelper.find(WeiXinPayAccount.class, searchModel);
        if (weiXinPayAccount == null) {
            weiXinPayAccount = new WeiXinPayAccount();
            weiXinPayAccount.setTenantId(tenantId);
            weiXinPayAccount.setBranchId(branchId);
            weiXinPayAccount.setAppId(appId);
            weiXinPayAccount.setMchId(mchId);
            weiXinPayAccount.setApiSecretKey(apiSecretKey);
            weiXinPayAccount.setAcceptanceModel(acceptanceModel);
            if (StringUtils.isNotBlank(subPublicAccountAppId)) {
                weiXinPayAccount.setSubOpenPlatformAppId(subPublicAccountAppId);
            }

            if (StringUtils.isNotBlank(subOpenPlatformAppId)) {
                weiXinPayAccount.setSubOpenPlatformAppId(subOpenPlatformAppId);
            }

            if (StringUtils.isNotBlank(subMiniProgramAppId)) {
                weiXinPayAccount.setSubMiniProgramAppId(subMiniProgramAppId);
            }

            if (StringUtils.isNotBlank(rsaPublicKey)) {
                weiXinPayAccount.setRsaPublicKey(rsaPublicKey);
            }

            if (StringUtils.isNotBlank(subMchId)) {
                weiXinPayAccount.setSubMchId(subMchId);
            }

            if (StringUtils.isNotBlank(operationCertificate)) {
                weiXinPayAccount.setOperationCertificate(operationCertificate);
            }

            if (StringUtils.isNotBlank(operationCertificatePassword)) {
                weiXinPayAccount.setOperationCertificatePassword(operationCertificatePassword);
            }
            weiXinPayAccount.setCreateUserId(userId);
            weiXinPayAccount.setLastUpdateUserId(userId);
            weiXinPayAccount.setLastUpdateRemark("新增微信支付账号！");
            DatabaseHelper.insert(weiXinPayAccount);
        } else {
            weiXinPayAccount.setAppId(appId);
            weiXinPayAccount.setMchId(mchId);
            weiXinPayAccount.setApiSecretKey(apiSecretKey);
            weiXinPayAccount.setAcceptanceModel(acceptanceModel);
            weiXinPayAccount.setSubPublicAccountAppId(StringUtils.isNotBlank(subPublicAccountAppId) ? subPublicAccountAppId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setSubOpenPlatformAppId(StringUtils.isNotBlank(subOpenPlatformAppId) ? subOpenPlatformAppId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setSubMiniProgramAppId(StringUtils.isNotBlank(subMiniProgramAppId) ? subMiniProgramAppId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setRsaPublicKey(StringUtils.isNotBlank(rsaPublicKey) ? rsaPublicKey : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setSubMchId(StringUtils.isNotBlank(subMchId) ? subMchId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setOperationCertificate(StringUtils.isNotBlank(operationCertificate) ? operationCertificate : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setOperationCertificatePassword(StringUtils.isNotBlank(operationCertificatePassword) ? operationCertificatePassword : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setLastUpdateUserId(userId);
            weiXinPayAccount.setLastUpdateRemark("修改微信支付账号！");
            DatabaseHelper.update(weiXinPayAccount);
        }

        CacheUtils.hset(Constants.KEY_WEI_XIN_PAY_ACCOUNTS, tenantId + "_" + branchId, GsonUtils.toJson(weiXinPayAccount));
        ApiRest apiRest = new ApiRest();
        apiRest.setSuccessful(true);
        apiRest.setMessage("保存微信支付账号成功！");
        return apiRest;
    }

    @Transactional(readOnly = true)
    public List<WeiXinPayAccount> findAllWeiXinPayAccounts() {
        SearchModel searchModel = new SearchModel(true);
        List<WeiXinPayAccount> weiXinPayAccounts = DatabaseHelper.findAll(WeiXinPayAccount.class, searchModel);
        return weiXinPayAccounts;
    }

    /**
     * 保存微信授权token
     *
     * @param saveWeiXinAuthorizerTokenModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveWeiXinAuthorizerToken(SaveWeiXinAuthorizerTokenModel saveWeiXinAuthorizerTokenModel) {
        String componentAppId = saveWeiXinAuthorizerTokenModel.getComponentAppId();
        String authorizerAppId = saveWeiXinAuthorizerTokenModel.getAuthorizerAppId();
        String authorizerAccessToken = saveWeiXinAuthorizerTokenModel.getAuthorizerAccessToken();
        Integer expiresIn = saveWeiXinAuthorizerTokenModel.getExpiresIn();
        String authorizerRefreshToken = saveWeiXinAuthorizerTokenModel.getAuthorizerRefreshToken();
        Date fetchTime = saveWeiXinAuthorizerTokenModel.getFetchTime();

        WeiXinAuthorizerToken weiXinAuthorizerToken = new WeiXinAuthorizerToken();
        weiXinAuthorizerToken.setComponentAppId(componentAppId);
        weiXinAuthorizerToken.setAuthorizerAppId(authorizerAppId);
        weiXinAuthorizerToken.setAuthorizerAccessToken(authorizerAccessToken);
        weiXinAuthorizerToken.setExpiresIn(expiresIn);
        weiXinAuthorizerToken.setAuthorizerRefreshToken(authorizerRefreshToken);
        weiXinAuthorizerToken.setFetchTime(fetchTime);
        DatabaseHelper.insert(weiXinAuthorizerToken);

        return ApiRest.builder().message("保存微信授权token成功").successful(true).build();
    }

    /**
     * 查询所有微信授权token
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<WeiXinAuthorizerToken> findAllWeiXinAuthorizerTokens() {
        SearchModel searchModel = new SearchModel(true);
        return DatabaseHelper.findAll(WeiXinAuthorizerToken.class, searchModel);
    }

    @Transactional(rollbackFor = Exception.class)
    public void refreshWeiXinAuthorizerToken(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken, BigInteger id) {
        WeiXinAuthorizerToken weiXinAuthorizerToken = WeiXinUtils.apiAuthorizerToken(componentAccessToken, componentAppId, authorizerAppId, authorizerRefreshToken);
        weiXinAuthorizerToken.setId(id);
        DatabaseHelper.update(weiXinAuthorizerToken);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWeiXinAuthorizerToken(WeiXinAuthorizerToken weiXinAuthorizerToken) {
        DatabaseHelper.update(weiXinAuthorizerToken);
    }
}
