package ir.piana.dev.common.schedulers;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SchedulerInstantUtil {
    public static Instant nextExecution(
            int initialDelay, String initialDelayUnit,
            int period, String periodUnit,
            TriggerContext context) {
        if (context.lastCompletionTime() != null) {
            return context.lastCompletionTime().toInstant()
                    .plus(period, ChronoUnit.valueOf(periodUnit.trim().toUpperCase()));
        } else {
            return Instant.now().plus(
                    initialDelay,
                    ChronoUnit.valueOf(initialDelayUnit.trim().toUpperCase()));
        }
    }

    public static Trigger trigger(
            int initialDelay, String initialDelayUnit,
            int period, String periodUnit) {
        return new TriggerUtil(initialDelay, initialDelayUnit, period, periodUnit);
    }

    static class TriggerUtil implements Trigger {
        private final int initialDelay;
        private final String initialDelayUnit;
        private final int period;
        private final String periodUnit;

        public TriggerUtil(int initialDelay, String initialDelayUnit, int period, String periodUnit) {
            this.initialDelay = initialDelay;
            this.initialDelayUnit = initialDelayUnit;
            this.period = period;
            this.periodUnit = periodUnit;
        }

        @Override
        public Instant nextExecution(TriggerContext triggerContext) {
            return SchedulerInstantUtil.nextExecution(
                    initialDelay, initialDelayUnit,
                    period, periodUnit, triggerContext);
        }
    }
}
