package ir.piana.dev.common.schedulers;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class FixedIntervalScheduler implements SchedulingConfigurer, Runnable {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int initialDelay;
    private String initialDelayUnit;
    private int period;
    private String periodUnit;
    private String activeFrom;
    private String activeTo;

    @Setter(AccessLevel.NONE)
    private AtomicInteger counter = new AtomicInteger(0);


    @Override
    public final void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        logger.info("{} scheduler try to start", getSchedulerName());
        taskRegistrar.setScheduler(Executors.newSingleThreadScheduledExecutor());
        taskRegistrar.addTriggerTask(() -> {
            var now = Instant.now();

            var nowInTehran = LocalDateTime.now(ZoneId.of("Asia/Tehran"));
            var activeFrom = LocalTime.parse(this.activeFrom);
            var activeTo = LocalTime.parse(this.activeTo);

            if (nowInTehran.toLocalTime().isAfter(activeFrom) && nowInTehran.toLocalTime().isBefore(activeTo)) {
                logger.info("{} scheduler start running for the {} time", getSchedulerName(), counter.incrementAndGet());
                this.run();
            }
        }, SchedulerInstantUtil.trigger(
                initialDelay, initialDelayUnit,
                period, periodUnit));
        logger.info("{} scheduler is running ", getSchedulerName());
    }

    public abstract String getSchedulerName();
}
