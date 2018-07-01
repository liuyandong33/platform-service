package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.saas.domains.WeiXinPublicAccount;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.weixin.DeleteWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.ObtainWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.ObtainWeiXinPublicAccountModel;
import build.dream.platform.models.weixin.SaveWeiXinPublicAccountModel;
import build.dream.platform.utils.DatabaseHelper;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class WeiXinService {
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
}
