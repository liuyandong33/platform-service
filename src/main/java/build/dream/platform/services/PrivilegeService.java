package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.utils.SearchModel;
import build.dream.platform.beans.ZTreeNode;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.BackgroundPrivilegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {
    @Autowired
    private BackgroundPrivilegeMapper backgroundPrivilegeMapper;

    @Transactional(readOnly = true)
    public ApiRest listBackgroundPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("service_name", Constants.SQL_OPERATION_SYMBOL_EQUALS, Constants.SERVICE_NAME_CATERING);
        searchModel.addSearchCondition("hidden", Constants.SQL_OPERATION_SYMBOL_EQUALS, 0);
        List<BackgroundPrivilege> backgroundPrivileges = backgroundPrivilegeMapper.findAll(searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode();
            zTreeNode.setId(backgroundPrivilege.getId().toString());
            zTreeNode.setpId(backgroundPrivilege.getParentId().toString());
            zTreeNode.setName(backgroundPrivilege.getPrivilegeName());
            zTreeNodes.add(zTreeNode);
        }
        ApiRest apiRest = new ApiRest();
        apiRest.setData(zTreeNodes);
        apiRest.setMessage("获取权限列表成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
