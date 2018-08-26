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
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            // 启动同步门店信息定时任务
            String synchronizeBranchInfoJobCronExpression = ConfigurationUtils.getConfiguration(Constants.SYNCHRONIZE_BRANCH_INFO_JOB_CRON_EXPRESSION);
            if (StringUtils.isNotBlank(synchronizeBranchInfoJobCronExpression)) {
                JobDetail synchronizeBranchInfoJobDetail = JobBuilder.newJob(SynchronizeBranchInfoJob.class).withIdentity("synchronizeBranchInfoJob", "platformJobGroup").build();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(synchronizeBranchInfoJobCronExpression);
                Trigger synchronizeBranchInfoJobCronTrigger = TriggerBuilder.newTrigger().withIdentity("synchronizeBranchInfoJobTrigger", "platformJobGroup").withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(synchronizeBranchInfoJobDetail, synchronizeBranchInfoJobCronTrigger);
            }

            // 启用禁用门店产品定时任务
            String disableBranchGoodsJobCronExpression = ConfigurationUtils.getConfiguration(Constants.DISABLE_BRANCH_GOODS_JOB_CRON_EXPRESSION);
            if (StringUtils.isNotBlank(disableBranchGoodsJobCronExpression)) {
                JobDetail disableBranchGoodsJobDetail = JobBuilder.newJob(DisableBranchGoodsJob.class).withIdentity("disableBranchGoodsJob", "platformJobGroup").build();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(disableBranchGoodsJobCronExpression);
                Trigger disableBranchGoodsJobCronTrigger = TriggerBuilder.newTrigger().withIdentity("disableBranchGoodsJobTrigger", "platformJobGroup").withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(disableBranchGoodsJobDetail, disableBranchGoodsJobCronTrigger);
            }

            // 启动刷新微信授权token定时任务
            String refreshWeiXinAuthorizerTokenJobCronExpression = ConfigurationUtils.getConfiguration(Constants.REFRESH_WEI_XIN_AUTHORIZER_TOKEN_JOB_CRON_EXPRESSION);
            if (StringUtils.isNotBlank(refreshWeiXinAuthorizerTokenJobCronExpression)) {
                JobDetail refreshWeiXinAuthorizerTokenJobDetail = JobBuilder.newJob(RefreshWeiXinAuthorizerTokenJob.class).withIdentity("refreshWeiXinAuthorizerTokenJob", "platformJobGroup").build();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(refreshWeiXinAuthorizerTokenJobCronExpression);
                Trigger refreshWeiXinAuthorizerTokenJobCronTrigger = TriggerBuilder.newTrigger().withIdentity("refreshWeiXinAuthorizerTokenJobTrigger", "platformJobGroup").withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(refreshWeiXinAuthorizerTokenJobDetail, refreshWeiXinAuthorizerTokenJobCronTrigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
