package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemPrivilege;
import build.dream.common.utils.SearchModel;
import build.dream.platform.beans.ZTreeNode;
import build.dream.platform.mappers.SystemPrivilegeMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemPrivilegeService {
    @Autowired
    private SystemPrivilegeMapper systemPrivilegeMapper;

    @Transactional(readOnly = true)
    public ApiRest findAll() {
        SearchModel searchModel = new SearchModel(true);
        List<SystemPrivilege> systemPrivileges = systemPrivilegeMapper.findAll(searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (SystemPrivilege systemPrivilege : systemPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode();
            zTreeNode.setId(systemPrivilege.getId().toString());
            zTreeNode.setpId(systemPrivilege.getParentId().toString());
            if (StringUtils.isNotBlank(systemPrivilege.getResourceName()) && StringUtils.isNotBlank(systemPrivilege.getOperateName())) {
                zTreeNode.setName(systemPrivilege.getOperateName());
            } else {
                zTreeNode.setName(systemPrivilege.getResourceName());
            }
            zTreeNodes.add(zTreeNode);
        }
        ApiRest apiRest = new ApiRest();
        apiRest.setData(zTreeNodes);
        apiRest.setMessage("获取权限列表成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
