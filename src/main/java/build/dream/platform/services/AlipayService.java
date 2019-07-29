package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.beans.AlipayAccount;
import build.dream.common.models.alipay.AlipayOpenAuthTokenAppModel;
import build.dream.common.saas.domains.AlipayAuthorizerInfo;
import build.dream.common.saas.domains.AlipayDeveloperAccount;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AlipayMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Transactional(rollbackFor = Exception.class)
    public ApiRest handleCallback(Map<String, String> params) {
        String tenantId = params.get("tenantId");
        String branchId = params.get("branchId");
        String appId = params.get("app_id");
        String appAuthCode = params.get("app_auth_code");

        AlipayDeveloperAccount alipayDeveloperAccount = AlipayUtils.obtainAlipayDeveloperAccount(appId);
        ValidateUtils.notNull(alipayDeveloperAccount, "支付宝开发者账号不存在！");

        AlipayOpenAuthTokenAppModel alipayOpenAuthTokenAppModel = AlipayOpenAuthTokenAppModel.builder()
                .appId(appId)
                .appPrivateKey(alipayDeveloperAccount.getAppPrivateKey())
                .alipayPublicKey(alipayDeveloperAccount.getAlipayPublicKey())
                .grantType("authorization_code")
                .code(appAuthCode)
                .build();
        Map<String, Object> result = AlipayUtils.alipayOpenAuthTokenApp(alipayOpenAuthTokenAppModel);
        Map<String, Object> alipayOpenAuthTokenAppResponse = MapUtils.getMap(result, "alipay_open_auth_token_app_response");

        String appAuthToken = MapUtils.getString(alipayOpenAuthTokenAppResponse, "app_auth_token");
        String userId = MapUtils.getString(alipayOpenAuthTokenAppResponse, "user_id");
        String authAppId = MapUtils.getString(alipayOpenAuthTokenAppResponse, "auth_app_id");
        int expiresIn = MapUtils.getIntValue(alipayOpenAuthTokenAppResponse, "expires_in");
        int reExpiresIn = MapUtils.getIntValue(alipayOpenAuthTokenAppResponse, "re_expires_in");
        String appRefreshToken = MapUtils.getString(alipayOpenAuthTokenAppResponse, "app_refresh_token");

        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .addSearchCondition(AlipayAuthorizerInfo.ColumnName.APP_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, appId)
                .addSearchCondition(AlipayAuthorizerInfo.ColumnName.AUTH_APP_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, authAppId)
                .build();
        AlipayAuthorizerInfo alipayAuthorizerInfo = DatabaseHelper.find(AlipayAuthorizerInfo.class, searchModel);
        if (Objects.isNull(alipayAuthorizerInfo)) {
            alipayAuthorizerInfo = AlipayAuthorizerInfo.builder()
                    .tenantId(BigInteger.valueOf(Long.valueOf(tenantId)))
                    .branchId(BigInteger.valueOf(Long.valueOf(branchId)))
                    .appId(appId)
                    .appAuthToken(appAuthToken)
                    .userId(userId)
                    .authAppId(authAppId)
                    .expiresIn(expiresIn)
                    .reExpiresIn(reExpiresIn)
                    .appRefreshToken(appRefreshToken)
                    .createdUserId(BigInteger.ZERO)
                    .updatedUserId(BigInteger.ZERO)
                    .build();
            DatabaseHelper.insert(alipayAuthorizerInfo);
        } else {
            alipayAuthorizerInfo.setAppAuthToken(appAuthToken);
            alipayAuthorizerInfo.setAppAuthToken(appAuthToken);
            alipayAuthorizerInfo.setUserId(userId);
            alipayAuthorizerInfo.setAuthAppId(authAppId);
            alipayAuthorizerInfo.setExpiresIn(expiresIn);
            alipayAuthorizerInfo.setReExpiresIn(reExpiresIn);
            alipayAuthorizerInfo.setAppRefreshToken(appRefreshToken);
            DatabaseHelper.update(alipayAuthorizerInfo);
        }
        return ApiRest.builder().message("处理回调成功！").successful(true).build();
    }
}
