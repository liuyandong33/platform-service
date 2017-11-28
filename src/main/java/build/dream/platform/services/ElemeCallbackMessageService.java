package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.ElemeCallbackMessage;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.ElemeCallbackMessageMapper;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ElemeCallbackMessageService {
    @Autowired
    private ElemeCallbackMessageMapper elemeCallbackMessageMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveElemeCallbackMessage(ElemeCallbackMessage elemeCallbackMessage) {
        elemeCallbackMessageMapper.insert(elemeCallbackMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest markHandleFailureMessage(String messageMd5) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("message_md5", Constants.SQL_OPERATION_SYMBOL_EQUALS, messageMd5);
        ElemeCallbackMessage elemeCallbackMessage = elemeCallbackMessageMapper.find(searchModel);
        Validate.notNull(elemeCallbackMessage, "消息不存在！");
        elemeCallbackMessage.setHandleResult(Constants.ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_FAILURE);
        elemeCallbackMessageMapper.update(elemeCallbackMessage);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("标记处理失败信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
