package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.ElemeCallbackMessage;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.utils.DatabaseHelper;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ElemeCallbackMessageService {
    @Transactional(rollbackFor = Exception.class)
    public void saveElemeCallbackMessage(ElemeCallbackMessage elemeCallbackMessage) {
        DatabaseHelper.insert(elemeCallbackMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest markHandleFailureMessage(String uuid) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("uuid", Constants.SQL_OPERATION_SYMBOL_EQUAL, uuid);
        ElemeCallbackMessage elemeCallbackMessage = DatabaseHelper.find(ElemeCallbackMessage.class, searchModel);
        Validate.notNull(elemeCallbackMessage, "消息不存在！");
        elemeCallbackMessage.setHandleResult(Constants.ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_FAILURE);
        DatabaseHelper.update(elemeCallbackMessage);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("标记处理失败信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
