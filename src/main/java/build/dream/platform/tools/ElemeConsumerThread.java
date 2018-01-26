package build.dream.platform.tools;

import build.dream.common.saas.domains.ElemeCallbackMessage;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.CommonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.ElemeCallbackMessageService;
import build.dream.platform.utils.ElemeUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigInteger;
import java.util.Map;

public class ElemeConsumerThread implements Runnable {
    private static final String ELEME_CONSUMER_THREAD_SIMPLE_NAME = "ElemeConsumerThread";
    private ElemeCallbackMessageService elemeCallbackMessageService = ApplicationHandler.getBean(ElemeCallbackMessageService.class);

    @Override
    public void run() {
        while (true) {
            String elemeMessage = null;
            String uuid = null;
            try {
                Map<String, String> elemeMessageBody = ElemeUtils.takeElemeMessage();
                if (MapUtils.isEmpty(elemeMessageBody)) {
                    continue;
                }

                elemeMessage = elemeMessageBody.get("callbackRequestBody");
                uuid = elemeMessageBody.get("uuid");

                JSONObject callbackRequestBodyJsonObject = JSONObject.fromObject(elemeMessage);
                ElemeCallbackMessage elemeCallbackMessage = new ElemeCallbackMessage();
                elemeCallbackMessage.setRequestId(callbackRequestBodyJsonObject.getString("requestId"));
                elemeCallbackMessage.setType(callbackRequestBodyJsonObject.getInt("type"));
                elemeCallbackMessage.setAppId(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("appId")));
                elemeCallbackMessage.setMessage(callbackRequestBodyJsonObject.getString("message"));
                elemeCallbackMessage.setShopId(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("shopId")));
                elemeCallbackMessage.setTimestamp(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("timestamp")));
                elemeCallbackMessage.setSignature(callbackRequestBodyJsonObject.getString("signature"));
                elemeCallbackMessage.setUserId(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("userId")));
                elemeCallbackMessage.setMessageMd5(uuid);
                elemeCallbackMessage.setHandleResult(Constants.ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_SUCCESS);

                BigInteger userId = CommonUtils.getServiceSystemUserId();
                userId = BigInteger.ZERO;
                elemeCallbackMessage.setCreateUserId(userId);
                elemeCallbackMessage.setLastUpdateUserId(userId);
                elemeCallbackMessage.setLastUpdateRemark("保存饿了么回调信息！");
                elemeCallbackMessageService.saveElemeCallbackMessage(elemeCallbackMessage);
            } catch (Exception e) {
                if (StringUtils.isNotBlank(elemeMessage)) {
                    ElemeUtils.addElemeMessage(elemeMessage, uuid);
                }
                LogUtils.error("保存饿了么消息失败", ELEME_CONSUMER_THREAD_SIMPLE_NAME, "run", e);
            }
        }
    }
}
