package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.saas.domains.WeiXinPublicAccount;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.weixin.*;
import build.dream.platform.utils.DatabaseHelper;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeiXinService {
    @Transactional
    public ApiRest saveWeiXinOpenPlatformApplication(SaveWeiXinOpenPlatformApplicationModel saveWeiXinOpenPlatformApplicationModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, saveWeiXinOpenPlatformApplicationModel.getAppId());
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, searchModel);
        if (weiXinOpenPlatformApplication == null) {
            weiXinOpenPlatformApplication = new WeiXinOpenPlatformApplication();
            weiXinOpenPlatformApplication.setAppId(saveWeiXinOpenPlatformApplicationModel.getAppId());
            weiXinOpenPlatformApplication.setAppSecret(saveWeiXinOpenPlatformApplicationModel.getAppSecret());
            weiXinOpenPlatformApplication.setCreateUserId(saveWeiXinOpenPlatformApplicationModel.getUserId());
            weiXinOpenPlatformApplication.setLastUpdateUserId(saveWeiXinOpenPlatformApplicationModel.getUserId());
            weiXinOpenPlatformApplication.setLastUpdateRemark("保存微信开放平台应用！");
            DatabaseHelper.insert(weiXinOpenPlatformApplication);
        } else {
            weiXinOpenPlatformApplication.setAppSecret(saveWeiXinOpenPlatformApplicationModel.getAppSecret());
            weiXinOpenPlatformApplication.setLastUpdateUserId(saveWeiXinOpenPlatformApplicationModel.getUserId());
            weiXinOpenPlatformApplication.setLastUpdateRemark("修改微信开放平台应用！");
            DatabaseHelper.update(weiXinOpenPlatformApplication);
        }
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinOpenPlatformApplication);
        apiRest.setClassName(WeiXinOpenPlatformApplication.class.getName());
        apiRest.setMessage("保存微信开放平台应用成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional
    public ApiRest deleteWeiXinOpenPlatformApplication(DeleteWeiXinOpenPlatformApplicationModel deleteWeiXinOpenPlatformApplicationModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, deleteWeiXinOpenPlatformApplicationModel.getAppId());
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
    public ApiRest findWeiXinOpenPlatformApplication(FindWeiXinOpenPlatformApplicationModel findWeiXinOpenPlatformApplicationModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, findWeiXinOpenPlatformApplicationModel.getAppId());
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = DatabaseHelper.find(WeiXinOpenPlatformApplication.class, searchModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinOpenPlatformApplication);
        apiRest.setClassName(WeiXinOpenPlatformApplication.class.getName());
        apiRest.setMessage("查询微信开放平台应用成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional
    public ApiRest saveWeiXinPublicAccount(SaveWeiXinPublicAccountModel saveWeiXinPublicAccountModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, saveWeiXinPublicAccountModel.getTenantId());
        WeiXinPublicAccount weiXinPublicAccount = DatabaseHelper.find(WeiXinPublicAccount.class, searchModel);
        if (weiXinPublicAccount == null) {
            weiXinPublicAccount = new WeiXinPublicAccount();
            weiXinPublicAccount.setTenantId(saveWeiXinPublicAccountModel.getTenantId());
            weiXinPublicAccount.setName(saveWeiXinPublicAccountModel.getName());
            weiXinPublicAccount.setAppId(saveWeiXinPublicAccountModel.getAppId());
            weiXinPublicAccount.setAppSecret(saveWeiXinPublicAccountModel.getAppSecret());
            weiXinPublicAccount.setOriginalId(saveWeiXinPublicAccountModel.getOriginalId());
            weiXinPublicAccount.setCreateUserId(saveWeiXinPublicAccountModel.getUserId());
            weiXinPublicAccount.setLastUpdateUserId(saveWeiXinPublicAccountModel.getUserId());
            weiXinPublicAccount.setLastUpdateRemark("新增微信公众号！");
            DatabaseHelper.insert(weiXinPublicAccount);
        } else {
            weiXinPublicAccount.setName(saveWeiXinPublicAccountModel.getName());
            weiXinPublicAccount.setAppId(saveWeiXinPublicAccountModel.getAppId());
            weiXinPublicAccount.setAppSecret(saveWeiXinPublicAccountModel.getAppSecret());
            weiXinPublicAccount.setOriginalId(saveWeiXinPublicAccountModel.getOriginalId());
            weiXinPublicAccount.setLastUpdateUserId(saveWeiXinPublicAccountModel.getUserId());
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
        searchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, obtainWeiXinPublicAccountModel.getTenantId());
        WeiXinPublicAccount weiXinPublicAccount = DatabaseHelper.find(WeiXinPublicAccount.class, searchModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinPublicAccount);
        apiRest.setClassName(WeiXinPublicAccount.class.getName());
        apiRest.setMessage("查询微信公众号成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
