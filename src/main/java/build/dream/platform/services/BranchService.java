package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.ProxyUtils;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.SystemPartitionMapper;
import build.dream.platform.mappers.UniversalMapper;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BranchService {
    @Autowired
    private UniversalMapper universalMapper;
    @Autowired
    private SystemPartitionMapper systemPartitionMapper;

    @Transactional(rollbackFor = Exception.class)
    public void synchronizeBranchInfo() throws IOException {
        String sql = "SELECT COUNT(1) FROM information_schema.TABLES WHERE table_name = #{tableName} AND table_schema = #{tableSchema};";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sql", sql);
        parameters.put("tableName", "branch");
        parameters.put("tableSchema", "saas-db");
        long count = (long) universalMapper.executeUniqueResultQuery(parameters).get("count");
        if (count == 0) {
            String createBranchTableSql = "CREATE TABLE branch(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id', tenant_id BIGINT NOT NULL COMMENT '商户id', branch_id BIGINT NOT NULL COMMENT '门店id', `code` VARCHAR(20) NOT NULL COMMENT '门店编码', `name` VARCHAR(20) NOT NULL COMMENT '门店名称', `type` TINYINT NOT NULL COMMENT '门店类型', `status` TINYINT NOT NULL COMMENT '门店状态', tenant_code VARCHAR(20) NOT NULL COMMENT '商户编码', create_time DATETIME NOT NULL COMMENT '门店创建时间');";
        }

        String deploymentEnvironment = ConfigurationUtils.getConfiguration(Constants.DEPLOYMENT_ENVIRONMENT);
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("deployment_environment", Constants.SQL_OPERATION_SYMBOL_EQUALS, deploymentEnvironment);
        searchModel.addSearchCondition("service_name", Constants.SQL_OPERATION_SYMBOL_IN, new String[]{Constants.SERVICE_NAME_CATERING, Constants.SERVICE_NAME_RETAIL});
        List<SystemPartition> systemPartitions = systemPartitionMapper.findAll(searchModel);
        for (SystemPartition systemPartition : systemPartitions) {
            Map<String, String> pullBranchInfosRequestParameters = new HashMap<String, String>();
            pullBranchInfosRequestParameters.put("lastPullTime", "");
            pullBranchInfosRequestParameters.put("reacquire", "");
            ApiRest pullBranchInfosApiRest = ProxyUtils.doGetWithRequestParameters(systemPartition.getPartitionCode(), systemPartition.getServiceName(), "branch", "pullBranchInfos", pullBranchInfosRequestParameters);
            Validate.isTrue(pullBranchInfosApiRest.isSuccessful(), pullBranchInfosApiRest.getError());
        }
    }
}
