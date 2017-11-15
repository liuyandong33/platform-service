package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.ElemeCallbackMessage;
import build.dream.common.utils.CommonUtils;
import build.dream.platform.mappers.ElemeCallbackMessageMapper;
import build.dream.platform.models.elemecallbackmessage.SaveElemeCallbackMessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class ElemeCallbackMessageService {
    @Autowired
    private ElemeCallbackMessageMapper elemeCallbackMessageMapper;

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveElemeCallbackMessage(SaveElemeCallbackMessageModel saveElemeCallbackMessageModel) throws IOException {
        ElemeCallbackMessage elemeCallbackMessage = new ElemeCallbackMessage();
        elemeCallbackMessage.setRequestId(saveElemeCallbackMessageModel.getRequestId());
        elemeCallbackMessage.setType(saveElemeCallbackMessageModel.getType());
        elemeCallbackMessage.setAppId(saveElemeCallbackMessageModel.getAppId());
        elemeCallbackMessage.setMessage(saveElemeCallbackMessageModel.getMessage());
        elemeCallbackMessage.setShopId(saveElemeCallbackMessageModel.getShopId());
        elemeCallbackMessage.setTimestamp(saveElemeCallbackMessageModel.getTimestamp());
        elemeCallbackMessage.setSignature(saveElemeCallbackMessageModel.getSignature());
        elemeCallbackMessage.setUserId(saveElemeCallbackMessageModel.getUserId());
        BigInteger userId = CommonUtils.getServiceSystemUserId();
        userId = BigInteger.ZERO;
        elemeCallbackMessage.setCreateUserId(userId);
        elemeCallbackMessage.setLastUpdateUserId(userId);
        elemeCallbackMessage.setLastUpdateRemark("保存饿了么回调信息！");
        elemeCallbackMessageMapper.insert(elemeCallbackMessage);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("保存饿了么回调信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
