package build.dream.platform.constants;

public class Constants extends build.dream.common.constants.Constants {
    public static final String CATERING_CURRENT_PARTITION_CODE = "catering.current.partition.code";
    public static final String RETAIL_CURRENT_PARTITION_CODE = "retail.current.partition.code";
    public static final Integer ORDER_TYPE_TENANT_ORDER = 1;
    public static final Integer ORDER_TYPE_AGENT_ORDER = 2;
    public static final Integer ORDER_STATUS_UNPAID = 1;
    public static final Integer ORDER_STATUS_PAID = 2;

    public static final String SMS_PLATFORM_APP_ID = "sms.platform.app.id";
    public static final String SMS_PLATFORM_APP_SECRET = "sms.platform.app.secret";
    public static final String SMS_PLATFORM_SERVICE_URL = "sms.platform.service.url";

    public static final Integer ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_SUCCESS = 1;
    public static final Integer ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_FAILURE = 2;

    public static final String PLATFORM_PUBLIC_KEY = "platform.public.key";
    public static final String PLATFORM_PRIVATE_KEY = "platform.private.key";

    public static final String PRIVILEGE_TYPE_BACKGROUND = "background";
    public static final String PRIVILEGE_TYPE_APP = "app";
    public static final String PRIVILEGE_TYPE_POS = "pos";

    public static final String ROLE_TYPE_BACKGROUND = "background";
    public static final String ROLE_TYPE_APP = "app";
    public static final String ROLE_TYPE_POS = "pos";

    public static final String GOODS_INFOS_SCHEMA_FILE_PATH = "build/dream/platform/schemas/goodsInfosSchema.json";
    public static final String SPECIAL_GOODS_ACTIVITY_INFOS_SCHEMA_FILE_PATH = "build/dream/platform/schemas/specialGoodsActivityInfosSchema.json";
    public static final String ELEME_MESSAGE_SCHEMA_FILE_PATH = "build/dream/platform/schemas/elemeMessageSchema.json";

    public static final Integer ACTIVITY_TYPE_SPECIAL_GOODS = 1;
    public static final Integer ACTIVITY_STATUS_UNEXECUTED = 1;
    public static final Integer ACTIVITY_STATUS_EXECUTING = 2;
    public static final Integer ACTIVITY_STATUS_TERMINATED = 3;
    public static final Integer ACTIVITY_STATUS_EXPIRED = 3;
}
