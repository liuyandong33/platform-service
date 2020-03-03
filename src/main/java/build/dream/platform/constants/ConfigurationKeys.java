package build.dream.platform.constants;

public class ConfigurationKeys extends build.dream.common.constants.ConfigurationKeys {
    /**
     * 禁用门店产品定时任务cron表达式
     */
    public static final String DISABLE_BRANCH_GOODS_JOB_CRON_EXPRESSION = "disable.branch.goods.job.cron.expression";

    /**
     * 刷新微信公众号授权token定时任务cron表达式
     */
    public static final String REFRESH_WEI_XIN_AUTHORIZER_TOKEN_JOB_CRON_EXPRESSION = "refresh.wei.xin.authorizer.token.job.cron.expression";

    /**
     * 餐饮业态当前分区
     */
    public static final String CATERING_CURRENT_PARTITION_CODE = "catering.current.partition.code";

    /**
     * 零售业态当前分区
     */
    public static final String RETAIL_CURRENT_PARTITION_CODE = "retail.current.partition.code";

    /**
     * 基础服务免费试用天数
     */
    public static final String BASIC_SERVICES_GOODS_FREE_TRIAL_DAYS = "basic.services.goods.free.trial.days";
}
