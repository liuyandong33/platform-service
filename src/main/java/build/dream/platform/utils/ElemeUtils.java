package build.dream.platform.utils;

import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.QueueUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.tools.ElemeConsumerThread;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuyandong on 2017/3/13.
 */
public class ElemeUtils {
    public static void addElemeMessage(JSONObject callbackRequestBodyJsonObject, String uuid, int count) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("callbackRequestBody", callbackRequestBodyJsonObject);
        map.put("uuid", uuid);
        map.put("count", count);
        QueueUtils.rpush(Constants.KEY_ELEME_CALLBACK_MESSAGE, GsonUtils.toJson(map));
    }

    public static String takeElemeMessage() {
        String elemeMessage = QueueUtils.blpop(Constants.KEY_ELEME_CALLBACK_MESSAGE, 1, TimeUnit.HOURS);
        return elemeMessage;
    }

    public static void startElemeConsumerThread() {
        new Thread(new ElemeConsumerThread()).start();
    }
}
