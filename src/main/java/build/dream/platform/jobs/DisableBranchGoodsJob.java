package build.dream.platform.jobs;

import build.dream.common.utils.LogUtils;
import build.dream.platform.services.BranchService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class DisableBranchGoodsJob implements Job {
    private static final String DISABLE_BRANCH_GOODS_JOB_SIMPLE_NAME = DisableBranchGoodsJob.class.getSimpleName();
    @Autowired
    private BranchService branchService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            branchService.disableGoods();
        } catch (Exception e) {
            LogUtils.error("禁用门店产品失败", DISABLE_BRANCH_GOODS_JOB_SIMPLE_NAME, "execute", e);
        }
    }
}
