package build.dream.platform.jobs;

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
        try {
            // 启用禁用门店产品定时任务
            startDisableBranchGoodsJob();

            // 启动刷新微信授权token定时任务
            startRefreshWeiXinAuthorizerTokenJob();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stopJob(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
        if (JobUtils.checkExists(jobKey) && JobUtils.checkExists(triggerKey)) {
            JobUtils.pauseTrigger(triggerKey);
            JobUtils.unscheduleJob(triggerKey);
            JobUtils.deleteJob(jobKey);
        }
    }

    private void startDisableBranchGoodsJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("disableBranchGoodsJob", "platformJobGroup");
        TriggerKey triggerKey = TriggerKey.triggerKey("disableBranchGoodsJobTrigger", "platformJobGroup");
        String disableBranchGoodsJobCronExpression = ConfigurationUtils.getConfiguration(Constants.DISABLE_BRANCH_GOODS_JOB_CRON_EXPRESSION);
        if (StringUtils.isNotBlank(disableBranchGoodsJobCronExpression)) {
            stopJob(jobKey, triggerKey);
            JobUtils.scheduleCronJob(null);
        } else {
            stopJob(jobKey, triggerKey);
        }
    }

    public void startRefreshWeiXinAuthorizerTokenJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("refreshWeiXinAuthorizerTokenJob", "platformJobGroup");
        TriggerKey triggerKey = TriggerKey.triggerKey("refreshWeiXinAuthorizerTokenJobTrigger", "platformJobGroup");
        String refreshWeiXinAuthorizerTokenJobCronExpression = ConfigurationUtils.getConfiguration(Constants.REFRESH_WEI_XIN_AUTHORIZER_TOKEN_JOB_CRON_EXPRESSION);
        if (StringUtils.isNotBlank(refreshWeiXinAuthorizerTokenJobCronExpression)) {
            stopJob(jobKey, triggerKey);
            JobUtils.scheduleCronJob(null);
        } else {
            stopJob(jobKey, triggerKey);
        }
    }
}
