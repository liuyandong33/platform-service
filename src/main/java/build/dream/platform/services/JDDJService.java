package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.platform.mappers.JDDJMapper;
import build.dream.platform.models.jddj.ListJDDJCodesModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JDDJService {
    @Autowired
    private JDDJMapper jddjMapper;

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
}
