package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemParameter;
import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.TenantGoodsMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BranchService {
    @Autowired
    private TenantGoodsMapper tenantGoodsMapper;

    /**
     * 同步门店信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void synchronizeBranchInfo() {
        String sql = "SELECT COUNT(1) AS count FROM information_schema.TABLES WHERE table_name = #{tableName} AND table_schema = #{tableSchema};";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sql", sql);
        parameters.put("tableName", "branch");
        parameters.put("tableSchema", "saas-db");
        long count = DatabaseHelper.universalCount(parameters);
        if (count == 0) {
            String createBranchTableSql = "CREATE TABLE branch(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id', tenant_id BIGINT NOT NULL COMMENT '商户id', branch_id BIGINT NOT NULL COMMENT '门店id', `code` VARCHAR(20) NOT NULL COMMENT '门店编码', `name` VARCHAR(20) NOT NULL COMMENT '门店名称', `type` TINYINT NOT NULL COMMENT '门店类型', `status` TINYINT NOT NULL COMMENT '门店状态', tenant_code VARCHAR(20) NOT NULL COMMENT '商户编码', create_time DATETIME NOT NULL COMMENT '门店创建时间');";
            Map<String, Object> createBranchTableParameters = new HashMap<String, Object>();
            createBranchTableParameters.put("sql", createBranchTableSql);
            DatabaseHelper.executeUpdate(createBranchTableParameters);
        }

        String deploymentEnvironment = ConfigurationUtils.getConfiguration(Constants.DEPLOYMENT_ENVIRONMENT);
        SearchModel systemPartitionSearchModel = new SearchModel(true);
        systemPartitionSearchModel.addSearchCondition("deployment_environment", Constants.SQL_OPERATION_SYMBOL_EQUAL, deploymentEnvironment);
        systemPartitionSearchModel.addSearchCondition("service_name", Constants.SQL_OPERATION_SYMBOL_IN, new String[]{Constants.SERVICE_NAME_CATERING, Constants.SERVICE_NAME_RETAIL});
        List<SystemPartition> systemPartitions = DatabaseHelper.findAll(SystemPartition.class, systemPartitionSearchModel);

        SearchModel systemParameterSearchModel = new SearchModel(true);
        systemParameterSearchModel.addSearchCondition("parameter_name", Constants.SQL_OPERATION_SYMBOL_EQUAL, Constants.LAST_PULL_TIME);
        SystemParameter lastPullTimeSystemParameter = DatabaseHelper.find(SystemParameter.class, systemParameterSearchModel);

        String lastPullTime = null;
        String reacquire = null;

        Date date = new Date();
        BigInteger userId = CommonUtils.getServiceSystemUserId();
        userId = BigInteger.ZERO;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (lastPullTimeSystemParameter == null) {
            lastPullTimeSystemParameter = new SystemParameter();
            lastPullTimeSystemParameter.setParameterName(Constants.LAST_PULL_TIME);
            lastPullTimeSystemParameter.setParameterValue(simpleDateFormat.format(date));
            lastPullTimeSystemParameter.setCreateUserId(userId);
            lastPullTimeSystemParameter.setLastUpdateUserId(userId);
            lastPullTimeSystemParameter.setLastUpdateRemark("保存最后拉取时间！");
            DatabaseHelper.insert(lastPullTimeSystemParameter);
            lastPullTime = "1970-01-01 00:00:00";
            reacquire = "true";
        } else {
            lastPullTime = lastPullTimeSystemParameter.getParameterValue();
            lastPullTimeSystemParameter.setParameterValue(simpleDateFormat.format(date));
            lastPullTimeSystemParameter.setLastUpdateUserId(userId);
            lastPullTimeSystemParameter.setLastUpdateRemark("同步门店信息定时任务执行，修改最后拉取时间！");
            DatabaseHelper.update(lastPullTimeSystemParameter);
            reacquire = "false";
        }

        for (SystemPartition systemPartition : systemPartitions) {
            Map<String, String> pullBranchInfosRequestParameters = new HashMap<String, String>();
            pullBranchInfosRequestParameters.put("lastPullTime", lastPullTime);
            pullBranchInfosRequestParameters.put("reacquire", reacquire);
            ApiRest pullBranchInfosApiRest = ProxyUtils.doGetWithRequestParameters(systemPartition.getPartitionCode(), systemPartition.getServiceName(), "branch", "pullBranchInfos", pullBranchInfosRequestParameters);
            ValidateUtils.isTrue(pullBranchInfosApiRest.isSuccessful(), pullBranchInfosApiRest.getError());
            Map<String, Object> data = (Map<String, Object>) pullBranchInfosApiRest.getData();
            List<Map<String, Object>> insertBranchInfos = (List<Map<String, Object>>) data.get("insertBranchInfos");
            List<Map<String, Object>> updateBranchInfos = (List<Map<String, Object>>) data.get("updateBranchInfos");
            if (CollectionUtils.isNotEmpty(insertBranchInfos)) {
                StringBuilder insertBranchSql = new StringBuilder("INSERT INTO branch(tenant_id, branch_id, code, name, type, status, tenant_code, create_time) VALUES");
                for (Map<String, Object> branchInfo : insertBranchInfos) {
                    insertBranchSql.append("(");
                    insertBranchSql.append(branchInfo.get("tenantId"));
                    insertBranchSql.append(", ");
                    insertBranchSql.append(branchInfo.get("branchId"));
                    insertBranchSql.append(", '");
                    insertBranchSql.append(branchInfo.get("code"));
                    insertBranchSql.append("', '");
                    insertBranchSql.append(branchInfo.get("name"));
                    insertBranchSql.append("', ");
                    insertBranchSql.append(branchInfo.get("type"));
                    insertBranchSql.append(", ");
                    insertBranchSql.append(branchInfo.get("status"));
                    insertBranchSql.append(", '");
                    insertBranchSql.append(branchInfo.get("tenantCode"));
                    insertBranchSql.append("', '");
                    insertBranchSql.append(branchInfo.get("createTime"));
                    insertBranchSql.append("'), ");
                }
                insertBranchSql.deleteCharAt(insertBranchSql.length() - 1);
                insertBranchSql.deleteCharAt(insertBranchSql.length() - 1);

                Map<String, Object> insertBranchParameters = new HashMap<String, Object>();
                insertBranchParameters.put("sql", insertBranchSql.toString());
                DatabaseHelper.executeUpdate(insertBranchParameters);
            }

            if (CollectionUtils.isNotEmpty(updateBranchInfos)) {
                for (Map<String, Object> branchInfo : updateBranchInfos) {
                    int deleted = (int) branchInfo.get("deleted");
                    if (deleted == 1) {
                        StringBuilder deleteBranchSql = new StringBuilder("DELETE FROM branch WHERE tenant_id = ");
                        deleteBranchSql.append(branchInfo.get("tenantId"));
                        deleteBranchSql.append(" AND branch_id = ");
                        deleteBranchSql.append(branchInfo.get("branchId"));
                        Map<String, Object> deleteBranchParameters = new HashMap<String, Object>();
                        deleteBranchParameters.put("sql", deleteBranchSql.toString());
                        DatabaseHelper.executeUpdate(deleteBranchParameters);
                    } else {
                        StringBuilder updateBranchSql = new StringBuilder("UPDATE branch SET name = '");
                        updateBranchSql.append(branchInfo.get("name"));
                        updateBranchSql.append("', ");
                        updateBranchSql.append(" type = ");
                        updateBranchSql.append(branchInfo.get("type"));
                        updateBranchSql.append(", ");
                        updateBranchSql.append("status = ");
                        updateBranchSql.append(branchInfo.get("status"));
                        updateBranchSql.append(" WHERE tenant_id = ");
                        updateBranchSql.append(branchInfo.get("tenantId"));
                        updateBranchSql.append(" AND branch_id = ");
                        updateBranchSql.append(branchInfo.get("branchId"));
                        Map<String, Object> updateBranchParameters = new HashMap<String, Object>();
                        updateBranchParameters.put("sql", updateBranchSql.toString());
                        DatabaseHelper.executeUpdate(updateBranchParameters);
                    }
                }
            }
        }
    }

    /**
     * 禁用门店产品
     *
     * @throws ParseException
     */
    @Transactional(readOnly = true)
    public void disableGoods() throws ParseException {
        Date expireTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
        List<Map<String, Object>> expiredBranches = tenantGoodsMapper.findAllExpiredBranches(expireTime);
        for (Map<String, Object> expiredBranch : expiredBranches) {
            String disableSql = MapUtils.getString(expiredBranch, "disableSql");
            if (StringUtils.isBlank(disableSql)) {
                continue;
            }
            String business = MapUtils.getString(expiredBranch, "business");
            String partitionCode = MapUtils.getString(expiredBranch, "partitionCode");
            String tenantId = MapUtils.getString(expiredBranch, "tenantId");
            String branchId = MapUtils.getString(expiredBranch, "branchId");
            Map<String, String> disableGoodsRequestParameters = new HashMap<String, String>();
            disableGoodsRequestParameters.put("tenantId", tenantId);
            disableGoodsRequestParameters.put("branchId", branchId);
            disableGoodsRequestParameters.put("disableSql", disableSql);
            ApiRest disableGoodsApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, CommonUtils.getServiceName(business), "branch", "disableGoods", disableGoodsRequestParameters);
            ValidateUtils.isTrue(disableGoodsApiRest.isSuccessful(), disableGoodsApiRest.getError());
        }
    }
}
