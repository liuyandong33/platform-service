package build.dream.platform.constants;

public class Constants extends build.dream.common.constants.Constants {
    public static final Integer ORDER_TYPE_TENANT_ORDER = 1;
    public static final Integer ORDER_TYPE_AGENT_ORDER = 2;
    public static final Integer ORDER_STATUS_UNPAID = 1;
    public static final Integer ORDER_STATUS_PAID = 2;

    public static final String GOODS_INFOS_SCHEMA_FILE_PATH = "build/dream/platform/schemas/goodsInfosSchema.json";
    public static final String SPECIAL_GOODS_ACTIVITY_INFOS_SCHEMA_FILE_PATH = "build/dream/platform/schemas/specialGoodsActivityInfosSchema.json";
    public static final String ELEME_MESSAGE_SCHEMA_FILE_PATH = "build/dream/platform/schemas/elemeMessageSchema.json";
    public static final String CONTRACT_PRICE_INFOS_SCHEMA_FILE_PATH = "build/dream/platform/schemas/contractPriceInfosSchema.json";

    public static final Integer ACTIVITY_TYPE_SPECIAL_GOODS = 1;
    public static final Integer ACTIVITY_STATUS_UNEXECUTED = 1;
    public static final Integer ACTIVITY_STATUS_EXECUTING = 2;
    public static final Integer ACTIVITY_STATUS_TERMINATED = 3;
    public static final Integer ACTIVITY_STATUS_EXPIRED = 3;

    public static final Integer ACTIVATION_CODE_STATUS_NOT_USED = 1;
    public static final Integer ACTIVATION_CODE_STATUS_NOT_ALREADY_USED = 2;
    public static final Integer ACTIVATION_CODE_STATUS_NOT_EXPIRED = 3;

    public static final Integer SALE_FLOW_TYPE_TENANT_FLOW = 1;
    public static final Integer SALE_FLOW_TYPE_AGENT_FLOW = 2;

    public static final String BASIC_SERVICES_GOODS_FREE_TRIAL_DAYS = "basic.services.goods.free.trial.days";

    public static final Integer AGENT_CONTRACT_STATUS_UNAUDITED = 1;
    public static final Integer AGENT_CONTRACT_STATUS_UNEXECUTED = 2;
    public static final Integer AGENT_CONTRACT_STATUS_EXECUTING = 3;
    public static final Integer AGENT_CONTRACT_STATUS_TERMINATED = 4;
    public static final Integer AGENT_CONTRACT_STATUS_EXPIRED = 5;
}
