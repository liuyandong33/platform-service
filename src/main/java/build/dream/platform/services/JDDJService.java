package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.mappers.JDDJMapper;
import build.dream.platform.models.jddj.ListJDDJCodesModel;
import build.dream.platform.models.jddj.SaveJDDJInfoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class JDDJService {
    @Autowired
    private JDDJMapper jddjMapper;

    /**
     * 分页查询京东到家Code
     *
     * @param listJDDJCodesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listJDDJCodes(ListJDDJCodesModel listJDDJCodesModel) {
        String searchString = listJDDJCodesModel.getSearchString();
        int page = listJDDJCodesModel.getPage();
        int rows = listJDDJCodesModel.getRows();

        if (StringUtils.isNotBlank(searchString)) {
            searchString = "%" + searchString + "%";
        }

        long count = jddjMapper.countJDDJCodes(searchString);
        List<Map<String, Object>> results = null;
        if (count > 0) {
            results = jddjMapper.listJDDJCodes(searchString, (page - 1) * rows, rows);
        } else {
            results = new ArrayList<Map<String, Object>>();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", results);

        return ApiRest.builder().data(data).message("查询京东到家Code成功！").successful(true).build();
    }

    /**
     * 保存京东到家商家ID
     *
     * @param saveJDDJInfoModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveJDDJInfo(SaveJDDJInfoModel saveJDDJInfoModel) {
        BigInteger tenantId = saveJDDJInfoModel.getTenantId();
        String appKey = saveJDDJInfoModel.getAppKey();
        String appSecret = saveJDDJInfoModel.getAppSecret();
        String venderId = saveJDDJInfoModel.getVenderId();

        SearchModel checkSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(Tenant.ColumnName.JDDJ_VENDER_ID, venderId)
                .build();

        Tenant check = DatabaseHelper.find(Tenant.class, checkSearchModel);
        ValidateUtils.isTrue(Objects.isNull(check), "该商家ID已经被商户【" + check.getName() + "】绑定！");

        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(Tenant.ColumnName.ID, tenantId)
                .build();
        Tenant tenant = DatabaseHelper.find(Tenant.class, searchModel);
        ValidateUtils.notNull(tenant, "商户不存在！");

        tenant.setJddjVenderId(venderId);
        tenant.setJddjAppKey(appKey);
        tenant.setJddjAppSecret(appSecret);
        tenant.setUpdatedRemark("保存京东到家信息！");
        DatabaseHelper.update(tenant);
        return ApiRest.builder().message("保存京东到家信息成功！").successful(true).build();
    }
}
