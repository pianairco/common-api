package ir.piana.dev.common.service;

import ir.piana.dev.common.schedulers.AdjustableIntervalScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UniqueIdService extends AdjustableIntervalScheduler {
    private AtomicLong id;

    public long getId() {
        return id.getAndIncrement();
    }

    @Override
    protected IntervalByUnit exec() {
        int nowSecond = LocalDateTime.now().getSecond();
        long startOfDaySecond = LocalDate.now().atStartOfDay().getSecond();
        id = new AtomicLong((nowSecond - startOfDaySecond) * 10000);

        return new IntervalByUnit(
                LocalDate.now().atStartOfDay().plusDays(1).getSecond() - nowSecond,
                ChronoUnit.SECONDS);
    }

    @Override
    public String getSchedulerName() {
        return "uniqueIdService";
    }
}
