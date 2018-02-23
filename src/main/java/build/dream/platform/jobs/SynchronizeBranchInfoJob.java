package build.dream.platform.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SynchronizeBranchInfoJob implements Job {
    private static final String SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME = "SynchronizeBranchInfoJob";

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println(SYNCHRONIZE_BRANCH_INFO_JOB_SIMPLE_NAME);
    }
}
