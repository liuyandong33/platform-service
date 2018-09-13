package build.dream.platform.configurations;

import build.dream.platform.constants.Constants;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by liuyandong on 2017/09/20.
 */

@Configuration
public class QuartzSchedulerConfiguration {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        Resource configLocation = new ClassPathResource(Constants.QUARTZ_PROPERTIES);
        schedulerFactoryBean.setConfigLocation(configLocation);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(platformTransactionManager);
        schedulerFactoryBean.setSchedulerName(Constants.SCHEDULER_NAME);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey(Constants.APPLICATION_CONTEXT);
        return schedulerFactoryBean;
    }

    @Bean
    public JobFactory jobFactory() {
        return new JobFactory();
    }

    public class JobFactory extends AdaptableJobFactory {
        @Autowired
        private AutowireCapableBeanFactory autowireCapableBeanFactory;

        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            Object jobInstance = super.createJobInstance(bundle);
            autowireCapableBeanFactory.autowireBean(jobInstance);
            return jobInstance;
        }
    }
}
