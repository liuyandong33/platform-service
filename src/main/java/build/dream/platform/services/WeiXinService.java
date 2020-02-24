package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.beans.ComponentAccessToken;
import build.dream.common.domains.saas.WeiXinAuthorizerInfo;
import build.dream.common.domains.saas.WeiXinAuthorizerToken;
import build.dream.common.domains.saas.WeiXinOpenPlatformApplication;
import build.dream.common.domains.saas.WeiXinPayAccount;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.weixin.HandleAuthCallbackModel;
import build.dream.platform.models.weixin.ObtainWeiXinMiniProgramsModel;
import build.dream.platform.models.weixin.ObtainWeiXinPublicAccountModel;
import build.dream.platform.models.weixin.SaveWeiXinPayAccountModel;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeiXinService {
    /**
     * 查询微信开放平台应用
     *
     * @param appId
     * @return
     */
    @Transactional(readOnly = true)
    public WeiXinOpenPlatformApplication obtainWeiXinOpenPlatformApplication(String appId) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(WeiXinOpenPlatformApplication.ColumnName.APP_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, appId);
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, searchModel);
        return weiXinOpenPlatformApplication;
    }

    /**
     * 获取已授权的微信公众号
     *
     * @param obtainWeiXinPublicAccountModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainWeiXinPublicAccount(ObtainWeiXinPublicAccountModel obtainWeiXinPublicAccountModel) {
        BigInteger tenantId = obtainWeiXinPublicAccountModel.getTenantId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(WeiXinAuthorizerInfo.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition(WeiXinAuthorizerInfo.ColumnName.AUTHORIZER_TYPE, Constants.SQL_OPERATION_SYMBOL_EQUAL, Constants.AUTHORIZER_TYPE_PUBLIC_ACCOUNT);
        WeiXinAuthorizerInfo weiXinAuthorizerInfo = DatabaseHelper.find(WeiXinAuthorizerInfo.class, searchModel);
        return ApiRest.builder().data(weiXinAuthorizerInfo).className(WeiXinAuthorizerInfo.class.getName()).message("查询微信公众号成功！").successful(true).build();
    }

    /**
     * 获取已授权的微信小程序
     *
     * @param obtainWeiXinMiniProgramsModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainWeiXinMiniPrograms(ObtainWeiXinMiniProgramsModel obtainWeiXinMiniProgramsModel) {
        BigInteger tenantId = obtainWeiXinMiniProgramsModel.getTenantId();
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(WeiXinAuthorizerInfo.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition(WeiXinAuthorizerInfo.ColumnName.AUTHORIZER_TYPE, Constants.SQL_OPERATION_SYMBOL_EQUAL, Constants.AUTHORIZER_TYPE_MINI_PROGRAM);
        List<WeiXinAuthorizerInfo> weiXinAuthorizerInfos = DatabaseHelper.findAll(WeiXinAuthorizerInfo.class, searchModel);
        return ApiRest.builder().data(weiXinAuthorizerInfos).message("获取微信小程序信息成功！").successful(true).build();
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
        String apiKey = saveWeiXinPayAccountModel.getApiKey();
        String subPublicAccountAppId = saveWeiXinPayAccountModel.getSubPublicAccountAppId();
        String subOpenPlatformAppId = saveWeiXinPayAccountModel.getSubOpenPlatformAppId();
        String subMiniProgramAppId = saveWeiXinPayAccountModel.getSubMiniProgramAppId();
        String rsaPublicKey = saveWeiXinPayAccountModel.getRsaPublicKey();
        String subMchId = saveWeiXinPayAccountModel.getSubMchId();
        String operationCertificate = saveWeiXinPayAccountModel.getOperationCertificate();
        String operationCertificatePassword = saveWeiXinPayAccountModel.getOperationCertificatePassword();
        SearchModel searchModel = new SearchModel(true);
        boolean acceptanceModel = saveWeiXinPayAccountModel.getAcceptanceModel();
        String apiV3Key = saveWeiXinPayAccountModel.getApiV3Key();
        BigInteger userId = saveWeiXinPayAccountModel.getUserId();

        searchModel.addSearchCondition(WeiXinPayAccount.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        searchModel.addSearchCondition(WeiXinPayAccount.ColumnName.BRANCH_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        WeiXinPayAccount weiXinPayAccount = DatabaseHelper.find(WeiXinPayAccount.class, searchModel);
        if (weiXinPayAccount == null) {
            weiXinPayAccount = new WeiXinPayAccount();
            weiXinPayAccount.setTenantId(tenantId);
            weiXinPayAccount.setBranchId(branchId);
            weiXinPayAccount.setAppId(appId);
            weiXinPayAccount.setMchId(mchId);
            weiXinPayAccount.setApiKey(apiKey);
            weiXinPayAccount.setAcceptanceModel(acceptanceModel);
            weiXinPayAccount.setApiV3Key(apiV3Key);
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
            weiXinPayAccount.setCreatedUserId(userId);
            weiXinPayAccount.setUpdatedUserId(userId);
            weiXinPayAccount.setUpdatedRemark("新增微信支付账号！");
            DatabaseHelper.insert(weiXinPayAccount);
        } else {
            weiXinPayAccount.setAppId(appId);
            weiXinPayAccount.setMchId(mchId);
            weiXinPayAccount.setApiKey(apiV3Key);
            weiXinPayAccount.setAcceptanceModel(acceptanceModel);
            weiXinPayAccount.setApiV3Key(apiV3Key);
            weiXinPayAccount.setSubPublicAccountAppId(StringUtils.isNotBlank(subPublicAccountAppId) ? subPublicAccountAppId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setSubOpenPlatformAppId(StringUtils.isNotBlank(subOpenPlatformAppId) ? subOpenPlatformAppId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setSubMiniProgramAppId(StringUtils.isNotBlank(subMiniProgramAppId) ? subMiniProgramAppId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setRsaPublicKey(StringUtils.isNotBlank(rsaPublicKey) ? rsaPublicKey : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setSubMchId(StringUtils.isNotBlank(subMchId) ? subMchId : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setOperationCertificate(StringUtils.isNotBlank(operationCertificate) ? operationCertificate : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setOperationCertificatePassword(StringUtils.isNotBlank(operationCertificatePassword) ? operationCertificatePassword : Constants.VARCHAR_DEFAULT_VALUE);
            weiXinPayAccount.setUpdatedUserId(userId);
            weiXinPayAccount.setUpdatedRemark("修改微信支付账号！");
            DatabaseHelper.update(weiXinPayAccount);
        }

        CommonRedisUtils.hset(Constants.KEY_WEI_XIN_PAY_ACCOUNTS, tenantId + "_" + branchId, GsonUtils.toJson(weiXinPayAccount));
        return ApiRest.builder().message("保存微信支付账号成功！").successful(true).build();
    }

    /**
     * 缓存微信支付账号
     */
    @Transactional(readOnly = true)
    public void cacheWeiXinPayAccounts() {
        SearchModel searchModel = new SearchModel(true);
        List<WeiXinPayAccount> weiXinPayAccounts = DatabaseHelper.findAll(WeiXinPayAccount.class, searchModel);
        Map<String, String> weiXinPayAccountMap = new HashMap<String, String>();
        Map<String, String> weiXinPayApiV3KeyMap = new HashMap<String, String>();
        for (WeiXinPayAccount weiXinPayAccount : weiXinPayAccounts) {
            weiXinPayAccountMap.put(weiXinPayAccount.getTenantId() + "_" + weiXinPayAccount.getBranchId(), JacksonUtils.writeValueAsString(weiXinPayAccount));
            weiXinPayApiV3KeyMap.put(weiXinPayAccount.getAppId(), weiXinPayAccount.getApiV3Key());
        }
        CommonRedisUtils.del(Constants.KEY_WEI_XIN_PAY_ACCOUNTS);
        if (MapUtils.isNotEmpty(weiXinPayAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_WEI_XIN_PAY_ACCOUNTS, weiXinPayAccountMap);
        }

        CommonRedisUtils.del(Constants.KEY_WEI_XIN_PAY_API_V3_KEYS);
        if (MapUtils.isNotEmpty(weiXinPayApiV3KeyMap)) {
            CommonRedisUtils.hmset(Constants.KEY_WEI_XIN_PAY_API_V3_KEYS, weiXinPayApiV3KeyMap);
        }
    }

    /**
     * 查询所有微信授权token
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<WeiXinAuthorizerToken> obtainAllWeiXinAuthorizerTokens() {
        SearchModel searchModel = new SearchModel(true);
        return DatabaseHelper.findAll(WeiXinAuthorizerToken.class, searchModel);
    }

    /**
     * 缓存微信授权token
     */
    @Transactional(readOnly = true)
    public void cacheWeiXinAuthorizerTokens() {
        SearchModel searchModel = new SearchModel(true);
        List<WeiXinAuthorizerToken> weiXinAuthorizerTokens = DatabaseHelper.findAll(WeiXinAuthorizerToken.class, searchModel);
        Map<String, String> weiXinAuthorizerTokenMap = new HashMap<String, String>();
        for (WeiXinAuthorizerToken weiXinAuthorizerToken : weiXinAuthorizerTokens) {
            weiXinAuthorizerTokenMap.put(weiXinAuthorizerToken.getComponentAppId() + "_" + weiXinAuthorizerToken.getAuthorizerAppId(), JacksonUtils.writeValueAsString(weiXinAuthorizerToken));
        }

        CommonRedisUtils.del(Constants.KEY_WEI_XIN_AUTHORIZER_TOKENS);
        if (MapUtils.isNotEmpty(weiXinAuthorizerTokenMap)) {
            CommonRedisUtils.hmset(Constants.KEY_WEI_XIN_AUTHORIZER_TOKENS, weiXinAuthorizerTokenMap);
        }
    }

    /**
     * 刷新微信授权token
     *
     * @param componentAccessToken
     * @param weiXinAuthorizerToken
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public WeiXinAuthorizerToken refreshWeiXinAuthorizerToken(String componentAccessToken, WeiXinAuthorizerToken weiXinAuthorizerToken) {
        WeiXinAuthorizerToken newWeiXinAuthorizerToken = WeiXinUtils.apiAuthorizerToken(componentAccessToken, weiXinAuthorizerToken.getComponentAppId(), weiXinAuthorizerToken.getAuthorizerAppId(), weiXinAuthorizerToken.getAuthorizerRefreshToken());

        weiXinAuthorizerToken.setAuthorizerAccessToken(newWeiXinAuthorizerToken.getAuthorizerAccessToken());
        weiXinAuthorizerToken.setExpiresIn(newWeiXinAuthorizerToken.getExpiresIn());
        weiXinAuthorizerToken.setAuthorizerRefreshToken(newWeiXinAuthorizerToken.getAuthorizerRefreshToken());
        weiXinAuthorizerToken.setFetchTime(newWeiXinAuthorizerToken.getFetchTime());
        DatabaseHelper.update(weiXinAuthorizerToken);
        return weiXinAuthorizerToken;
    }

    /**
     * 修改微信授权token
     *
     * @param weiXinAuthorizerToken
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWeiXinAuthorizerToken(WeiXinAuthorizerToken weiXinAuthorizerToken) {
        DatabaseHelper.update(weiXinAuthorizerToken);
    }

    /**
     * 处理微信授权回调
     *
     * @param handleAuthCallbackModel
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest handleAuthCallback(HandleAuthCallbackModel handleAuthCallbackModel) throws IOException {
        BigInteger tenantId = handleAuthCallbackModel.getTenantId();
        String componentAppId = handleAuthCallbackModel.getComponentAppId();
        String authCode = handleAuthCallbackModel.getAuthCode();

        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, TupleUtils.buildTuple3(WeiXinOpenPlatformApplication.ColumnName.APP_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, componentAppId));
        ValidateUtils.notNull(weiXinOpenPlatformApplication, "开放平台应用不存在！");

        String componentAppSecret = weiXinOpenPlatformApplication.getAppSecret();

        ComponentAccessToken token = WeiXinUtils.obtainComponentAccessToken(componentAppId, componentAppSecret);

        String componentAccessToken = token.getComponentAccessToken();
        WeiXinAuthorizerToken weiXinAuthorizerToken = WeiXinUtils.apiQueryAuth(componentAccessToken, componentAppId, authCode);
        DatabaseHelper.insert(weiXinAuthorizerToken);

        String authorizerAppId = weiXinAuthorizerToken.getAuthorizerAppId();
        WeiXinAuthorizerInfo weiXinAuthorizerInfo = WeiXinUtils.apiGetAuthorizerInfo(componentAccessToken, componentAppId, authorizerAppId);
        weiXinAuthorizerInfo.setTenantId(tenantId);
        DatabaseHelper.insert(weiXinAuthorizerInfo);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("weiXinAuthorizerToken", weiXinAuthorizerToken);
        data.put("weiXinAuthorizerInfo", weiXinAuthorizerInfo);

        return ApiRest.builder().data(data).message("处理授权回调成功！").successful(true).build();
    }
}
