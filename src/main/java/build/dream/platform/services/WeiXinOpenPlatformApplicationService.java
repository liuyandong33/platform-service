package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.WeiXinOpenPlatformApplicationMapper;
import build.dream.platform.models.weixin.DeleteWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.FindWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.SaveWeiXinOpenPlatformApplicationModel;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeiXinOpenPlatformApplicationService {
    @Autowired
    private WeiXinOpenPlatformApplicationMapper weiXinOpenPlatformApplicationMapper;

    @Transactional
    public ApiRest saveWeiXinOpenPlatformApplication(SaveWeiXinOpenPlatformApplicationModel saveWeiXinOpenPlatformApplicationModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, saveWeiXinOpenPlatformApplicationModel.getAppId());
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = weiXinOpenPlatformApplicationMapper.find(searchModel);
        if (weiXinOpenPlatformApplication == null) {
            weiXinOpenPlatformApplication = new WeiXinOpenPlatformApplication();
            weiXinOpenPlatformApplication.setAppId(saveWeiXinOpenPlatformApplicationModel.getAppId());
            weiXinOpenPlatformApplication.setAppSecret(saveWeiXinOpenPlatformApplicationModel.getAppSecret());
            weiXinOpenPlatformApplication.setCreateUserId(saveWeiXinOpenPlatformApplicationModel.getUserId());
            weiXinOpenPlatformApplication.setLastUpdateUserId(saveWeiXinOpenPlatformApplicationModel.getUserId());
            weiXinOpenPlatformApplication.setLastUpdateRemark("保存微信开放平台应用！");
            weiXinOpenPlatformApplicationMapper.insert(weiXinOpenPlatformApplication);
        } else {
            weiXinOpenPlatformApplication.setAppSecret(saveWeiXinOpenPlatformApplicationModel.getAppSecret());
            weiXinOpenPlatformApplication.setLastUpdateUserId(saveWeiXinOpenPlatformApplicationModel.getUserId());
            weiXinOpenPlatformApplication.setLastUpdateRemark("修改微信开放平台应用！");
            weiXinOpenPlatformApplicationMapper.update(weiXinOpenPlatformApplication);
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
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = weiXinOpenPlatformApplicationMapper.find(searchModel);
        Validate.notNull(weiXinOpenPlatformApplication, "微信开放平台应用不存在！");
        weiXinOpenPlatformApplication.setDeleted(true);
        weiXinOpenPlatformApplicationMapper.update(weiXinOpenPlatformApplication);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinOpenPlatformApplication);
        apiRest.setClassName(WeiXinOpenPlatformApplication.class.getName());
        apiRest.setMessage("删除微信开放平台应用成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest find(FindWeiXinOpenPlatformApplicationModel findWeiXinOpenPlatformApplicationModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("app_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, findWeiXinOpenPlatformApplicationModel.getAppId());
        WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = weiXinOpenPlatformApplicationMapper.find(searchModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(weiXinOpenPlatformApplication);
        apiRest.setClassName(WeiXinOpenPlatformApplication.class.getName());
        apiRest.setMessage("查询微信开放平台应用成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
