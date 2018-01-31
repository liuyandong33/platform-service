package build.dream.platform.utils;

import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.JacksonUtils;
import build.dream.common.utils.QueueUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.tools.ElemeConsumerThread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuyandong on 2017/3/13.
 */
public class ElemeUtils {
    public static void addElemeMessage(String elemeMessage, String uuid) {
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put("callbackRequestBody", elemeMessage);
        messageMap.put("uuid", uuid);
        QueueUtils.rpush(Constants.KEY_ELEME_CALLBACK_MESSAGE, GsonUtils.toJson(messageMap));
    }

    public static Map<String, String> takeElemeMessage() {
        String message = QueueUtils.blpop(Constants.KEY_ELEME_CALLBACK_MESSAGE, 1, TimeUnit.HOURS);
        Map<String, String> messageMap = JacksonUtils.readValue(message, Map.class);
        return messageMap;
    }

    public static void startElemeConsumerThread() {
        new Thread(new ElemeConsumerThread()).start();
    }
}
