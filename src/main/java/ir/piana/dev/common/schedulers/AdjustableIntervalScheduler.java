package ir.piana.dev.common.schedulers;

import ir.piana.dev.common.util.FinalContainer;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AdjustableIntervalScheduler implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int initialDelay;
    private String initialDelayUnit;

    private String activeFrom;
    private String activeTo;

    @Autowired
    private TaskScheduler scheduler;

    @Setter(AccessLevel.NONE)
    private AtomicInteger counter = new AtomicInteger(0);

    @Setter(AccessLevel.NONE)
    private FinalContainer<ScheduledFuture<?>> container = new FinalContainer<>();
    private FinalContainer<IntervalByUnit> lastInterval = new FinalContainer<>();

    @EventListener(ApplicationReadyEvent.class)
    void createScheduler() {
        addTaskToScheduler(initialDelay, ChronoUnit.valueOf(initialDelayUnit));
    }

    protected abstract IntervalByUnit exec();

    @Override
    public final void run() {
        logger.info("{} scheduler start running for the {} time", getSchedulerName(), counter.incrementAndGet());
        if (Objects.nonNull(this.activeFrom) && Objects.nonNull(this.activeTo)) {
            var nowInTehran = LocalDateTime.now(ZoneId.of("Asia/Tehran"));
            var activeFrom = LocalTime.parse(this.activeFrom);
            var activeTo = LocalTime.parse(this.activeTo);
            if (nowInTehran.toLocalTime().isAfter(activeFrom) && nowInTehran.toLocalTime().isBefore(activeTo)) {
                executeAndReset();
            }
        } else {
            executeAndReset();
        }
    }

    private void executeAndReset() {
        try {
            IntervalByUnit interval = exec();
            if (interval != null &&
                    (lastInterval.get().interval != interval.interval || lastInterval.get().unit != interval.unit)) {
                removeTaskFromScheduler();
                addTaskToScheduler(interval.interval, interval.unit);
            }
        } catch (Throwable t) {
            logger.error(getSchedulerName() + "led to error : ", t);
        }

    }

    /*Instant getInstant(int sec) {
        return LocalDateTime.now().plusSeconds(sec).atZone(ZoneId.systemDefault()).toInstant();
    }*/

    private Instant getInstant(int period, ChronoUnit periodUnit) {
        return LocalDateTime.now().plus(period, periodUnit).atZone(ZoneId.systemDefault()).toInstant();
    }

    private void addTaskToScheduler(int period, ChronoUnit periodUnit) {
        logger.info("{} scheduler try to reset with {}({})", getSchedulerName(), periodUnit.name(), period);
        ScheduledFuture<?> scheduledTask = scheduler.schedule(this, getInstant(period, periodUnit));
        container.set(scheduledTask);
        lastInterval.set(new IntervalByUnit(period, periodUnit));
        logger.info("{} scheduler reseted to {}({})", getSchedulerName(), periodUnit.name(), period);
    }

    private void removeTaskFromScheduler() {
        ScheduledFuture<?> scheduledTask = container.get();
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            container.set(null);
            lastInterval.set(null);
        }
    }

    public abstract String getSchedulerName();

    public static class IntervalByUnit {
        int interval;
        ChronoUnit unit;

        public IntervalByUnit(int interval, ChronoUnit unit) {
            this.interval = interval;
            this.unit = unit;
        }
    }
}
