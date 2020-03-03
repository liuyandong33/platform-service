package build.dream.platform.jobs;

import build.dream.common.models.job.ScheduleCronJobModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.JobUtils;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class JobScheduler implements Serializable {
    public void scheduler() {
        // 启用禁用门店产品定时任务
        ApplicationHandler.callMethodSuppressThrow(() -> startDisableBranchGoodsJob());

        // 启动刷新微信授权token定时任务
        ApplicationHandler.callMethodSuppressThrow(() -> startRefreshWeiXinAuthorizerTokenJob());
    }

    private void stopJob(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
        if (JobUtils.checkExists(jobKey) && JobUtils.checkExists(triggerKey)) {
            JobUtils.pauseTrigger(triggerKey);
            JobUtils.unscheduleJob(triggerKey);
            JobUtils.deleteJob(jobKey);
        }
    }

    private void startDisableBranchGoodsJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("Disable_Branch_Goods_Job", "Platform_Job_Group");
        TriggerKey triggerKey = TriggerKey.triggerKey("Disable_Branch_Goods_Trigger", "Platform_Trigger_Group");
        String disableBranchGoodsJobCronExpression = ConfigurationUtils.getConfiguration(Constants.DISABLE_BRANCH_GOODS_JOB_CRON_EXPRESSION);
        stopJob(jobKey, triggerKey);
        if (StringUtils.isNotBlank(disableBranchGoodsJobCronExpression)) {
            stopJob(jobKey, triggerKey);
            ScheduleCronJobModel scheduleCronJobModel = ScheduleCronJobModel.builder()
                    .jobName("Disable_Branch_Goods_Job")
                    .jobGroup("Platform_Job_Group")
                    .jobClass(DisableBranchGoodsJob.class)
                    .triggerName("Disable_Branch_Goods_Job_Trigger")
                    .triggerGroup("Platform_Trigger_Group")
                    .cronExpression(disableBranchGoodsJobCronExpression)
                    .build();
            JobUtils.scheduleCronJob(scheduleCronJobModel);
        }
    }

    public void startRefreshWeiXinAuthorizerTokenJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("Refresh_Wei_Xin_Authorizer_Token_Job", "Platform_Job_Group");
        TriggerKey triggerKey = TriggerKey.triggerKey("Refresh_Wei_Xin_Authorizer_Token_Trigger", "Platform_Trigger_Group");
        String refreshWeiXinAuthorizerTokenJobCronExpression = ConfigurationUtils.getConfiguration(Constants.REFRESH_WEI_XIN_AUTHORIZER_TOKEN_JOB_CRON_EXPRESSION);
        stopJob(jobKey, triggerKey);
        if (StringUtils.isNotBlank(refreshWeiXinAuthorizerTokenJobCronExpression)) {
            stopJob(jobKey, triggerKey);
            ScheduleCronJobModel scheduleCronJobModel = ScheduleCronJobModel.builder()
                    .jobName("Refresh_Wei_Xin_Authorizer_Token_Job")
                    .jobGroup("Platform_Job_Group")
                    .jobClass(RefreshWeiXinAuthorizerTokenJob.class)
                    .triggerName("Refresh_Wei_Xin_Authorizer_Token_Trigger")
                    .triggerGroup("Platform_Trigger_Group")
                    .cronExpression(refreshWeiXinAuthorizerTokenJobCronExpression)
                    .build();
            JobUtils.scheduleCronJob(scheduleCronJobModel);
        }
    }
}
