package build.dream.platform.jobs;

import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.BranchService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SynchronizeBranchInfoJob implements Job {
    private static final String SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME = "SynchronizeBranchInfoJob";
    @Autowired
    private BranchService branchService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            boolean setnxSuccessful = CacheUtils.setnx(Constants.KEY_SYNCHRONIZE_BRANCH_INFO_JOB_EXECUTE_SIGN, UUID.randomUUID().toString());
            if (setnxSuccessful) {
                branchService.synchronizeBranchInfo();
                CacheUtils.expire(Constants.KEY_SYNCHRONIZE_BRANCH_INFO_JOB_EXECUTE_SIGN, 30, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            CacheUtils.delete(Constants.KEY_SYNCHRONIZE_BRANCH_INFO_JOB_EXECUTE_SIGN);
            LogUtils.error("同步门店信息失败", SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME, "execute", e);
        }
    }
}
