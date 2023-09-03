package ir.piana.dev.common.schedulers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SchedulerAutoConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        var concurrentTaskScheduler = new ConcurrentTaskScheduler();
        concurrentTaskScheduler.setConcurrentExecutor(Executors.newFixedThreadPool(10));
        return concurrentTaskScheduler;
    }
}
