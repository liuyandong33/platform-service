package build.dream.platform.jobs;

import build.dream.common.utils.ConfigurationUtils;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    public void scheduler() {
        try {
            // 启动同步门店信息定时任务
            startSynchronizeBranchInfoJob();

            // 启用禁用门店产品定时任务
            startDisableBranchGoodsJob();

            // 启动刷新微信授权token定时任务
            startRefreshWeiXinAuthorizerTokenJob();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stopJob(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);

            scheduler.deleteJob(jobKey);
        }
    }

    private void startSynchronizeBranchInfoJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("synchronizeBranchInfoJob", "platformJobGroup");
        TriggerKey triggerKey = TriggerKey.triggerKey("synchronizeBranchInfoJobTrigger", "platformJobGroup");
        String synchronizeBranchInfoJobCronExpression = ConfigurationUtils.getConfiguration(Constants.SYNCHRONIZE_BRANCH_INFO_JOB_CRON_EXPRESSION);
        if (StringUtils.isNotBlank(synchronizeBranchInfoJobCronExpression)) {
            stopJob(jobKey, triggerKey);

            JobDetail synchronizeBranchInfoJobDetail = JobBuilder.newJob(SynchronizeBranchInfoJob.class).withIdentity(jobKey).build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(synchronizeBranchInfoJobCronExpression);
            Trigger synchronizeBranchInfoJobCronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(synchronizeBranchInfoJobDetail, synchronizeBranchInfoJobCronTrigger);
        } else {
            stopJob(jobKey, triggerKey);
        }
    }

    private void startDisableBranchGoodsJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("disableBranchGoodsJob", "platformJobGroup");
        TriggerKey triggerKey = TriggerKey.triggerKey("disableBranchGoodsJobTrigger", "platformJobGroup");
        String disableBranchGoodsJobCronExpression = ConfigurationUtils.getConfiguration(Constants.DISABLE_BRANCH_GOODS_JOB_CRON_EXPRESSION);
        if (StringUtils.isNotBlank(disableBranchGoodsJobCronExpression)) {
            stopJob(jobKey, triggerKey);

            JobDetail disableBranchGoodsJobDetail = JobBuilder.newJob(DisableBranchGoodsJob.class).withIdentity(jobKey).build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(disableBranchGoodsJobCronExpression);
            Trigger disableBranchGoodsJobCronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(disableBranchGoodsJobDetail, disableBranchGoodsJobCronTrigger);
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

            JobDetail refreshWeiXinAuthorizerTokenJobDetail = JobBuilder.newJob(RefreshWeiXinAuthorizerTokenJob.class).withIdentity(jobKey).build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(refreshWeiXinAuthorizerTokenJobCronExpression);
            Trigger refreshWeiXinAuthorizerTokenJobCronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(refreshWeiXinAuthorizerTokenJobDetail, refreshWeiXinAuthorizerTokenJobCronTrigger);
        } else {
            stopJob(jobKey, triggerKey);
        }
    }
}
