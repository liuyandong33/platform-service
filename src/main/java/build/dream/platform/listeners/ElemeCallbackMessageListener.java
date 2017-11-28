package build.dream.platform.listeners;

import build.dream.common.saas.domains.ElemeCallbackMessage;
import build.dream.common.utils.CacheUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.ElemeCallbackMessageService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ElemeCallbackMessageListener implements MessageListener {
    private static final String ELEME_CALLBACK_MESSAGE_LISTENER_SIMPLE_NAME = "ElemeCallbackMessageListener";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ElemeCallbackMessageService elemeCallbackMessageService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<String> stringRedisSerializer = stringRedisTemplate.getStringSerializer();
        String body = stringRedisSerializer.deserialize(message.getBody());
        JSONObject messageJsonObject = JSONObject.fromObject(body);
        String uuid = messageJsonObject.getString("uuid");
        boolean setnxSuccessful = CacheUtils.setnx(uuid, uuid);
        if (setnxSuccessful) {
            CacheUtils.expire(uuid, 1800, TimeUnit.SECONDS);
            JSONObject callbackRequestBodyJsonObject = JSONObject.fromObject(messageJsonObject.getString("callbackRequestBody"));
            ElemeCallbackMessage elemeCallbackMessage = new ElemeCallbackMessage();
            elemeCallbackMessage.setRequestId(callbackRequestBodyJsonObject.getString("requestId"));
            elemeCallbackMessage.setType(callbackRequestBodyJsonObject.getInt("type"));
            elemeCallbackMessage.setAppId(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("appId")));
            elemeCallbackMessage.setMessage(callbackRequestBodyJsonObject.getString("message"));
            elemeCallbackMessage.setShopId(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("shopId")));
            elemeCallbackMessage.setTimestamp(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("timestamp")));
            elemeCallbackMessage.setSignature(callbackRequestBodyJsonObject.getString(""));
            elemeCallbackMessage.setUserId(NumberUtils.createBigInteger(callbackRequestBodyJsonObject.getString("userId")));
            elemeCallbackMessage.setMessageMd5(uuid);
            elemeCallbackMessage.setHandleResult(Constants.ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_SUCCESS);
            elemeCallbackMessageService.saveElemeCallbackMessage(elemeCallbackMessage);
        }
    }
}
