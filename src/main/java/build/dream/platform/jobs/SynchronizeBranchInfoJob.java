package build.dream.platform.jobs;

import build.dream.platform.services.BranchService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class SynchronizeBranchInfoJob implements Job {
    private static final String SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME = "SynchronizeBranchInfoJob";
    @Autowired
    private BranchService branchService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            branchService.synchronizeBranchInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
