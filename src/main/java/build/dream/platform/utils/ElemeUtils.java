package build.dream.platform.utils;

import build.dream.common.utils.QueueUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.tools.ElemeConsumerThread;

import java.util.concurrent.TimeUnit;

/**
 * Created by liuyandong on 2017/3/13.
 */
public class ElemeUtils {
    public static void addElemeMessage(String elemeMessage) {
        QueueUtils.rpush(Constants.KEY_ELEME_CALLBACK_MESSAGE, elemeMessage);
    }

    public static String takeElemeMessage() {
        String elemeMessage = QueueUtils.blpop(Constants.KEY_ELEME_CALLBACK_MESSAGE, 1, TimeUnit.HOURS);
        return elemeMessage;
    }

    public static void startElemeConsumerThread() {
        new Thread(new ElemeConsumerThread()).start();
    }
}
