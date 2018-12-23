package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.ElemeBranchMapping;
import build.dream.common.saas.domains.ElemeToken;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.eleme.HandleTenantAuthorizeCallbackModel;
import build.dream.platform.models.eleme.SaveElemeBranchMappingModel;
import build.dream.platform.models.eleme.VerifyTokenModel;
import net.sf.json.JSONObject;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ElemeService {
    /**
     * 处理商户授权回调
     *
     * @param handleTenantAuthorizeCallbackModel
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest handleTenantAuthorizeCallback(HandleTenantAuthorizeCallbackModel handleTenantAuthorizeCallbackModel) throws IOException {
        BigInteger tenantId = handleTenantAuthorizeCallbackModel.getTenantId();
        BigInteger branchId = handleTenantAuthorizeCallbackModel.getBranchId();
        BigInteger userId = handleTenantAuthorizeCallbackModel.getUserId();
        Integer elemeAccountType = handleTenantAuthorizeCallbackModel.getElemeAccountType();
        String code = handleTenantAuthorizeCallbackModel.getCode();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        Tenant tenant = DatabaseHelper.find(Tenant.class, searchModel);
        Validate.notNull(tenant, "商户不存在！");

        String appKey = ConfigurationUtils.getConfiguration(Constants.ELEME_APP_KEY);
        String appSecret = ConfigurationUtils.getConfiguration(Constants.ELEME_APP_SECRET);
        String redirectUrl = CommonUtils.getOutsideUrl(Constants.SERVICE_NAME_OUT, "eleme", "tenantAuthorizeCallback");
        JSONObject tokenJsonObject = JSONObject.fromObject(ElemeUtils.obtainTokenByCode(code, appKey, appSecret, redirectUrl));
        Date fetchTime = new Date();
        ElemeToken elemeToken = new ElemeToken();
        elemeToken.setTenantId(tenantId);
        String tokenField = null;
        if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_CHAIN_ACCOUNT) {
            tokenField = Constants.ELEME_TOKEN + "_" + tenantId;
        } else if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_INDEPENDENT_ACCOUNT) {
            tokenField = Constants.ELEME_TOKEN + "_" + tenantId + "_" + branchId;
            elemeToken.setBranchId(branchId);
        }
        elemeToken.setAccessToken(tokenJsonObject.getString("access_token"));
        elemeToken.setRefreshToken(tokenJsonObject.getString("refresh_token"));
        elemeToken.setExpiresIn(tokenJsonObject.getInt("expires_in"));
        elemeToken.setTokenType(tokenJsonObject.getString("token_type"));
        elemeToken.setFetchTime(fetchTime);

        elemeToken.setCreatedUserId(userId);
        elemeToken.setUpdatedUserId(userId);
        elemeToken.setUpdatedRemark("商户授权获取token");
        DatabaseHelper.insert(elemeToken);

        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName(ElemeToken.TABLE_NAME);
        updateModel.addContentValue(ElemeToken.ColumnName.DELETED, 1);
        updateModel.addContentValue(ElemeToken.ColumnName.UPDATED_USER_ID, userId);
        updateModel.addContentValue(ElemeToken.ColumnName.UPDATED_REMARK, "商户重新授权，删除本条记录！");
        updateModel.addSearchCondition(ElemeToken.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_CHAIN_ACCOUNT) {

        } else if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_INDEPENDENT_ACCOUNT) {
            updateModel.addSearchCondition(ElemeToken.ColumnName.BRANCH_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
        }
        DatabaseHelper.universalUpdate(updateModel);

        tokenJsonObject.put("fetch_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fetchTime));
        CacheUtils.hset(Constants.KEY_ELEME_TOKENS, tokenField, GsonUtils.toJson(elemeToken));

        return ApiRest.builder().message("处理商户授权成功！").successful(true).build();
    }

    /**
     * 保存门店映射
     *
     * @param saveElemeBranchMappingModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveElemeBranchMapping(SaveElemeBranchMappingModel saveElemeBranchMappingModel) {
        BigInteger tenantId = saveElemeBranchMappingModel.getTenantId();
        BigInteger branchId = saveElemeBranchMappingModel.getBranchId();
        BigInteger userId = saveElemeBranchMappingModel.getUserId();
        BigInteger shopId = saveElemeBranchMappingModel.getShopId();
        String updatedRemark = "门店(" + branchId + ")绑定饿了么(" + shopId + ")，清除绑定关系！";

        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName(ElemeBranchMapping.TABLE_NAME);
        updateModel.addContentValue(ElemeBranchMapping.ColumnName.DELETED, 1);
        updateModel.addContentValue(ElemeBranchMapping.ColumnName.UPDATED_USER_ID, userId);
        updateModel.addContentValue(ElemeBranchMapping.ColumnName.UPDATED_REMARK, updatedRemark);
        updateModel.addSearchCondition(ElemeBranchMapping.ColumnName.SHOP_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, shopId);
        DatabaseHelper.universalUpdate(updateModel);

        ElemeBranchMapping elemeBranchMapping = new ElemeBranchMapping();
        elemeBranchMapping.setTenantId(tenantId);
        elemeBranchMapping.setBranchId(branchId);
        elemeBranchMapping.setShopId(shopId);

        elemeBranchMapping.setCreatedUserId(userId);
        elemeBranchMapping.setUpdatedUserId(userId);
        elemeBranchMapping.setUpdatedRemark("保存饿了么门店映射！");
        DatabaseHelper.insert(elemeBranchMapping);

        return ApiRest.builder().message("保存饿了么门店映射成功！").successful(true).build();
    }

    /**
     * 验证token是否有效
     *
     * @param verifyTokenModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest verifyToken(VerifyTokenModel verifyTokenModel) {
        BigInteger tenantId = verifyTokenModel.getTenantId();
        BigInteger branchId = verifyTokenModel.getBranchId();
        BigInteger userId = verifyTokenModel.getUserId();
        int elemeAccountType = verifyTokenModel.getElemeAccountType();

        boolean isEffective = ElemeUtils.verifyToken(tenantId.toString(), branchId.toString(), elemeAccountType);
        if (isEffective) {

        } else {
            SearchModel searchModel = new SearchModel(true);
            searchModel.addSearchCondition(ElemeToken.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);

            if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_CHAIN_ACCOUNT) {

            } else if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_INDEPENDENT_ACCOUNT) {
                searchModel.addSearchCondition(ElemeToken.ColumnName.BRANCH_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
            }

            ElemeToken elemeToken = DatabaseHelper.find(ElemeToken.class, searchModel);
            elemeToken.setDeleted(true);
            elemeToken.setDeletedTime(new Date());
            elemeToken.setUpdatedUserId(userId);
            DatabaseHelper.update(elemeToken);

            if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_CHAIN_ACCOUNT) {
                CacheUtils.hdel(Constants.KEY_ELEME_TOKENS, Constants.ELEME_TOKEN + "_" + tenantId);
            } else if (elemeAccountType == Constants.ELEME_ACCOUNT_TYPE_INDEPENDENT_ACCOUNT) {
                CacheUtils.hdel(build.dream.common.constants.Constants.KEY_ELEME_TOKENS, Constants.ELEME_TOKEN + "_" + tenantId + "_" + branchId);
            }
        }

        return ApiRest.builder().data(isEffective).message("验证token成功！").successful(true).build();
    }
}
