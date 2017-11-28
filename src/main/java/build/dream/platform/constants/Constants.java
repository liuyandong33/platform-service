package build.dream.platform.constants;

public class Constants extends build.dream.common.constants.Constants {
    public static final String CATERING_CURRENT_PARTITION_CODE = "catering.current.partition.code";
    public static final String RETAIL_CURRENT_PARTITION_CODE = "retail.current.partition.code";
    public static final Integer ORDER_TYPE_TENANT_ORDER = 1;
    public static final Integer ORDER_TYPE_AGENT_ORDER = 2;
    public static final Integer ORDER_STATUS_UNPAID = 1;
    public static final Integer ORDER_STATUS_PAID = 2;

    public static final String WEI_XIN_PAY_CALLBACK_SUCCESS_RETURN_VALUE = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    public static final String WEI_XIN_PAY_CALLBACK_FAILURE_RETURN_VALUE = "<xml><return_code><![CDATA[FAILURE]]></return_code></xml>";

    public static final String ALI_PAY_CALLBACK_SUCCESS_RETURN_VALUE = "SUCCESS";
    public static final String ALI_PAY_CALLBACK_FAILURE_RETURN_VALUE = "FAILURE";

    public static final String TRADE_FINISHED = "TRADE_FINISHED";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final Integer ORDER_PAID_TYPE_WEI_XIN = 1;
    public static final Integer ORDER_PAID_TYPE_ALI_PAY = 2;

    public static final String SMS_PLATFORM_APP_ID = "sms.platform.app.id";
    public static final String SMS_PLATFORM_APP_SECRET = "sms.platform.app.secret";
    public static final String SMS_PLATFORM_SERVICE_URL = "sms.platform.service.url";

    public static final Integer ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_SUCCESS = 1;
    public static final Integer ELEME_CALLBACK_MESSAGE_HANDLE_RESULT_FAILURE = 2;
}
