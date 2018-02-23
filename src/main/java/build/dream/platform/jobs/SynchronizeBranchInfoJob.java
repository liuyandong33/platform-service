package build.dream.platform.jobs;

import build.dream.common.utils.LogUtils;
import build.dream.platform.services.BranchService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SynchronizeBranchInfoJob implements Job {
    private static final String SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME = "SynchronizeBranchInfoJob";
    @Autowired
    private BranchService branchService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            branchService.synchronizeBranchInfo();
        } catch (Exception e) {
            LogUtils.error("同步门店信息失败", SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME, "execute", e);
        }
    }
}
